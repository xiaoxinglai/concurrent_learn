import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName RandomTest
 * @Author laixiaoxing
 * @Date 2019/7/29 上午8:45
 * @Description ThreadLocalRandom
 * @Version 1.0
 */
public class RandomTest {

    public static void main(String[] args) {

        //创建一个默认种子的随机数生成器
        Random random=new Random();
        for (int i = 0; i <10 ; i++) {
            //输出10个在0-5之间的随机数 ，包含0 不包含5
            System.out.println(random.nextInt(5));
        }


        //创建一个默认种子的随机数生成器
        ThreadLocalRandom threadLocalRandom= ThreadLocalRandom.current();
        for (int i = 0; i <10 ; i++) {
            //输出10个在0-5之间的随机数 ，包含0 不包含5
            System.out.println(threadLocalRandom.nextInt(5));
        }
    }

    //随机数生成需要一个默认的种子，是一个long类型的数字， 种子相同次每次随机生成的数组均相同
    //可以在构造函数中指定种子  Random random=new Random(1);
    //但是如果不指定，则使用默认的
    //默认种子一个原子常量和当前时间的纳秒值进行位运算 。
    //原子常量每使用一次都会更新 为静态常量，以确保全局不会出现重复的种子
//    public Random() {
//        this(seedUniquifier() ^ System.nanoTime());
//    }
//
//    private static long seedUniquifier() {
//        // L'Ecuyer, "Tables of Linear Congruential Generators of
//        // Different Sizes and Good Lattice Structure", 1999
//        for (;;) {
//            long current = seedUniquifier.get();
//            long next = current * 181783497276652981L;
//            if (seedUniquifier.compareAndSet(current, next))
//                return next;
//        }
//    }
//
//    private static final AtomicLong seedUniquifier
//            = new AtomicLong(8682522807148012L);



    //有了默认种子之后 如何生成随机数
//    public int nextInt(int bound) {
//        if (bound <= 0)
//            throw new IllegalArgumentException(BadBound);
//
    //根据老的种子生成新的种子
//        int r = next(31);
//        int m = bound - 1;
    //根据新的种子计算新的随机数
//        if ((bound & m) == 0)  // i.e., bound is a power of 2
//            r = (int)((bound * (long)r) >> 31);
//        else {
//            for (int u = r;
//                 u - (r = u % bound) + m < 0;
//                 u = next(31))
//                ;
//        }
//        return r;
//    }

    //新的随机数生成需要两个步骤，首先根据老的种子生成新的种子，然后根据新的种子来计算新的随机数

    //在单线程下每次调用nextInt都是根据老的种子算出新的种子
//    protected int next(int bits) {
//        long oldseed, nextseed;
//        AtomicLong seed = this.seed;
//        do {
//            oldseed = seed.get();
//            nextseed = (oldseed * multiplier + addend) & mask;
//        } while (!seed.compareAndSet(oldseed, nextseed));
//        return (int)(nextseed >>> (48 - bits));
//    }

    //在多线程下，多个线程可能拿到同一个种子去计算新的种子，那么就会进入cas重试的阶段，因为要保证只有一个线程可以更新老的种子为新的，
    //更新失败的线程需要获取到最新的种子再次计算，保证种子不重复才能保证随机数的随机性。

    //每个Random实例里面都有一个原子性的种子变量用来记录当前的种子值，当要生成新的随机数时需要根据当前种子计算新的种子并更新回原子变量
    //在多线程下使用单个Random实例生成随机数时，多个线程同时计算随机数来计算新的种子时，多个线程会竞争同一个原子变量的更新操作，由于原子变量的更新是cas操作，同时只有一个线程会成功，所以造成大量线程自旋重试，这会降低并发性能，于是ThreadLocalRandom应运而生。

}
