/**
 * @ClassName ThreadLocalTest
 * @Author laixiaoxing
 * @Date 2019/7/15 下午11:41
 * @Description ThreadLocal示例
 * @Version 1.0
 */
public class ThreadLocalTest {


    //(2)创建ThreadLocal本量
    static ThreadLocal<String> localVariable = new ThreadLocal<String>();


    /*
    * 本例：开启了两个线程，在每个线程内部都设置了本地变量的值，
    * 然后调用print函数打印当前本地变量。如果打印后调用了本地变量的remove方法，则会删除本地内存中的该变量
    * */

    //（1）print函数
    static void print(String str) {
        //1.1 打印当前线程本地内存中localVariable变量的值
        System.out.println(str + " : " + localVariable.get());
        localVariable.remove();
    }

    public static void main(String[] args) {
        //        new Thread(() -> {
        //            //设置线程One中本地变量localVariable
        //            localVariable.set("threadOne local variable");
        //            //调用打印函数
        //            print("threadOne");
        //            //打印本地变量值
        //            System.out.println("threadOne remove after" + " : " + localVariable.get());
        //        }).start();
        //
        //
        //        new Thread(() -> {
        //            //设置线程One中本地变量localVariable
        //            localVariable.set("threadTwo local variable");
        //            //调用打印函数
        //            print("threadTwo");
        //            //打印本地变量值
        //            System.out.println("threadTwo remove after" + " : " + localVariable.get());
        //        }).start();


        System.out.println(stringThreadLocal.get());

        stringThreadLocal.remove();
    }

    static StringThreadLocal stringThreadLocal = new StringThreadLocal();

    static class StringThreadLocal extends ThreadLocal {
        @Override
        protected Object initialValue() {
            return "赖晓星";
        }
    }


}
