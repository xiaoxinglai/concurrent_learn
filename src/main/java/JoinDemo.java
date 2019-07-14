/**
 * @ClassName JoinDemo
 * @Author laixiaoxing
 * @Date 2019/7/13 下午6:01
 * @Description TODO
 * @Version 1.0
 */
public class JoinDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread threadOne=new Thread(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("child threadOne over!");
        });



        Thread threadtwo=new Thread(()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("child threadTwo over!");
        });


        Thread mainThread=Thread.currentThread();

        new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainThread.interrupt();
            System.out.println("Interrupted main!");
        }).start();



        System.out.println("time:"+System.currentTimeMillis());
        threadOne.start();
        threadtwo.start();
        System.out.println("wait threadOne child thread over!");
        threadOne.join();
        System.out.println("wait threadtwo child thread over!");
        threadtwo.join();
        System.out.println("time:"+System.currentTimeMillis());

    }
}
