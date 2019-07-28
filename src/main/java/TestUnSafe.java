import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @ClassName TestUnSafe
 * @Author laixiaoxing
 * @Date 2019/7/25 下午11:30
 * @Description TODO
 * @Version 1.0
 */
public class TestUnSafe {

    /**
     * 获取Unsafe的实例
     */
    static final Unsafe unsafe;

    /**
     * 记录变量state在类TestUnSafe中的偏移量
     */
    static final long stateOffset;

    //变量
    private volatile long state = 0;


    static {

        try {

            /**
             * 通过发射获取unsafe的实例
              */
            Field field=Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);

            unsafe=(Unsafe)field.get(null);


            //获取state变量在类TestUnSafe中的偏移值
            stateOffset = unsafe.objectFieldOffset(TestUnSafe.class.getDeclaredField("state"));
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            throw new Error(ex);
        }


    }


    public static void main(String[] args) {
        //创建实例，并且设置state值为1
        TestUnSafe test=new TestUnSafe();
        Boolean sucees=unsafe.compareAndSwapInt(test,stateOffset,0,1);
        System.out.println(sucees);
    }


}
