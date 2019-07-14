import java.util.Objects;

/**
 * @ClassName ThreadDemo
 * @Author laixiaoxing
 * @Date 2019/7/13 下午6:48
 * @Description thread
 * @Version 1.0
 */
public class ThreadDemo {

    private static Object lock = new Object();

    public static void main(String[] args) {
        new Thread(()->{
            synchronized (lock){
                try {
                    System.out.println("A休眠10秒不放弃锁");
                    Thread.sleep(10000);
                    System.out.println("A休眠10秒醒来");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();



        new Thread(()->{

            synchronized (lock){
                System.out.println("B休眠10秒不放弃锁");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("B休眠10秒醒来");

            }

        }).start();



    }
}
