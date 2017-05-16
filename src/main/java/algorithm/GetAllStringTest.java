package algorithm;

/**
 * Created by gaoziye on 2016/10/27.
 */
public class GetAllStringTest {

    public static void main(String[] args) {
        char[] cs = {'a', 'b', 'c'};
        changeAndPrint(cs, 0, cs.length);
    }

    public static void changeAndPrint(char[] cs, int start, int end) {
        if (start >= end -1) {
            for (char c : cs) {
                System.out.print(c);
            }
            System.out.println();
        } else {
            for (int i = 0; i < end; i++) {
                swap(cs, start, i);
                changeAndPrint(cs, start + 1, end);
                swap(cs, start, i);
            }
        }
    }

    public static void swap(char[] cs, int a, int b) {
        char tmp = cs[a];
        cs[a] = cs[b];
        cs[b] = tmp;
    }

}
