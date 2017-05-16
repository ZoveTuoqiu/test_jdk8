package algorithm;


import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by gaoziye on 2017/1/16.
 */
public class StdDrawTest {

    public static void main(String[] args) {
        drawPoint();
    }

    public static void drawPoint() {
        int N = 100;
        StdDraw.setXscale(0, N);
        StdDraw.setYscale(0, N * N);
        StdDraw.setPenRadius(.01);
        for (int i = 1; i <= N; i++) {
            StdDraw.point(i, i);
            StdDraw.point(i, i*i);
            StdDraw.point(i, i*Math.log(i));
        }
    }

}
