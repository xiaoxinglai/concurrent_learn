/**
 * @ClassName ThreadLocalExtendTest
 * @Author laixiaoxing
 * @Date 2019/7/16 下午12:26
 * @Description threadLocal的继承(该继承指的是子线程获取父线程的ThreadLocal变量值)
 * @Version 1.0
 */
public class ThreadLocalExtendTest {


    //创建线程变量
    public  static ThreadLocal<String> threadLocal=new ThreadLocal<>();

    public static void main(String[] args) {
        threadLocal.set("hello World");

        new Thread(()->{
            System.out.println("thread:"+threadLocal.get());
        }).start();

        System.out.println("main"+threadLocal.get());



    }
}
