package config;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jason ���� �������ù���ͬʱ������������ļ��仯����̬���Property
 */
public class LocalProperty implements IPropertySupport {
	private static final Logger logger = LoggerFactory
			.getLogger(LocalProperty.class);
	private static WatchService watcher = null;
	private static final ExecutorService executorService = Executors
			.newFixedThreadPool(1);
	private volatile ConcurrentHashMap<String, String> localConfigMap = new ConcurrentHashMap<String, String>();
	static {
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			logger.error("init local config watch fail:" + e);
		}
	}
	private final String configFile;
    /**
     * ���������ļ�
     * @param configFile
     * @param isDynamic �Ƿ�̬�������̬����ô���ļ����ʱ���Զ�ˢ������
     */
	public LocalProperty(String configFile, boolean isDynamic) {
		this.configFile = configFile;
		try {
			loadConfig();
			if (isDynamic) {
				registerWatcher();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
    /**
     * ע���ļ���������properties�ļ����������޸ġ�ɾ���䶯����ô��ˢ������
     */
	@Override
	public void registerWatcher() {
		try {
			Path dir = Paths.get(
					this.getClass().getClassLoader().getResource(configFile)
							.toURI()).getParent();
			logger.info("start watch dir:{}", dir);
			dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE);
		} catch (Exception e) {
			logger.error("registerWatcher error:{}", e);
			return;
		}
		Runnable checkConfigLoop = new Runnable() {
			@Override
			public void run() {
				while (true) {
					WatchKey key = null;
					try {
						key = watcher.take();
					} catch (InterruptedException e) {
						logger.error("checkConfigLoop InterruptedException:{}",
								e);
					}
					for (WatchEvent<?> event : key.pollEvents()) {
						WatchEvent.Kind kind = event.kind();
						if (kind == StandardWatchEventKinds.OVERFLOW) {
							continue;
						}
						// Ŀ¼�����¼������������ļ���
						WatchEvent<Path> evt = (WatchEvent<Path>) event;
						Path name = evt.context();
						if (name.toString().equals(configFile)) {
							logger.info(
									"local config file:{} change ,load new config...",
									configFile);
							try {
								loadConfig();
							} catch (Throwable e) {
								logger.error(
										"checkConfigLoop loadConfig error:{}",
										e);
							}
						} else {
							logger.debug(
									"ignore event:it was not a config file changing as name:{}",
									name);
						}
					}
					boolean valid = key.reset();
					if (!valid) {
						break; // Exit if directory is deleted
					}
				}
			}
		};
		executorService.submit(checkConfigLoop);
	}

	/**
	 * ���ر��������ļ�
	 */
	@Override
	public void loadConfig() {
		try {
			synchronized (this) {
				ConcurrentHashMap<String, String> newConfigMap = new ConcurrentHashMap<>();
				Path file = Paths.get(this.getClass().getClassLoader()
						.getResource(configFile.toString()).toURI());
				FileReader fr = new FileReader(file.toFile().getPath());
				Properties properties = new Properties();
				properties.load(fr);// load()������ͨ���ַ���ֱ�Ӽ����ļ�
				Enumeration enumeration = properties.propertyNames();
				while (enumeration.hasMoreElements()) {
					String propertyName = enumeration.nextElement().toString();
					logger.debug("localConfigMap put key:{},value:{}",
							propertyName, properties.getProperty(propertyName));
					newConfigMap.put(propertyName,
							properties.getProperty(propertyName));
				}
				if (newConfigMap.size() > 0)
					localConfigMap = newConfigMap;
			}
		} catch (Exception e) {
			logger.error("loadLocalConfig error:{}", e);
		}
	}

	@Override
	public String get(String key) {
		return localConfigMap.get(key);
	}

	/**
	 * ������е�����
	 */
	@Override
	public Properties loadAll() {
		Properties prop = new Properties();
		for (Map.Entry<String, String> entry : localConfigMap.entrySet()) {
			prop.put(entry.getKey(), entry.getValue());
		}
		return prop;
	}

	@PreDestroy
	public void destroy() {
		if (!(executorService.isShutdown() || executorService.isTerminated())) {
			executorService.shutdownNow();
		}
	}

	/**
	 * ͬ�������ļ������������ļ��־û������������ļ���
	 * @param properties
	 */
	public void syncLocalConfig(Properties properties) {
		Path file;
		try {
			file = Paths.get(this.getClass().getClassLoader()
					.getResource(configFile.toString()).toURI());
			logger.debug("Ready to sync local property, file = "
					+ file.toFile().getPath());
			UnitFile unitFile = new UnitFile(file.toFile().getPath());
			unitFile.write(properties);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
