package curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;

import javax.swing.*;

/**
 * Created by gaoziye on 2016/5/18.
 */
public class PathCacheTest
{
    private static final String PATH = "/example/cache";
    public static void main(String[] args) throws Exception
    {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        PathChildrenCache cache = new PathChildrenCache(client, PATH, true);
        cache.start();
        String changeName = "key";
        PathChildrenCacheListener cacheListener = new PathChildrenCacheListener(){
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception
            {
                System.out.println("事件类型" + event.getType() + ", 节点数据 changeName:" + changeName + ",path:" + event.getData().getPath() + " = " + new String(event.getData().getData()));
            }
        };
//        ZKPaths.mkdirs(client.getZookeeperClient().getZooKeeper(), "/example1/cache1/test031");
        cache.getListenable().addListener(cacheListener);
        client.create().forPath("/example/cache/test01", "01".getBytes());
        Thread.sleep(100);
        client.create().forPath("/example/cache/test02", "02".getBytes());
        Thread.sleep(100);
        client.setData().forPath("/example/cache/test01", "01_V2".getBytes());
        Thread.sleep(100);
        client.setData().forPath("/example/cache/test02", "02_V2".getBytes());
        Thread.sleep(100);
        for (ChildData data : cache.getCurrentData())
        {
            System.out.println("getCurrentData:" + data.getPath() + " = " + new String(data.getData()));
        }
        client.delete().forPath("/example/cache/test01");
        Thread.sleep(10);
        client.delete().forPath("/example/cache/test02");
        Thread.sleep(1000 * 5);
        cache.close();
        client.close();
        System.out.println("OK!");
    }
}
