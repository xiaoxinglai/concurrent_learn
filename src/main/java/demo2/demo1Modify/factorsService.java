package demo2.demo1Modify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class factorsService {

    // private Map<Long, Future<List<Long>>> cache = new ConcurrentHashMap<Long, Future<List<Long>>>();
    //实验到结果是耗费6769秒，比demo1中的10300毫秒快了，但是 出现了三次放入到缓存中的情况，理想状态是只有一次即可 ， 解决方案如下

    //增加可见性 volatile 使得可以及时刷新这个值  效果：仍然作用不大，ConcurrentHashMap虽然cache.get(i)是原子的，但是判断完之后那部分不是，可以同时多个线程进去
    private  volatile Map<Long, Future<List<Long>>> cache = new ConcurrentHashMap<Long, Future<List<Long>>>();
    //解决方法 要么使用synchronized 那区域的代码锁起来，要么使用ConcurrentHashMap的 absentSet （如果没空值放入，并返回于原来的值)


    //建立一个因式分解服务
    public List<Long> Service(Long i) {
        List<Long> factors = factor(i);
        return factors;
    }

    private List<Long> factor(Long i) {
        long begintime = System.currentTimeMillis();
        Long j = 2L;
        Long k = 0L;
        Long start = i;
        List<Long> factors = new ArrayList<Long>();

//        //---方案一
//           synchronized (this) { //效果很显然 只有一次放入缓存，并且存在多种不同的数字的时候也很效 ，（不同于demo1的串行，如果是多个不同的数字时候就成了串行）
//
//            if (cache.get(i) == null) {
//                factorsTask factorsTask = new factorsTask(i);
//                FutureTask<List<Long>> future = new FutureTask<List<Long>>(factorsTask);
//                ThreadDemo t1 = new ThreadDemo(future);
//                t1.start();
//                System.out.println("放入到缓存中");
//                cache.put(start, future);
//            }
//        }

        //---方案二--使用putIfAbsent
        factorsTask factorsFuture = new factorsTask(i);
        FutureTask<List<Long>> future = new FutureTask<List<Long>>(factorsFuture);
        if (cache.putIfAbsent(i,future) == null) {
            Thread t1 = new Thread(future);
            t1.start();
            System.out.println("放入到缓存中");
        }


        Future<List<Long>> futureR = cache.get(i);
        try {
            factors = futureR.get();
            System.out.println("从缓存中取值");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //同步下输出 这部分花不了什么时间
        synchronized (this) {
            System.out.print(start + "的因数分解为: ");
            Long var = 1L;
            for (int i1 = 0; i1 < factors.size(); i1++) {
                System.out.print(factors.get(i1));
                if (i1 < factors.size() - 1) {
                    System.out.print("X");
                }

                var *= factors.get(i1);
            }

            long endtime = System.currentTimeMillis();
            System.out.print("  此线程耗时" + (endtime - begintime) + "毫秒");
            System.out.println();
        }

        return factors;
    }


}
