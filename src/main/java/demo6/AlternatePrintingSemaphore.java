package demo6;

import java.util.concurrent.Semaphore;

/**
 * @ClassName AlternatePrintingSemaphore
 * @Author laixiaoxing
 * @Date 2019/3/17 下午5:43
 * @Description 交替打印奇偶数 Semaphore实现
 * @Version 1.0
 */
public class AlternatePrintingSemaphore {


    public static void main(String[] args) throws InterruptedException {

        Semaphore semaphoreOld = new Semaphore(1);
        Semaphore semaphoreEven = new Semaphore(1);

        semaphoreOld.acquire();//让奇数先启动，所以先减掉偶数的信号量 等奇数线程来释放

        SemaphorePrintEven semaphorePrintEven = new SemaphorePrintEven(semaphoreOld, semaphoreEven);
        Thread t1 = new Thread(semaphorePrintEven);
        t1.start();

        SemaphorePrintOdd semaphorePrintOdd = new SemaphorePrintOdd(semaphoreOld, semaphoreEven);
        Thread t2 = new Thread(semaphorePrintOdd);
        t2.start();

    }

    /**
     * 使用信号量实现
     */
    static class SemaphorePrintOdd implements Runnable {

        int i = 0;
        private Semaphore semaphoreOdd;
        private Semaphore semaphoreEven;


        public SemaphorePrintOdd(Semaphore semaphoreOdd, Semaphore semaphoreEven) {
            this.semaphoreOdd = semaphoreOdd;
            this.semaphoreEven = semaphoreEven;
        }

        @Override
        public void run() {
            try {
                semaphoreOdd.acquire();//获取信号量
                while (true) {
                    i++;
                    if (i % 2 == 0) {
                        System.out.println("偶数线程：" + i);
                        semaphoreEven.release();
                        //再次申请获取偶数信号量，因为之前已经获取过，如果没有奇数线程去释放，那么就会一直阻塞在这，等待奇数线程释放
                        semaphoreOdd.acquire();//核心关键
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class SemaphorePrintEven implements Runnable {

        int i = 0;
        private Semaphore semaphoreOdd;
        private Semaphore semaphoreEven;


        public SemaphorePrintEven(Semaphore semaphoreOdd, Semaphore semaphoreEven) {
            this.semaphoreOdd = semaphoreOdd;
            this.semaphoreEven = semaphoreEven;
        }

        @Override
        public void run() {

            try {
                semaphoreEven.acquire();
                while (true) {
                    i++;
                    if (i % 2 == 1) {
                        System.out.println("奇数线程：" + i);
                        semaphoreOdd.release();
                        semaphoreEven.acquire();//再次申请获取奇数信号量，需要等偶数线程执行完然后释放该信号量，不然阻塞
                    }
                }

            } catch (Exception ex) {}


        }
    }
}
