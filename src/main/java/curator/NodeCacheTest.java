package curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by gaoziye on 2016/5/19.
 */
public class NodeCacheTest {
    private static final String PATH = "/example/cache/test01";
    public static void main(String[] args) throws Exception
    {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        final NodeCache cache = new NodeCache(client, PATH);
        cache.start();
        NodeCacheListener listener = new NodeCacheListener()
        {
            @Override
            public void nodeChanged() throws Exception
            {
                ChildData data = cache.getCurrentData();
                if (null != data)
                {
                    System.out.println("节点数据" + new String(cache.getCurrentData().getData()));
                }
                else
                {
                    System.out.println("节点删除!");
                }
            }
        };
        cache.getListenable().addListener(listener);
//        client.create().creatingParentsIfNeeded().forPath(PATH, "01".getBytes());
        Thread.sleep(10);
        client.setData().forPath(PATH, "02".getBytes());
        Thread.sleep(10);
        client.setData().forPath(PATH, "03".getBytes());
        Thread.sleep(10);
        client.setData().forPath(PATH, "04".getBytes());
        Thread.sleep(10);
        client.setData().forPath(PATH, "05".getBytes());
        Thread.sleep(10);
        client.setData().forPath(PATH, "06".getBytes());
        Thread.sleep(10);
        client.setData().forPath(PATH, "07".getBytes());
        Thread.sleep(1000);

        System.out.println("take a rest!");


        client.delete().deletingChildrenIfNeeded().forPath(PATH);
        Thread.sleep(1000 * 2);
        cache.close();
        client.close();
        System.out.println("OK!");
    }

}