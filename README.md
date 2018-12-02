# concurrent_learn
java并发练习笔记


##### java并发编程实战之从因数分解看多线程
***
这篇博文主要通过对多个大数的因数分解进行实践，进而引出初步的多线程的一些用法和思考。

1.因数分解
> 因数分解是对一个数字m,找出它所有的因数的过程，比如说数字6 因数分解之后为 2x3

2.为何选择因数分解

> 因为因数分解是一个很费事的计算过程，目前没有很好的算法可以提高效率，可以很好的用来模拟复杂计算处理过程，以便于我们明显的去观察在不同的情况下线程的状态

3. 因数分解的算法代码如下

> 具体就是先从2开始除，看2能不能被整除，如果可以则继续除以2，如果不能，则除以3 以此类推，直到除数不在小于m

代码如下

```
public class factorsService {

    //建立一个因式分解服务
    public void Service(Long i) {

        List<Long> factors = factor(i);
    }

    private List<Long> factor(Long i) {
        //时间起点
        long begintime = System.currentTimeMillis();

        Long j = 2L;
        Long k = 0L;
        Long start = i;
        List<Long> factors = new ArrayList<Long>();
            while (j <= i) {
                if (i % j == 0) {
                    i = i / j;
                    factors.add(j);
                } else {
                    j++;
                }
                k++;
            }


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

```

main方法如下

```
 public static void main(String[] args) {
        factorsService factorsService = new factorsService();

        List<Long> nums = Arrays.asList(1323999999999L, 1323999999999L); 
        Long begin=System.currentTimeMillis();
        for (Long num : nums) {
            factorsService.Service(num);
        }
        Long end=System.currentTimeMillis();

        System.out.println("2个任务计算完成，耗时:"+(end-begin)+"毫秒");
}

```

我们来看下输出， 这是对两个Long型的整数进行因数分解，此时没有使用多线程，串行执行

运行结果如下

> 计算结束，循环了374328530次  
1323999999999的因数分解为: 3X3X3X131X374328527  
验证：分解出来因数相乘结果为:1323999999999  
耗时5374毫秒  
计算结束，循环了374328530次  
1323999999999的因数分解为: 3X3X3X131X374328527  
验证：分解出来因数相乘结果为:1323999999999  
耗时4698毫秒  
2个任务计算完成，耗时:10073毫秒


我们可以看到,两个数的因数分解 总计花了10073秒

那么有什么更好的方法可以提升速度吗？答案是从算法来说，其实是没有 的，大数的因数分解很难做，因此奠定了非对称加密的基石（非对称加密的秘钥生成利用是利用到了这点特性，已知积很难算出因数，但是已知因数却很容易算出积）  好了，算法考虑优化不是我们现在这个博文的主题，接下来进入正题，我们使用多线程解决

3.什么是多线程，为什么要用多线程
> 通俗的来说线程是比进程更轻量级的调度单元，一个进程里面可以包含多个线程。  
举个例子，我们常说的八核cpu  指定的就是1个cpu有8个物理处理单元，也就是最多可以同时跑8个物理线程。  （cpu还有超线程技术，可以用一个物理线程模拟多个线程）  
既然cpu如此强大，但是如果我们的程序是单线程的，那么cpu的强大功能就浪费了，单线程用不上多核。  
疑问1:我们用单线程做了两个因数分解，耗时10073毫秒，那么我们起两个线程，同时做这个因数分解的任务，那是不是只需要一半的时间?<br> 疑问2:如果我有5个数要做因数分解，那我起五个线程，是不是也可以缩小5倍时间？


4.多线程跑因数分解的任务代码如下
```
public class factorsThread implements Runnable {
    private CountDownLatch latch;

    public factorsThread(CountDownLatch latch) {
        this.latch = latch; //初始化闭锁
    }


    factorsService factorsService;

    public void setFactorsService(factorsService factorsService) {
        this.factorsService = factorsService;
    }


    Long num = 0L;

    public void setNum(Long num) {
        
        this.num = num;
    }




    public void run() {
        factorsService.Service(num);
        latch.countDown();  //CountDownLatch在执行完run后减1
    }
}
```


CountDownLatch 闭锁，一开始维护一个初始变量值，执行countDown()之后变量值减1

引入这个CountDownLatch是为了在某一个点同步线程，让线程在同一个地方停下来，可以想象为一道门，然后多个线程都在这个门前停下来，好方便我们统计多线程的执行的时间。

