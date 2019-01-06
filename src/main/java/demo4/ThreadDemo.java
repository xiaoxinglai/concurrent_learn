package demo4;

import java.util.concurrent.*;

public class ThreadDemo {

    public static void main(String[] args) {

//        ExecutorService executorService= Executors.newFixedThreadPool(10);
//        for (int i = 0; i <10000000; i++) {
//           Thread t= new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        System.out.println("启动线程");
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            executorService.submit(t);
//        }
//


//        ScheduledExecutorService executorService= Executors.newScheduledThreadPool(10);
//
//        executorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                //打印当前系统秒数
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(System.currentTimeMillis()/1000);
//            }
//        },0,2,TimeUnit.SECONDS);
//
//
    }

}



