/**
 * @ClassName yield
 * @Author laixiaoxing
 * @Date 2019/7/13 下午8:13
 * @Description TODO
 * @Version 1.0
 */
public class yield {


    public static void main(String[] args) {
         new Thread(() -> {
            test();
        }).start();

        new Thread(() -> {
            test();
        }).start();


        new Thread(() -> {
            test();
        }).start();


    }

    private static void test() {
        for (int i = 0; i < 5; i++) {
            //当i=0时让出cpu执行权，放弃时间片，进行下一轮调度
            if ((i % 5) == 0) {
                System.out.println(Thread.currentThread() + "yield cpu ...");
               Thread.yield();
            }

        }
        System.out.println(Thread.currentThread() + "is over");
    }
}
