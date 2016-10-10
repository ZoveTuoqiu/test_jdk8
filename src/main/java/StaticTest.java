import java.util.Date;

/**
 * Created by gaoziye on 2016/3/21.
 */
public class StaticTest {
    public static void main (String[] args) {
        System.out.println(5);
        staticFuction();
        System.out.println("b=" + b);
        System.out.println(new Date(1461146048007L));
    }

    static StaticTest st = new StaticTest();

    static {
        System.out.println(1);
    }

    {
    System.out.println(2);
}

    StaticTest(){
        System.out.println(3);
        System.out.println("a=" + a + ",b=" + b);
    }

    public static void staticFuction(){
        System.out.println(4);
    }

    int a = 100;
    static int b = 200;

}