```CountDownLatch```的用法就是，先在要使用CountDownLatch的线程类中声明一个CountDownLatch属性,并在run方法执行完之后调用 `latch.countDown(); `将计数器减1:
例如
```
public class factorsThread implements Runnable {
    private CountDownLatch latch;

    public factorsThread(CountDownLatch latch) {
        this.latch = latch; //初始化闭锁
    }
    
....
 public void run() {
        factorsService.Service(num);
        latch.countDown();  //CountDownLatch在执行完run后减1
    }


```
然后在需要使用这个Thread类的地方，创建一个CountDownLatch的对象，并传入需要同步的线程数量，然后将CountDownLatch的象
传入到Thread类latch字段值里，然后启动这个线程
```
 //创建CountDownLatch对象，2表示最终要同步的是2个线程
        final CountDownLatch latch = new CountDownLatch(2);
        factorsThread factorsThread = new factorsThread(latch);
        factorsThread.setFactorsService(factorsService);
        
        System.out.println("多线程的方式启动计算：");
        Long beginT = System.currentTimeMillis();
        for (Long num : nums) {
            factorsThread.setNum(num);
            Thread thread = new Thread(factorsThread);//新建线程
            thread.start();
        }
        
        ...
```

最后在需要等待所有线程完成任务的地方
```
 try {
            //多线程运行结束前一直等待
            latch.await(); //闭锁的作用，阻塞等待 直到countDown为0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
```



具体如下：
比如说，我们并发了2个线程，要怎么统计这两个线程执行结束后花了多少时间？可以在线程启动之前，读取到当前系统时间，然后在 latch.await()之后，所有多线程执行完毕之后，再读取一次时间，时间差即是多线程执行期间的总时间。

```
 public static void main(String[] args) {
        factorsService factorsService = new factorsService();

        List<Long> nums = Arrays.asList(1323999999999L, 1323999999999L);
        //开始时间
        Long begin=System.currentTimeMillis();
        
        //创建CountDownLatch对象，2表示最终要同步的是2个线程
        final CountDownLatch latch = new CountDownLatch(2);
        factorsThread factorsThread = new factorsThread(latch);
        factorsThread.setFactorsService(factorsService);
        
        System.out.println("多线程的方式启动计算：");
        Long beginT = System.currentTimeMillis();
        for (Long num : nums) {
            factorsThread.setNum(num);
            Thread thread = new Thread(factorsThread);//新建线程
            thread.start();
        }

        try {
            //多线程运行结束前一直等待
            latch.await(); //闭锁的作用，阻塞等待 直到countDown为0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//结束时间
        Long endT = System.currentTimeMillis();
        System.out.println("多线程任务计算完成!耗时" + (endT - beginT) + "毫秒");


    }
```

输出结果：
> 多线程的方式启动计算：  
计算结束，循环了374328530次  
1323999999999的因数分解为: 3X3X3X131X374328527  
验证：分解出来因数相乘结果为:1323999999999  
耗时5721毫秒  
计算结束，循环了374328530次  
1323999999999的因数分解为: 3X3X3X131X374328527  
验证：分解出来因数相乘结果为:1323999999999  
耗时5750毫秒  
多线程任务计算完成!耗时5751毫秒  

结论：两个线程并发运行的时候，时间缩小了接近一半,解决疑问1 


疑问二，当我们5个线程的时候并发会怎样？
代码如下

main代码
```
  public static void main(String[] args) {
        factorsService factorsService = new factorsService();

        List<Long> nums = Arrays.asList(1323999999999L, 1323999999999L,1323999999999L, 1323999999999L, 1323999999999L); //51323999999999L, 51323999999999L, 51323999999998L);
        Long begin=System.currentTimeMillis();
        for (Long num : nums) {
            factorsService.Service(num);
        }
        Long end=System.currentTimeMillis();

        System.out.println("5个任务计算完成，耗时:"+(end-begin)+"毫秒");

        final CountDownLatch latch = new CountDownLatch(2);
        factorsThread factorsThread = new factorsThread(latch);
        factorsThread.setFactorsService(factorsService);

        System.out.println("多线程的方式启动计算：");
        Long beginT = System.currentTimeMillis();
        for (Long num : nums) {
            factorsThread.setNum(num);
            Thread thread = new Thread(factorsThread);
            thread.start();
        }

        try {
            //多线程运行结束前一直等待
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Long endT = System.currentTimeMillis();
        System.out.println("多线程任务计算完成!耗时" + (endT - beginT) + "毫秒");

    }

```

