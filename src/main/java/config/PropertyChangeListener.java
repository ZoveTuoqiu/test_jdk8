package config;

import java.util.Properties;

/**
 * 
 * �����÷������ʱ����
 * @author Jason
 *
 */
public interface PropertyChangeListener {

	public void onPropertyChanged(String configFile, Properties properties);
}
