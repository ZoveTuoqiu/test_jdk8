import java.util.Arrays;

/**
 * Created by gaoziye on 2016/3/15.
 */
public class LambdaTest {

    public static void main(String[] args){
        String[] arr = {"program", "list", "c", "can", "computer"};
        Arrays.sort(arr, (m, n) -> Integer.compare(m.length(), n.length()));
        System.out.println(Arrays.toString(arr));
    }

}