service代码 
```
public class factorsService {

    //建立一个因式分解服务
    public void Service(Long i) {

        List<Long> factors = factor(i);
    }

    private List<Long> factor(Long i) {
        //时间起点
        long begintime = System.currentTimeMillis();

        Long j = 2L;
        Long k = 0L;
        Long start = i;
        List<Long> factors = new ArrayList<Long>();
        while (j <= i) {
            if (i % j == 0) {
                i = i / j;
                factors.add(j);
            } else {
                j++;
            }
            k++;
        }

        synchronized (this) { //主要是新增了这块的同步，以便于打印控制台消息的时候顺序不会乱  
        //因为线程是交替执行的，这部分不同步的话打印会乱序。
        //这部分代码的同步不会影响什么时间，因为主要的时间消耗不在这部分逻辑上
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
            System.out.println("耗时" + (endtime - begintime) + "毫秒");
        }
        return factors;
    }
}

```

输出结果
> 1323999999999的因数分解为: 3X3X3X131X374328527  耗时7032毫秒  
1323999999999的因数分解为: 3X3X3X131X374328527   耗时6666毫秒  
1323999999999的因数分解为: 3X3X3X131X374328527  耗时5614毫秒  
1323999999999的因数分解为: 3X3X3X131X374328527 耗时4303毫秒 <br>1323999999999的因数分解为: 3X3X3X131X374328527  耗时4295毫秒  
5个任务计算完成，耗时:27910毫秒  
多线程的方式启动计算:  
1323999999999的因数分解为: 3X3X3X131X374328527 耗时9757毫秒    
1323999999999的因数分解为: 3X3X3X131X374328527 耗时9777毫秒  
1323999999999的因数分解为: 3X3X3X131X374328527 耗时9798毫秒  
1323999999999的因数分解为: 3X3X3X131X374328527 耗时9815毫秒  
1323999999999的因数分解为: 3X3X3X131X374328527 耗时9807毫秒  
多线程任务计算完成!耗时9779毫秒

疑问2：5个线程是否可以缩短五倍时间，答案是不能 <br>27910/9779 大概就是2.7倍的差距，并没有5倍<br>
因此提升线程并不是线性的提升效率，反而随着线程数量的增加，cpu会花费大量的时间用于处理各个线程之间的切换上<br>
切换时候需要保存各个线程的状态，又要恢复，而且各个线程之间彼此还会竞争cpu<br>
合理的线程数目约为N 或者N+1  N为cpu核心数



