import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName FIFOMutex
 * @Author laixiaoxing
 * @Date 2019/8/10 下午8:12
 * @Description 使用LocalSupport和队列设计一个先入先出的锁
 * @Version 1.0
 */
public class FIFOMutex {

    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<Thread>();
    /**
     * 0 无锁  1 有锁
     */
    private volatile AtomicInteger lock = new AtomicInteger(0);

    public void lock() {
        //获取当前调用该方法的线程对象
        Thread current = Thread.currentThread();
        //将该线程加入到等待队列中
        waiters.add(current);

        //当获取到的队首线程不是当前线程
        while (waiters.peek() != current || !lock.compareAndSet(0, 1)) {
            //将当前线程挂起
            LockSupport.park(this);
        }
    }


    public void unlock() {
        //获取当前调用该方法的线程对象
        Thread current = Thread.currentThread();
        if (waiters.peek()==current){
            //释放锁标志
           lock.compareAndSet(1, 0);
               waiters.remove();
               //唤醒队首线程
               LockSupport.unpark(waiters.peek());
               //将当前线程从等待队列移除
        }
    }


    public static void main(String[] args) {

        FIFOMutex fifoMutex=new FIFOMutex();

        new Thread(() -> {
            //获取锁
            fifoMutex.lock();
            System.out.println("线程1获取到了锁");

            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fifoMutex.unlock();
        }).start();


         new Thread(() -> {

             try {
                 Thread.sleep(1000L);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

             System.out.println("线程2尝试获取锁");
            fifoMutex.lock();
            System.out.println("线程2尝试获取锁成功");
            fifoMutex.unlock();
        }).start();



         new Thread(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程3直接unlock");
            fifoMutex.unlock();
            System.out.println("线程3直接unlock成功");
        }).start();

    }


}
