package concurrent;

import java.util.concurrent.Semaphore;

/**
 * Created by gaoziye on 2016/4/13.
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);
        int n = 8;
        for (int i = 0; i < n; i++) {
            new Worker(semaphore).start();
        }

    }

    public static class Worker extends Thread{
        private Semaphore semaphore;

        public Worker(Semaphore semaphore){
            this.semaphore = semaphore;
        }

        @Override
        public void run(){
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + "ռ��");
                Thread.sleep(2000);
                semaphore.release();
                System.out.println(Thread.currentThread().getName() + "�ͷ�");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
