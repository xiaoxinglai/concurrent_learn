//package demo1.test;
//
//import demo1.test.factorsService;
//import demo1.test.factorsThread;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//
//public class demo_1_main {
//
//
//
//
//    public static void Proxymain(String[] args) {
//        factorsService factorsService = new factorsService();
//
//        List<Long> nums = Arrays.asList(1323999999999L, 1323999999999L,1323999999999L, 1323999999999L, 1323999999999L); //51323999999999L, 51323999999999L, 51323999999998L);
//        Long begin=System.currentTimeMillis();
//        for (Long num : nums) {
//            factorsService.Service(num);
//        }
//        Long end=System.currentTimeMillis();
//
//        System.out.println("5个任务计算完成，耗时:"+(end-begin)+"毫秒");
//
//        //5个任务计算完成，无缓存  耗时:23335毫秒
//
//
////        final CountDownLatch latch = new CountDownLatch(2);
////        factorsThread factorsThread = new factorsThread(latch);
////        factorsThread.setFactorsService(factorsService);
////
////
////        System.out.println("多线程的方式启动计算：");
////        Long beginT = System.currentTimeMillis();
////        for (Long num : nums) {
////            factorsThread.setNum(num);
////            Thread thread = new Thread(factorsThread);
////            thread.start();
////        }
////
////        try {
////            //多线程运行结束前一直等待
////            latch.await();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////
////        Long endT = System.currentTimeMillis();
////        System.out.println("多线程任务计算完成!耗时" + (endT - beginT) + "毫秒");
//
//        //多线程执行完成 5个任务 无缓存 10299毫秒
//
//    }
//
//}
