package ThreadCheat1.interrupt;

/**
 * @ClassName InterruptDemo
 * @Author laixiaoxing
 * @Date 2019/4/22 下午12:35
 * @Description 验证中断
 * @Version 1.0
 */
public class InterruptDemo {

    static class Mythread implements Runnable {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 500000; i++) {
                    if (Thread.interrupted()) {
                        System.out.println("检测到中断状态");
                        throw new InterruptedException();
                    }
                    System.out.println("i=" + (i + 1));
                }

                System.out.println("其他处理逻辑，中断后不执行");
            } catch (InterruptedException e) {
                System.out.println("捕获到中断异常之后");
                e.printStackTrace();
            }
            }
        }


        public static void main(String[] args) {

            Mythread mythread = new Mythread();
            Thread thread = new Thread(mythread);
            thread.start();
            try {
                Thread.sleep(2000);
                thread.interrupt();//中断
            } catch (InterruptedException e) {
                System.out.println("main catch");
                e.printStackTrace();
            }
        }

    }
