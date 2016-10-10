package config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Զ�����õ��޸ĺ�ͬ��
 * @author Jason
 *
 */
public class RemoteProperty implements IPropertySupport {
	private static final Logger logger = LoggerFactory
			.getLogger(RemoteProperty.class);
	private ConcurrentHashMap<String, String> remoteConfigMap = new ConcurrentHashMap<String, String>();
	private static final String DEFAULT_ENCODE = "utf-8";

	private final CuratorFramework zkClient;
	private final String servicePath;
	private final String configFile;
	private final PropertyChangeListener propertyListener;
    /**
     *
     * @param servicePath  ����ڵ㣬�����õ�properties������Ľڵ�
     * @param zkClient zookeeper�ͻ���
     * @param configUseRemote �Ƿ�ʹ��Զ�̽ڵ㣬����������ʱ���Ƿ�ȥԶ�̼��������ļ�
     * @param pushtoRemote   �Ƿ����͵�Զ�̽ڵ�
     * @param isDynamic �Ƿ�̬����
     * @param propertyListener  ���÷������ʱ�Ļص�����
     */
	public RemoteProperty(String servicePath, CuratorFramework zkClient,boolean configUseRemote,
			boolean pushtoRemote, boolean isDynamic,
			PropertyChangeListener propertyListener) {
		this.servicePath = servicePath;
		this.configFile = servicePath.substring(servicePath.lastIndexOf("/"));
		this.zkClient = zkClient;
		this.propertyListener = propertyListener;
		if(configUseRemote){
			loadConfig();
		}
		/*if (isDynamic){
			registerWatcher();
		}*/
	}

	/**
	 * �ѱ��ص����������͵�Զ��
	 * @param localProperties
	 */
	public void pushConfigToRemote(Properties localProperties) {
		try {
			if(zkClient.checkExists().forPath(servicePath)!=null){
				logger.debug("Deleting old path before pushing to remote" + servicePath);
				zkClient.delete().deletingChildrenIfNeeded().forPath(servicePath);
			}
			for (Entry<Object, Object> entry : localProperties.entrySet()) {
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				String path = servicePath.concat("/").concat(key);
				if (zkClient.checkExists().forPath(path) == null) {
					logger.debug("creating path for " + path);
					zkClient.create().creatingParentsIfNeeded()
							.withMode(CreateMode.PERSISTENT).forPath(path);
					zkClient.setData().forPath(path, value.getBytes(DEFAULT_ENCODE));
				} else {
					zkClient.setData().forPath(path, value.getBytes(DEFAULT_ENCODE));
				}
			}
		} catch (Exception e) {
			logger.error("up init error:{}", e);
		}
	}

	/**
	 * ���ĳ��ֵ
	 */
	@Override
	public String get(String key) {
		return remoteConfigMap.get(key);
	}

	@Override
	public Properties loadAll() {
		Properties prop = new Properties();
		for (Entry<String, String> entry : remoteConfigMap.entrySet()) {
			prop.put(entry.getKey(), entry.getValue());
		}
		return prop;
	}

	/**
	 * ����Զ�������ļ�����Զ��zk�л�ȡ��д������map����
	 */
	@Override
	public void loadConfig() {
		try {
			if (zkClient.checkExists().forPath(servicePath) != null) {
				//������е��ӽڵ�
				List<String> childPaths = zkClient.getChildren().forPath(
						servicePath);
				//��ѯ�ӽڵ㣬������е�propertie������
				for (String path : childPaths) {
					byte[] valBytes = zkClient.getData().forPath(
							servicePath + "/" + path);
					if (valBytes != null) {
						String val = new String(valBytes, DEFAULT_ENCODE);
						remoteConfigMap.put(path.trim(), val.trim());
					}
				}
			}
		} catch (Exception e) {
			logger.error("localConfig init error:{}", e);
		}
	}

	/**
	 * ע���������Զ��zk��������ýڵ��б䶯��ʱ������֪ͨ������
	 */
	@Override
	public void registerWatcher() {
		final PathChildrenCache pathChildrenCache = new PathChildrenCache(
				zkClient, servicePath, true);
		pathChildrenCache.getListenable().addListener(
				new PathChildrenCacheListener() {
					@Override
					public void childEvent(CuratorFramework client,
							PathChildrenCacheEvent event) throws Exception {
						String path = event.getData().getPath();
						String key = path.replaceAll(servicePath + "/", "")
								.trim();
						byte[] dataBytes = event.getData().getData();
						String value ="";
						if(dataBytes!=null&&dataBytes.length>0){
							value = new String(dataBytes, DEFAULT_ENCODE);
						}
						
						if (event.getType() == null)
							return;
						switch (event.getType()) {
						case CHILD_ADDED:
							logger.debug("Child Added. key=" + key + " value=" + value);
							if (value != null
									&& value.equals(remoteConfigMap.get(key))) {
								logger.debug("Property already there, no need to add.");
								break;
							}
							remoteConfigMap.put(key, value);
							if (propertyListener != null) {
								propertyListener.onPropertyChanged(configFile,
										loadAll());
							}
							break;
						case CHILD_REMOVED:
							logger.debug("Child Removed.key=" + key + " value="
									+ value);
							remoteConfigMap.remove(key);
							if (propertyListener != null) {
								propertyListener.onPropertyChanged(configFile,
										loadAll());
							}
							break;
						case CHILD_UPDATED:
							logger.debug("Child Updated.key=" + key + " value=" + value);
							remoteConfigMap.put(key, value);
							if (propertyListener != null) {
								propertyListener.onPropertyChanged(configFile,
										loadAll());
							}
							break;
						}
					}
				});
		try {
			pathChildrenCache.start();
		} catch (Exception e) {
			logger.error("registerWatcher zookeeper path error", e);
		}
	}
}
