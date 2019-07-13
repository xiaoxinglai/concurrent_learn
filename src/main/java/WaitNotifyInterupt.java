/**
 * @ClassName WaitNotifyInterupt
 * @Author laixiaoxing
 * @Date 2019/7/13 下午12:02
 * @Description 线程中断的例子
 * @Version 1.0
 */
public class WaitNotifyInterupt {

    static Object obj = new Object();

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            synchronized (obj) {
                try {
                    System.out.println("begin");
                    obj.wait();
                    System.out.println("end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadA.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadA.interrupt();
        System.out.println("Thread end");


    }


}
