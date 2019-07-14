import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @ClassName isInterrupted
 * @Author laixiaoxing
 * @Date 2019/7/13 下午10:38
 * @Description isInterrupted和Interrupted的区别
 * @Version 1.0
 */
public class isInterrupted {
    public static void main(String[] args) {
        Thread t=new Thread(()->{
            for(;;){

            }
        });

        t.start();
        //设置中断标志
        t.interrupt();

        //获取中断标志 true
        System.out.println("t.isInterrupted: "+t.isInterrupted());

        //获取中断标志并重置  false
        System.out.println("t.Interrupted: "+t.interrupted());

        //获取中断标志并重置 false
        System.out.println("Thread.Interrupted: "+Thread.interrupted());

        //获取中断标志 true
        System.out.println("t.isInterrupted: "+t.isInterrupted());


    }
}
