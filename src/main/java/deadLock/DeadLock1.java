package deadLock;

/**
 * @ClassName deadLock1
 * @Author laixiaoxing
 * @Date 2019/7/13 上午10:19
 * @Description 长时间等待资源的死锁
 * @Version 1.0
 */
public class DeadLock1 {


    private static volatile Object resourceA=new Object();
    private static volatile Object resourceB=new Object();

    public static void main(String[] args) {
        new Thread(()->{
            synchronized (resourceA){
                System.out.println("获取resourceA的锁");
                synchronized (resourceB){
                    System.out.println("获取resourceB的锁");
                    try {
                        //进入等待状态，此时会释放resource的监视器锁，但是不会释放resourceB的监视器锁
                        System.out.println("进行等待状态并释放resourceA的锁");
                        resourceA.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }).start();


        new Thread(()->{
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (resourceA){
                System.out.println("获取resourceA的锁");
                synchronized (resourceB){
                    System.out.println("获取resourceB的锁");
                    try {
                        resourceA.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }).start();



    }






}
