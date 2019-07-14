/**
 * @ClassName Interrupted
 * @Author laixiaoxing
 * @Date 2019/7/13 下午8:57
 * @Description 使用中断sleep退出的例子
 * @Version 1.0
 */
public class InterruptedSleep {

    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread(() -> {

            try {
                System.out.println("threadOne begin sleep for 2000 seconds");
                Thread.sleep(200000);
                System.out.println("threadOne awaking");
            } catch (InterruptedException e) {
                System.out.println("threadOne is interrupted while sleeping");
                return;
            }
        });


        t.start();

        Thread.sleep(1000);
        //中断子线程 让它从休眠中返回
        System.out.println("main thread interrupt thread");
        t.interrupt();
        //等待子线程执行完毕
        t.join();

        System.out.println("main is over");

    }


}
