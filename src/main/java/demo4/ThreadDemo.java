package demo4;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadDemo {

    public static void main(String[] args) {

        ExecutorService executorService= Executors.newFixedThreadPool(10);
        for (int i = 0; i <10000000; i++) {
           Thread t= new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("启动线程");
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            executorService.submit(t);
        }

    }

}



