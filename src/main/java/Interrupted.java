/**
 * @ClassName Interrupted
 * @Author laixiaoxing
 * @Date 2019/7/13 下午8:57
 * @Description 使用中断优化退出的例子
 * @Version 1.0
 */
public class Interrupted {

    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread(() -> {

            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread() + "  hello");
            }

        });
        t.start();

        //中断子线程
        System.out.println("main thread interrupt thread");
        t.interrupt();
        //等待子线程执行完毕


        System.out.println("main is over");

    }


}
