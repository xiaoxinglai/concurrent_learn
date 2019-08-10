import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName LockSupportDemo
 * @Author laixiaoxing
 * @Date 2019/8/10 下午4:21
 * @Description LockSupport工具类
 * @Version 1.0
 */
public class LockSupportDemo {

    public  void testPark(){
        LockSupport.park(this);
    }


    public  static void main(String[] args) {
        LockSupportDemo lockSupportDemo=new LockSupportDemo();
        lockSupportDemo.testPark();

    }
}
