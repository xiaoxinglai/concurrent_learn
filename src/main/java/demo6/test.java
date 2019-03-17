package demo6;

/**
 * @ClassName test
 * @Author laixiaoxing
 * @Date 2019/3/17 下午9:45
 * @Description TODO
 * @Version 1.0
 */
public class test {
    private static Object object=new Object();

    public static void main(String[] args) {
        synchronized (object){
            System.out.println("helloWorld");

        }
    }
}
