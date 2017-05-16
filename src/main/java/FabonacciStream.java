import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by gaoziye on 2017/1/10.
 */
public class FabonacciStream {

    public static void main(String[] args) {
        Stream<Long> fabonacci = Stream.generate(new FabonacciSupplier());
        fabonacci.skip(10).limit(10).forEach(System.out::println);
    }

    static class FabonacciSupplier implements Supplier {

        long a = 0;
        long b = 1;
        /**
         * Gets a result.
         *
         * @return a result
         */
        public Object get() {
            long x = a + b;
            a = b;
            b = x;
            return a;
        }
    }

}
