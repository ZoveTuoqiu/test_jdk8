package config;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ����LocalConfig��RemoteConfig
 */
public class PropertyUnit {
	private static final Logger logger = LoggerFactory
			.getLogger(PropertyUnit.class);
	private LocalProperty localConfig;
	private RemoteProperty remoteConfig;
	private boolean pushToRemote = false;
	private boolean configUseRemote = false;
	private String[] excludeConfigs;
	ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
   /**
    * ���õ�Ԫ��һ��PropertyUnit����һ�������ļ�
    * @param serverpath  ����Ľڵ�
    * @param configFile �����ļ�·��
    * @param excludeConfig  Ҫ���Ե��ļ�
    * @param zkClient   zookeeper���ӵĿͻ���
    * @param configUseRemote  �Ƿ�ʹ��Զ�̽ڵ�
    * @param pushToRemote     ���������Ƿ����͵�Զ��
    * @param isDynamic        �Ƿ�̬�������������ļ�
    * @param propertyListener ���������ļ��Ķ���Ļص�����
    */
	public PropertyUnit(String serverpath, String configFile,String excludeConfig,
			CuratorFramework zkClient,boolean configUseRemote, boolean pushToRemote, boolean isDynamic,
			PropertyChangeListener propertyListener) {
		this.pushToRemote = pushToRemote;
		this.configUseRemote = configUseRemote;
		localConfig = new LocalProperty(configFile, isDynamic);
		if(StringUtils.isNotEmpty(excludeConfig)){
			excludeConfigs = excludeConfig.split(",");
			for(String config : excludeConfigs){
				if(configFile.contains(config)){
					logger.debug("Ignore the exclude config:" + excludeConfig);
					return;
				}

			}
		}

		remoteConfig = new RemoteProperty(serverpath, zkClient, configUseRemote,pushToRemote,
				isDynamic, new PropertyChangeListener() {
					@Override
					public void onPropertyChanged(String configFile,
							Properties properties) {
						logger.debug("Remote config changed,sync local config");
						localConfig.syncLocalConfig(properties);
					}
				});
		if (pushToRemote) {
			logger.debug("Push Local config " + configFile + " to remote!");
			remoteConfig.pushConfigToRemote(localConfig.loadAll());
		}
		if(isDynamic){
		logger.debug("Need to watch zk node chage for config " + configFile);
		service.schedule(new Runnable(){
			@Override
			public void run() {
				remoteConfig.registerWatcher();
			}
		}, 1000 * 60, TimeUnit.MILLISECONDS);
	}
	}
    /**
     * �������е����ã���ϲ��������ú�Զ������
     * @return
     */
	public Properties loadAll() {
		logger.debug("Loading all properties");
		if (pushToRemote) {
			if(remoteConfig!=null){
				remoteConfig.loadConfig();
			}
		}
		Properties localProp = localConfig.loadAll();
		Properties merged = new Properties();
		if(remoteConfig!=null){
			Properties remoteProp = remoteConfig.loadAll();
			// load localConfig first,so that remoteConfig can override localConfig
			merged.putAll(localProp);
			merged.putAll(remoteProp);
		}
		else{
			merged.putAll(localProp);
		}

		if(configUseRemote&&!pushToRemote){
			localConfig.syncLocalConfig(merged);
		}

		return merged;
	}
}
