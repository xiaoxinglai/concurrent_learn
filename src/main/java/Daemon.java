/**
 * @ClassName daemon
 * @Author laixiaoxing
 * @Date 2019/7/14 下午1:11
 * @Description TODO
 * @Version 1.0
 */
public class Daemon {

    public static void main(String[] args) {
        Thread t=new Thread(()->{
            for(;;){}
        });
        //设置为守护线程
        t.setDaemon(true);
        t.start();
        System.out.println("main thread is over");
    }

}
