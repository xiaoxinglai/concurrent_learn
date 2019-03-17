package demo4;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池
 */
public class UserDefinedThread {
    public static void main(String[] args) {

        ExecutorService executorService = new ThreadPoolExecutor(1, 2, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(100), new DefineThreadFactory(new ThreadGroup("测试线程组"),"测试线程"),new DefineRejectedExecutionHandler());


        for (int i = 0; i < 10; i++) {
            final  int a=i;
            executorService.submit(()->{
                System.out.println("线程"+a+"开始执行");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程"+a+"执行完毕");
            });
        }
    }

    static class  DefineThreadFactory implements ThreadFactory {

        public DefineThreadFactory(ThreadGroup group, String namePrefix) {
            this.group = group;
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            //线程组，runnable对象，名字，线程编号，线程堆栈大小（0表示可以忽略该参数）
            String name=namePrefix + threadNumber.getAndIncrement();
            Thread t = new Thread(group, r,
                    name,
                    0);
            System.out.println("创建了新线程"+name);
            return t;
        }
        //当前线程池编号
        private static final AtomicInteger poolNumber = new AtomicInteger(1);

        //当前线程组
        private final ThreadGroup group;
        //当前线程编号
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        //当前线程名
        private final String namePrefix;
    }

    static  class  DefineRejectedExecutionHandler implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("多余的任务被丢弃"+r.toString());
        }
    }


}

