/**
 * @ClassName NotyfyDemo
 * @Author laixiaoxing
 * @Date 2019/7/13 下午5:03
 * @Description notify
 * @Version 1.0
 */
public class NotyfyDemo {


    private static volatile Object resourceA = new Object();
    private static volatile Object resourceB = new Object();


    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("threadA get resourceA lock");
                try {
                    resourceA.wait();
                    System.out.println("threadA end wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("threadB get resourceA lock");
                try {
                    resourceA.wait();
                    System.out.println("threadB end wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();


        new Thread(() -> {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (resourceA) {
                System.out.println("threadC get resourceA lock");
                System.out.println("threadC begin notify");
                resourceA.notifyAll();
                System.out.println("threadC end notify");

            }
        }).start();


    }
}
