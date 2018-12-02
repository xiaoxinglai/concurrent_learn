package demo2.demo1Modify;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class demo2Main {

    public static void main(String[] args) {
            factorsService factorsService = new factorsService();

            List<Long> nums = Arrays.asList(1323999999999L, 1323999999999L, 1323999999999L, 1323999999999L, 1323999999999L,13239999999979L,13239999999979L); //51323999999999L, 51323999999999L, 51323999999998L);

            final CountDownLatch latch = new CountDownLatch(7);
            factorsThread factorsThread = new factorsThread(latch);
            factorsThread.setFactorsService(factorsService);


            System.out.println("多线程的方式启动计算：");
            Long beginT = System.currentTimeMillis();
            for (Long num : nums) {
                factorsThread.setNum(num);
                Thread thread = new Thread(factorsThread);
                thread.start();
            }

            try {
                //多线程运行结束前一直等待
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Long endT = System.currentTimeMillis();
            System.out.println("多线程任务计算完成!耗时共" + (endT - beginT) + "毫秒");

            //多线程执行完成 5个任务 无缓存 10299毫秒

        }
}