##### java并发编程实战从因数分解看多线程（下）
***
[java并发编程实战之从因数分解看多线程(上)](https://blog.csdn.net/qq_20009015/article/details/82847839)
从之前的那篇文章中我们已经验证了多线程可以显著提高程序的效率，但并非没有限制。那么要如何才能更进一步的提升这个程序的效率，答案是利用缓存。  
我们将已经进行过因数分解的数字和它分解之后的结果存起来，如果下次任务中发现这个数字已经被分解过了，就直接从缓存里拿，而没有必要继续去计算。

我们选用map做为缓存，改造后的因数分解的代码如下：
```
public class factorsService {
    //用map作为缓存
    private Map<Long,List<Long>> cache=new HashMap<Long, List<Long>>();
    
    //建立一个因式分解服务
    public void Service(Long i) {

        List<Long> factors = factor(i);
    }

    private List<Long> factor(Long i) {
        //时间起点
        long begintime = System.currentTimeMillis();

        Long j = 2L;
        Long k = 0L;
        Long start = i;
        List<Long> factors = new ArrayList<Long>();
        //先从缓存中去取数据 如果为空则进入计算，否则直接取缓存
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
            //计算完之后放回缓存中
            cache.put(start, factors);
        } else {
            System.out.print("由于已经有缓存，所以从缓存中取数据: ");
            factors = cache.get(i);
        }

        

        synchronized (this) {
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
        }
        return factors;
    }
}


```

main方法 先执行单线程的情况
```
  public static void main(String[] args) {
        factorsService factorsService = new factorsService();

        List<Long> nums = Arrays.asList(1323999999999L, 1323999999999L,1323999999999L, 1323999999999L, 1323999999999L); //51323999999999L, 51323999999999L, 51323999999998L);
        Long begin=System.currentTimeMillis();
        for (Long num : nums) {
            factorsService.Service(num);
        }
        Long end=System.currentTimeMillis();

        System.out.println("5个任务计算完成，耗时:"+(end-begin)+"毫秒");
    }


```

输出结果:
> 1323999999999的因数分解为:  3X3X3X131X374328527  耗时7214毫秒  
由于已经有缓存，所以从缓存中取数据: 1323999999999的因数分解为:  3X3X3X131X374328527  耗时0毫秒  
由于已经有缓存，所以从缓存中取数据: 1323999999999的因数分解为:  3X3X3X131X374328527  耗时0毫秒  
由于已经有缓存，所以从缓存中取数据: 1323999999999的因数分解为:  3X3X3X131X374328527  耗时0毫秒  
由于已经有缓存，所以从缓存中取数据: 1323999999999的因数分解为:  3X3X3X131X374328527  耗时0毫秒  
5个任务计算完成，耗时:7214毫秒


main 方法 以多线程的情况启动：

```
 public static void main(String[] args) {
        factorsService factorsService = new factorsService();

        List<Long> nums = Arrays.asList(1323999999999L, 1323999999999L,1323999999999L, 1323999999999L, 1323999999999L); //51323999999999L, 51323999999999L, 51323999999998L);
        Long begin=System.currentTimeMillis();

        final CountDownLatch latch = new CountDownLatch(2);
        factorsThread factorsThread = new factorsThread(latch);
        factorsThread.setFactorsService(factorsService);

        System.out.println("多线程的方式启动计算：");
        Long beginT = System.currentTimeMillis();
        for (Long num : nums) {
            factorsThread.setNum(num);
            Thread thread = new Thread(factorsThread);
            thread.start();
        }

        try {
            //多线程运行结束前一直等待
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Long endT = System.currentTimeMillis();
        System.out.println("多线程任务计算完成!耗时" + (endT - beginT) + "毫秒");

    }
```

输出结果:
> 多线程的方式启动计算：  
1323999999999的因数分解为:  3X3X3X131X374328527  耗时11607毫秒  
1323999999999的因数分解为:  3X3X3X131X374328527  耗时11633毫秒  
1323999999999的因数分解为:  3X3X3X131X374328527  耗时11675毫秒  
1323999999999的因数分解为:  3X3X3X131X374328527  耗时11693毫秒  
1323999999999的因数分解为:  3X3X3X131X374328527  耗时11701毫秒  
多线程任务计算完成!耗时11642毫秒  

我们可以看到，按照我们刚刚写法，多线程反而比单线程还慢了，耗费了11642毫秒。仔细观察，问题在于单线程走了缓存，而多线程没有，为什么会出现这种情况？

观察这两次控制台输出，结合代码
```
 //先从缓存中去取数据 如果为空则进入计算，否则直接取缓存
        if (cache.get(i) == null) { //竞态条件
            while (j <= i) {
                if (i % j == 0) {
                    i = i / j;
                    factors.add(j);
                } else {
                    j++;
                }
                k++;
            }
            //计算完之后放回缓存中
            cache.put(start, factors);
        } else {
            factors = cache.get(i);
            System.out.print("由于已经有缓存，所以从缓存中取数据: ");
        }
```

问题就出在  `if (cache.get(i) == null) ` 这个先判断再操作的竞态条件上，这个类已经不是线程安全的了。


***线程安全***
>当一个类的对象在多个线程中表现均一致时候可以称为线程安全，比如说不可变类，是绝对的线程安全。或者
线程中不存在共享的可以同时被被其他多个线程修改的变量，也不存在竞态条件。  
刚刚那个类中的map已经是线程间共享的可以被多个线程操作和修改的变量，且存在竞态条件，因为在多线程下可能出现不可预知的情况。  
这里就是多个线程同时进入到`map.get(i)==null`这里判断，发现是null,就都进去进行计算了，于是缓存失效


***解决方案一 synchronized同步？***
```
synchronized (this) { 
 if (cache.get(i) == null) { //竞态条件
            while (j <= i) {
                if (i % j == 0) {
                    i = i / j;
                    factors.add(j);
                } else {
                    j++;
                }
                k++;
            }
            //计算完之后放回缓存中
            cache.put(start, factors);
        } else {
            factors = cache.get(i);
            System.out.print("由于已经有缓存，所以从缓存中取数据: ");
        }
    }
```

简单粗暴的用synchronized将这段代码包起来，结果就是虽然缓存生效了，但是实际上这个代码已经退化成了单线程的代码，所有线程到了这一步关键的运算过程里，就都堵塞了，和单线程没有什么两样

**解决方案二  map.get(i) 换成原子操作？***
```
//用map作为缓存
    private Map<Long,List<Long>> cache=new ConcurrentHashMap<>();
```
使用ConcurrentHashMap代替hashMap， 这样map.get(i)操作将会加锁，同一时刻只有一个线程可以进去

但是事实上可以看到
```
 if (cache.get(i) == null) { //get为原子操作的情况下，竞态条件消除
 //但是下面的代码是一个非常耗费时间的运算
            while (j <= i) {
                if (i % j == 0) {
                    i = i / j;
                    factors.add(j);
                } else {
                    j++;
                }
                k++;
            }
            //计算完之后放回缓存中
            cache.put(start, factors);
        }
```

get为原子操作情况下，竞态条件是消除了，但是下面的运算是个非常耗费时间的操作，因此，当A线程进入发现缓存为空，于是自己去计算，但是实际上很可能这个计算任务已经有别的线程在执行了，A的最优策略应该是等待这个任务结束 ，然后去缓存里拿，而不是自己继续去执行。  
但是这个也比之前的情况下好。

***理想状态，判断条件同一个时刻只有一个线程在执行，并且如果发现任务已经在执行了，则等待该任务，而不是自己去计算，因此使用furture是非常合适的***


#### Furture
> 在并发编程中，我们经常用到非阻塞的模型，在之前的多线程的三种实现中，不管是继承thread类还是实现runnable接口，都无法保证获取到之前的执行结果。通过实现Callback接口，并用Future可以来接收多线程的执行结果。
 Future表示一个可能还没有完成的异步任务的结果，针对这个结果可以添加Callback以便在任务执行成功或失败后作出相应的操作。
 
 
 ### Furture的demo
 
 ***建立实现了callable接口的任务类***
 ```
public class FutureTest {
    //实现Callable<T>接口 T为call方法的返回值
    public static class Task implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("execute!!!");
            return "complete";
        }
    }

 ```
 
 
 ***使用如下***
 
 ```
public class FutureTest {
  
    public static void main(String[] args) throws InterruptedException,
            ExecutionException {
        
        System.out.println("futrue任务开始");
        //建立实现了callable接口的任务
        Task task=new Task();
        //将实现了callble接口的任务放到Future里面
        //FutureTask<T> T是实现了Callable<T>接口的call() 方法的返回值T
        FutureTask<String> futureTask=new FutureTask<String>(task);
        //将future放到线程thread里面
        Thread t=new Thread(futureTask);
        //启动线程
        t.start();
        System.out.println("线程启动");
        //获取线程执行之后结果输出
        System.out.println("future任务结束，输出:"+futureTask.get());

    }

}

```

***输出如下***
> futrue任务开始  
线程启动  
execute!!!  
future任务结束，输出:complete  

可以看到通过`FutureTask.get()`获取到了线程里面call方法的返回值
***
因为计算因数分解的任务耗时长，所以我们可以在map中缓存这个任务，当新的线程进来，看到map中已经有这个任务了，则等待这个任务结算，而不是继续自己开始一个计算任务。

具体实现：
`    private  Map<Long, Future<List<Long>>> cache = new ConcurrentHashMap<Long, Future<List<Long>>>();
`

***优化之后的代码如下***

1.首先实现callble接口的task，这部分负责因数分解的任务核心计算
```
public class factorsTask implements Callable<List<Long>> {
    private Long i = 0L; //要进行分解的数

    public factorsTask(Long i) {
        this.i = i;
    }

    @Override
    public List<Long> call() throws Exception {
        List<Long> factors = new ArrayList<Long>();
        Long j = 2L;
        while (j <= i) {
            if (i % j == 0) {
                i = i / j;
                factors.add(j);
            } else {
                j++;
            }
        }
        return  factors;
    }
}


```
2.调用callble任务的service

```
public class factorsService {

    private   Map<Long, Future<List<Long>>> cache = new ConcurrentHashMap<Long, Future<List<Long>>>();
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
        
            if (cache.get(i) == null) {
            //如果缓存为空，则建立一个新的计算任务，并放到缓存中
            //然后启动新的线程去进行计算
                factorsTask factorsTask = new factorsTask(i);
                FutureTask<List<Long>> future = new FutureTask<List<Long>>(factorsTask);
                Thread t1 = new Thread(future);
                t1.start();
                System.out.println("放入到缓存中");
                cache.put(start, future);
            }
       
       //从缓存中取出该任务
        Future<List<Long>> futureR = cache.get(i);
        try {
        //获取该任务的结果
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
            System.out.println();
            System.out.println("验证：分解出来因数相乘结果为:" + var);
            long endtime = System.currentTimeMillis();
            System.out.println("此线程耗时" + (endtime - begintime) + "毫秒");
        }

        return factors;
    }
}

```
3.启动service的thread
```
public class factorsThread implements Runnable {
    private CountDownLatch latch;

    public factorsThread(CountDownLatch latch) {
        this.latch = latch; //初始化闭锁
    }


    demo2.demo1Modify.factorsService factorsService;

    public void setFactorsService(factorsService factorsService) {
        this.factorsService = factorsService;
    }


    Long num = 0L;

    public void setNum(Long num) {
        this.num = num;
    }


    @Override
    public void run() {
        factorsService.Service(num);
        latch.countDown();  //执行完run后减1
    }
}

```

4.启动thread的main
```
    public static void main(String[] args) {
            factorsService factorsService = new factorsService();

            List<Long> nums = Arrays.asList(1323999999999L, 1323999999999L, 1323999999999L, 1323999999999L, 1323999999999L,13239999999979L,13239999999979L); //51323999999999L, 51323999999999L, 51323999999998L);

            final CountDownLatch latch = new CountDownLatch(7);
            factorsThread factorsThread = new factorsThread(latch);
            factorsThread.setFactorsService(factorsService);


            System.out.println("多线程的方式启动计算：");
            Long beginT = System.currentTimeMillis();
            for (Long num : nums) {
                factorsThread.setNum(num);
                Thread thread = new Thread(factorsThread);
                thread.start();
            }

            try {
                //多线程运行结束前一直等待
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Long endT = System.currentTimeMillis();
            System.out.println("多线程任务计算完成!耗时共" + (endT - beginT) + "毫秒");

            //多线程执行完成 5个任务 无缓存 10299毫秒

        }
```

***运行结果***
> 多线程的方式启动计算：  
放入到缓存中  
放入到缓存中  
放入到缓存中  
放入到缓存中  
放入到缓存中  
放入到缓存中  
放入到缓存中  
从缓存中取值  
13239999999979的因数分解为: 139X2909X32743829    此线程耗时1871毫秒  
从缓存中取值  
13239999999979的因数分解为: 139X2909X32743829    此线程耗时1875毫秒  
从缓存中取值  
13239999999979的因数分解为: 139X2909X32743829    此线程耗时1888毫秒  
从缓存中取值  
1323999999999的因数分解为: 3X3X3X131X374328527    此线程耗时7834毫秒  
从缓存中取值  
1323999999999的因数分解为: 3X3X3X131X374328527    此线程耗时7841毫秒  
从缓存中取值  
1323999999999的因数分解为: 3X3X3X131X374328527  此线程耗时7841毫秒  
此线程从缓存中取值  
1323999999999的因数分解为: 3X3X3X131X374328527  此线程耗时7849毫秒  
多线程任务计算完成!共耗时7852毫秒

从上面我们可以看到，对13239999999979和1323999999999进行因数分解，两个都有重复的，结果仍然没有走缓存。
理想的情况下，每个数字是只放入一次缓存。
这里这都放了。总共耗时7852秒

问题出在
```
 if (cache.get(i) == null) { 
            //如果缓存为空，则建立一个新的计算任务，并放到缓存中
            //然后启动新的线程去进行计算
                factorsTask factorsTask = new factorsTask(i);
                FutureTask<List<Long>> future = new FutureTask<List<Long>>(factorsTask);
                Thread t1 = new Thread(future);
                t1.start();
                System.out.println("放入到缓存中");
                cache.put(start, future);
            }

```

`(cache.get(i) == null`这里虽然是ConcurrentHashMap，get操作是原子的，一次只能一个线程进去，但是后面的操作，仍然是需要耗费时间的，当线程切换的时候，新的任务还没被放入到cache里面

***解决方法1 synchronized同步***  
代码如下
```
 synchronized (this) { 
            if (cache.get(i) == null) {
                factorsTask factorsTask = new factorsTask(i);
                FutureTask<List<Long>> future = new FutureTask<List<Long>>(factorsTask);
                Thread t1 = new Thread(future);
                t1.start();
                System.out.println("放入到缓存中");
                cache.put(start, future);
            }
        }
```

***输出结果***
>多线程的方式启动计算：  
放入到缓存中  
放入到缓存中  
从缓存中取值  
13239999999979的因数分解为: 139X2909X32743829    耗时1042毫秒  
从缓存中取值  
从缓存中取值  
13239999999979的因数分解为: 139X2909X32743829  此线程耗时1047毫秒  
13239999999979的因数分解为: 139X2909X32743829  此线程耗时1048毫秒  
从缓存中取值  
从缓存中取值  
从缓存中取值  
1323999999999的因数分解为:  
从缓存中取值  
3X3X3X131X374328527  耗时4964毫秒
1323999999999的因数分解为: 3X3X3X131X374328527  此线程耗时4964毫秒
1323999999999的因数分解为: 3X3X3X131X374328527  此线程耗时4962毫秒
1323999999999的因数分解为: 3X3X3X131X374328527  此线程耗时4963毫秒  
多线程任务计算完成!耗时共4965毫秒  



效果很显然，耗时4965秒 ,每个数字只有一次放入缓存，并且存在多种不同的数字的时候也很效 ，（不同于demo1的串行，如果是多个不同的数字时候就成了串行）


***解决方法2 使用putIfAbsent*** 
> `putIfAbsent()` 是`ConcurrentHashMap`的方法，是一个原子操作，判断是否为空，为空则放入值，并返回null ，这样就消除了上面的竞态条件

```
//---方案二--使用putIfAbsent
        factorsTask factorsFuture = new factorsTask(i);
        FutureTask<List<Long>> future = new FutureTask<List<Long>>(factorsFuture);
        if (cache.putIfAbsent(i,future) == null) {
            Thread t1 = new Thread(future);
            t1.start();
            System.out.println("放入到缓存中");
        }

```

***运行结果***
> 多线程的方式启动计算：  
放入到缓存中  
放入到缓存中  
从缓存中取值  
从缓存中取值  
从缓存中取值  
13239999999979的因数分解为: 139X2909X32743829  此线程耗时928毫秒  
13239999999979的因数分解为: 139X2909X32743829  此线程耗时929毫秒  
13239999999979的因数分解为: 139X2909X32743829  此线程耗时928毫秒  
从缓存中取值  
从缓存中取值  
从缓存中取值  
从缓存中取值  
1323999999999的因数分解为: 3X3X3X131X374328527  此线程耗时5390毫秒  
1323999999999的因数分解为: 3X3X3X131X374328527  此线程耗时5393毫秒  
1323999999999的因数分解为: 3X3X3X131X374328527  此线程耗时5393毫秒  
1323999999999的因数分解为: 3X3X3X131X374328527  此线程耗时5392毫秒  
多线程任务计算完成!耗时共5393毫秒


耗时和方案1差不多(些许时间差别与当前的系统情况有关)，然后也是每个数字只走了一次缓存


多线程初级入门大概如上，实现一个线程有三种方式 实现runable接口，callable接口，以及继承Thread类。




`CyclicBarrier`是栅栏，效果就是让多个线程都执行到某个指定的点之后，再一起继续执行。与`CountDownLatch`有点类似，最大的区别是`CyclicBarrier`可以循环使用。

这里举例两个场景，一个是斗地主，需要三个玩家都入场之后才可以开始。 另一个是赛马，多匹马一起跑，最先到达终点的算赢。这里用线程分别表示玩家和马。

三人斗地主代码如下：
（需要三个玩家入场之后才能发牌）
```
public class Player extends Thread {
    
    private static int count = 1;
    private final int id = count++;
    private CountDownLatch latch;
    
    public Player(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println("【玩家" + id + "】已入场");
        latch.countDown();//计数器减1
    }
    
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3); //初始化计数器
        System.out.println("牌局开始, 等待玩家入场...");
        new Player(latch).start();//启动玩家线程
        new Player(latch).start();////启动玩家线程
        new Player(latch).start();////启动玩家线程
        latch.await();//阻塞等待计数器为0
        System.out.println("玩家已到齐, 开始发牌...");
    }
    
}
```

结果如下：
> 牌局开始, 等待玩家入场...  
【玩家2】已入场  
【玩家1】已入场  
【玩家3】已入场  
玩家已到齐, 开始发牌...


如果不使用CountDownLatch，我们直接注释掉` //latch.await();`  看结果
> 牌局开始, 等待玩家入场...  
【玩家1】已入场  
【玩家2】已入场  
玩家已到齐, 开始发牌...  
【玩家3】已入场  

三个玩家没凑齐就发牌了。

原理：CountDownLatch  
构造函数中创建了Sync这个内部类的对象
```
    public CountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }
```

内部类Sync继承了`AbstractQueuedSynchronizer` 简称AQS，AQS可以理解成是java里面用java代码实现的Synchronize，而不是由jvm去实现的。在AQS里面维护了一个state ，以及一个同步队列和多个条件队列
想要锁但是没获取到锁的线程，都在同步队列里面等着，获取到锁但是没有满足执行条件的，都在条件队列里面等着。

调用latch.await();的线程，被加入到同步队列里面等着，直到计数器为0释放锁。

>它的内部提供了一个计数器，在构造闭锁时必须指定计数器的初始值，且计数器的初始值必须大于0。另外它还提供了一个countDown方法来操作计数器的值，每调用一次countDown方法计数器都会减1，直到计数器的值减为0时就代表条件已成熟，所有因调用await方法而阻塞的线程都会被唤醒。这就是CountDownLatch的内部机制  
CountDownLatch只有一个带参构造器，必须传入一个大于0的值作为计数器初始值，否则会报错。可以看到在构造方法中只是去new了一个Sync对象并赋值给成员变量sync。和其他同步工具类一样，CountDownLatch的实现依赖于AQS，它是AQS共享模式下的一个应用。CountDownLatch实现了一个内部类Sync并用它去继承AQS，这样就能使用AQS提供的大部分方法了。下面我们就来看一下Sync内部类的代码。
```
//同步器
private static final class Sync extends AbstractQueuedSynchronizer {

    //构造器
    Sync(int count) {
        setState(count);
    }

    //获取当前同步状态
    int getCount() {
        return getState();
    }

    //尝试获取锁
    //返回负数：表示当前线程获取失败
    //返回零值：表示当前线程获取成功, 但是后继线程不能再获取了
    //返回正数：表示当前线程获取成功, 并且后继线程同样可以获取成功
    protected int tryAcquireShared(int acquires) {
        return (getState() == 0) ? 1 : -1;
    }

    //尝试释放锁
    protected boolean tryReleaseShared(int releases) {
        for (;;) {
            //获取同步状态
            int c = getState();
            //如果同步状态为0, 则不能再释放了
            if (c == 0) {
                return false;
            }
            //否则的话就将同步状态减1
            int nextc = c-1;
            //使用CAS方式更新同步状态
            if (compareAndSetState(c, nextc)) {
                return nextc == 0;
            }
        }
    }
}
```






赛马游戏的代码如下:

马
`
public class Horse implements Runnable {

    private static int counter = 0;
    private final int id = counter++;
    private int strides = 0;
    private static Random rand = new Random(47);
    private static CyclicBarrier barrier;

    public Horse(CyclicBarrier CyclicBarrier) {
        this.barrier = CyclicBarrier;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    //赛马每次随机跑几步
                    strides += rand.nextInt(3);
                }
                barrier.await();//线程在此进行等待
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String tracks() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < getStrides(); i++) {
            s.append("*");
        }
        s.append(id);
        return s.toString();
    }

    public synchronized int getStrides() {
        return strides;
    }

    public String toString() {
        return "Horse " + id + " ";
    }

}

赛马过程
```
public class HorseRace implements Runnable {

    private static final int FINISH_LINE = 75;
    private static List<Horse> horses = new ArrayList<Horse>();
    private static ExecutorService exec = Executors.newCachedThreadPool();

    @Override
    public void run() {
        StringBuilder s = new StringBuilder();
        //打印赛道边界
        for(int i = 0; i < FINISH_LINE; i++) {
            s.append("=");
        }
        System.out.println(s);
        //打印赛马轨迹
        for(Horse horse : horses) {
            System.out.println(horse.tracks());
        }
        //判断是否结束
        for(Horse horse : horses) {
            if(horse.getStrides() >= FINISH_LINE) {
                System.out.println(horse + "won!");
                exec.shutdownNow();
                return;
            }
        }
        //休息指定时间再到下一轮
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch(InterruptedException e) {
            System.out.println("barrier-action sleep interrupted");
        }
    }

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(7, new HorseRace()); //需要到达的线程数，以及线程到达之后需要执行的线程
        for(int i = 0; i < 7; i++) {
            Horse horse = new Horse(barrier);
            horses.add(horse);
            exec.execute(horse);
        }
    }

}
```

执行结果
> 如下  
===============  
**0  
**1  
*2  
**3  
*4  
**5  
*6  
。。。。。省略n个  
===============  
***********0  
************1  
********2  
***********3  
**********4  
***************5  
************6  
Horse 5 won!

执行`barrier.await();`的线程进行等待，直到所有计数器到达设置的值，然后继续执行下一轮。


