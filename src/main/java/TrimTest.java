import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by gaoziye on 2016/5/13.
 */
public class TrimTest {
    public static void main (String[] args){
        try {
            String a = "1 ";
            System.out.println(Long.valueOf(a));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map map = MapUtils.EMPTY_MAP;
        System.out.println("value is :" + map.get("a"));
    }
}
