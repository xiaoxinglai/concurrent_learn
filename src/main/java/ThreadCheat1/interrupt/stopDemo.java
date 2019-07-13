package ThreadCheat1.interrupt;

/**
 * @ClassName InterruptDemo
 * @Author laixiaoxing
 * @Date 2019/4/22 下午12:35
 * @Description 验证中断
 * @Version 1.0
 */
public class stopDemo {





    static class Mythread extends Thread {
        @Override
        public void run() {
            try {
               this.stop();

            } catch (ThreadDeath e) {
                System.out.println("进入catch");
                e.printStackTrace();
            }
            }
        }


        public static void main(String[] args) {

            Mythread mythread = new Mythread();

            mythread.start();

        }

    }
