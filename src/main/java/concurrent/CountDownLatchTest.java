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
                    System.out.println(Thread.currentThread().getName()+ "��ʼ");
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "���");
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
                    System.out.println(Thread.currentThread().getName()+"��ʼ��");
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "���");
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            System.out.println("���߳̽���");
            countDownLatch.await();
            System.out.println("���߳����");
            System.out.println("����");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
