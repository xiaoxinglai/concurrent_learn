/**
 * @ClassName BlockQueue
 * @Author laixiaoxing
 * @Date 2019/7/14 下午5:50
 * @Descriptio 基于数组实现阻塞队列
 * @Version 1.0
 */
public class BlockQueue<T> {

    private int lenth;

    private Object[] arr;

    //头指针，指向第一个元素
    private int header = 0;

    //尾指针 指向最后一个元素
    private int tail = 0;

    //要使用环形队列

    //元素个数
    private int count;

    public BlockQueue(int lenth) {
        this.lenth = lenth;
        this.arr = new Object[lenth];
    }

    private void put(T t) throws InterruptedException {
        synchronized (arr) {
            if (count == arr.length) {
                System.out.println("队列已满");
                arr.wait();
            } else {
                arr[tail] = t;
                tail = (tail + 1) % (lenth );
                count++;
                arr.notify();
            }
        }
    }


    private T get() throws InterruptedException {
        synchronized (arr) {
            if (count == 0) {
                System.out.println("队列已空");
                arr.wait();
                return null;
            } else {
                T t = (T) arr[header];
                header = (header + 1) % (lenth );
                count--;
                arr.notify();
                return t;
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        BlockQueue queue=new BlockQueue(3);

        new Thread(()->{
            for (int i = 0; i <100000 ; i++) {
                try {
                    System.out.println("生产者生产: "+i);
                    queue.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(()->{
            for (int i = 0; i <100000 ; i++) {
                try {
                    Integer a= (Integer) queue.get();
                    if (a!=null){
                        System.out.println("消费者消费: "+a);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
