import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gaoziye on 2016/7/8.
 */
public class StreamTest {

    public static void main(String[] args) throws IllegalAccessException {
        Stream<String> stream = Stream.of("a", "b", "c", "d");
        stream.forEach(s -> System.out.println(s));
//        System.out.println(stream.collect(Collectors.joining()));
//        System.out.println(stream.collect(Collectors.joining(", ")));

        for (Field field : A.class.getDeclaredFields()) {
            System.out.println(field.get(null));
        }
    }

}
class A {
    public static String A = "a";
    public static String B = "b";
}
