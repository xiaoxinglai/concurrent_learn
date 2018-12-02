package demo1;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class factorsService {

    //  private Map<Long,List<Long>> cache=new HashMap<Long, List<Long>>();
    private Map<Long, List<Long>> cache = new ConcurrentHashMap<Long, List<Long>>();

    //应该存feture 要看下缓存里面有没有同样的任务在执行，如果有，那么应该等待这个任务完成，而不是另起一个任务 详情看demo2

    //建立一个因式分解服务
    public void Service(Long i) {
        List<Long> factors = factor(i);
    }

    private List<Long> factor(Long i) {
        long begintime = System.currentTimeMillis();
        Long j = 2L;
        Long k = 0L;
        Long start = i;
        List<Long> factors = new ArrayList<Long>();

        // synchronized (this) {  不加同步 所有的线程都会进去，然后发现没有，就都开始计算，导致跟没有缓存是一个区别  ，
        // 加了同步，那么有缓存 去缓存，没有缓存，计算，就不会重复计算了，但是结果就是 堵塞了，如果多个任务都是不同的值，那跟串行没有区别
        //改用currentHashmap之后 就只是get和put是同步锁住的，这里的话 因为计算时间很长，仍然是等于没缓存，但是比整个synchronized要好 起码在不同的值的情况下它是并行的 而不是串行
        //并且 如果计算量可以忽略不计 那么这个方式最优
        //但是计算量特别大的情况下，取缓存的时候，要看下缓存里面有没有同样的任务在执行，如果有，那么应该等待这个任务完成，而不是另起一个任务
        if (cache.get(i) == null) {
            while (j <= i) {
                if (i % j == 0) {
                    i = i / j;
                    factors.add(j);
                } else {
                    j++;
                }
                k++;
            }
            cache.put(start, factors);
        } else {
            factors = cache.get(i);
        }
        //  }
        System.out.println("计算结束，循环了" + k + "次");
        System.out.print(start + "的因数分解为: ");
        Long var = 1L;
        for (int i1 = 0; i1 < factors.size(); i1++) {
            System.out.print(factors.get(i1));
            if (i1 < factors.size() - 1) {
                System.out.print("X");
            }

            var *= factors.get(i1);
        }
        System.out.println();
        System.out.println("验证：分解出来因数相乘结果为:" + var);
        long endtime = System.currentTimeMillis();
        System.out.println("耗时" + (endtime - begintime) + "毫秒");
        return factors;
    }


}
