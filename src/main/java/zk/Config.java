package zk;

/**
 * Created by gaoziye on 2016/3/23.
 */
public interface Config {

    byte[] getConfig(String path) throws Exception;
}
