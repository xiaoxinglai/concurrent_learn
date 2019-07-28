import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @ClassName ThreadLocalExtendTest
 * @Author laixiaoxing
 * @Date 2019/7/16 下午12:26
 * @Description threadLocal的继承(该继承指的是子线程获取父线程的ThreadLocal变量值)
 * @Version 1.0
 */
public class InheritableThreadLocalTest {


    //创建线程变量
    public static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) throws IOException {
        threadLocal.set("hello World");

        new Thread(() -> {
            System.out.println("thread:" + threadLocal.get());
        }).start();

        System.out.println("main" + threadLocal.get());


    }
}
