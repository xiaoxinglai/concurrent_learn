package demo2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FutureTest {
    //实现Callable接口
    public static class Task implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("execute!!!");
            return "complete";
        }
    }

    public static void main(String[] args) throws InterruptedException,
            ExecutionException {
        List<Future<String>> results = new ArrayList<Future<String>>();
        //线程池的方式
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        for (int i = 0; i < 10; i++) {
//            results.add(executorService.submit(new Task()));
//        }
//        for (Future<String> future : results) {
//            System.out.println(future.get());
//        }

//        System.out.println("Main complete");
//
//        if (!executorService.isShutdown()) {
//            executorService.shutdown();
//        }

        //普通线程的方式


        System.out.println("futrue任务开始");
        //建立实现了callable接口的任务
        Task task=new Task();
        //将实现了callble接口的任务放到Future里面
        FutureTask<String> futureTask=new FutureTask<String>(task);
        //将future放到线程thread里面
        Thread t=new Thread(futureTask);
        //启动线程
        t.start();
        System.out.println("线程启动");
        //获取线程执行之后结果输出
        System.out.println("future任务结束，输出:"+futureTask.get());



    }

}
