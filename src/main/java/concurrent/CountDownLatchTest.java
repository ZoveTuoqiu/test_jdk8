package concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * Created by gaoziye on 2016/4/12.
 */
public class CountDownLatchTest {

    public static void main(String[] args){
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName()+ "开始");
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "完成");
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName()+"开始");
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "完成");
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            System.out.println("子线程进行");
            countDownLatch.await();
            System.out.println("子线程完成");
            System.out.println("继续");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
