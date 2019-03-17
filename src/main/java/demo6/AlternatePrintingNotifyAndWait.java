package demo6;

/**
 * @ClassName AlternatePrintingNotifyAndWait
 * @Author laixiaoxing
 * @Date 2019/3/17 下午7:40
 * @Description 交替打印奇偶数 notify实现
 * @Version 1.0
 */
public class AlternatePrintingNotifyAndWait {

        private static Object object = new Object();



        private static int i = 0;

        public static void main(String[] args) throws InterruptedException {
            new Thread(() -> {

                while (true) {
                    System.out.println("进入了奇数线程");
                    synchronized (object) {
                        if (i % 2 == 1) {
                            System.out.println("奇数线程" + i++);
                            try {

                                System.out.println("奇数线程notify");
                                object.notify();

                                System.out.println("奇数线程wait");
                                object.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            try {
                                System.out.println("奇数线程wait");
                                object.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();



            new Thread(() -> {
                while (true) {
                    System.out.println("进入了偶数线程");
                    synchronized (object) {
                        if (i % 2 == 0) {
                            System.out.println("偶数线程" + i++);
                            try {
                                System.out.println("偶数线程notify");
                                object.notify();

                                System.out.println("偶数线程wait");
                                object.wait();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();


        }

}




