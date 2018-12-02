//package demo1.test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class factorsService {
//    //用map作为缓存
//    private Map<Long,List<Long>> cache=new ConcurrentHashMap<>();
//
//    //建立一个因式分解服务
//    public void Service(Long i) {
//
//        List<Long> factors = factor(i);
//    }
//
//    private List<Long> factor(Long i) {
//        //时间起点
//        long begintime = System.currentTimeMillis();
//
//        Long j = 2L;
//        Long k = 0L;
//        Long start = i;
//        List<Long> factors = new ArrayList<Long>();
//        //先从缓存中去取数据 如果为空则进入计算，否则直接取缓存
//        if (cache.get(i) == null) {
//            while (j <= i) {
//                if (i % j == 0) {
//                    i = i / j;
//                    factors.add(j);
//                } else {
//                    j++;
//                }
//                k++;
//            }
//            //计算完之后放回缓存中
//            cache.put(start, factors);
//        } else {
//            factors = cache.get(i);
//            System.out.print("由于已经有缓存，所以从缓存中取数据: ");
//        }
//
//
//
//        synchronized (this) {
//
//            System.out.print(start + "的因数分解为:  ");
//            Long var = 1L;
//            for (int i1 = 0; i1 < factors.size(); i1++) {
//                System.out.print(factors.get(i1));
//                if (i1 < factors.size() - 1) {
//                    System.out.print("X");
//                }
//
//                var *= factors.get(i1);
//            }
//            long endtime = System.currentTimeMillis();
//            System.out.print("  耗时" + (endtime - begintime) + "毫秒");
//            System.out.println();
//        }
//        return factors;
//    }
//}
