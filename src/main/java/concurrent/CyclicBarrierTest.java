package concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by gaoziye on 2016/4/12.
 */
public class CyclicBarrierTest {

    public static void main(String[] args){
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(4);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                System.out.println("当前线程：" + Thread.currentThread().getName());
            }
        });
        int n = 4;
        for (int i = 0; i < n; i++){
            new Worker(cyclicBarrier).start();
        }
//        for (int i = 0; i < n; i++){
//            if (i < n-1) {
//                new Worker(cyclicBarrier).start();
//            } else {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                new Worker(cyclicBarrier).start();
//            }
//        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("重用");

        for (int i = 0; i < n; i++){
            new Worker(cyclicBarrier).start();
        }

    }

    static class Worker extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Worker(CyclicBarrier cyclicBarrier){
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run(){
            System.out.println(Thread.currentThread().getName() + "开始");
            try {
                Thread.sleep(5000);
                System.out.println(Thread.currentThread().getName() + "完成，等待");
                cyclicBarrier.await();
//                cyclicBarrier.await(2000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
//            catch (TimeoutException e) {
//                e.printStackTrace();
//            }
            System.out.println("所有完成，继续");
        }

    }

}
