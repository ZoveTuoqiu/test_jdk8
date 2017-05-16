package config;

import java.util.Properties;

/**
 * @author Jason
 *   �����ļ��Ļ��࣬�����Ǹ��»��߲��������ļ����������·�����ʵ�ָ���
 */
public interface IPropertySupport {
	/**
	 * �������е�������
	 * @return
	 */
    public Properties loadAll();
    /**
     * ��������
     */
    public void loadConfig();
    /**
     * ע�����
     */
    public void registerWatcher();
    /**
     * ���ĳ��ֵ
     * @param key
     * @return
     */
    public String get(String key);
}
