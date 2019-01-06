# concurrent_learn
#java并发练习笔记


## java并发编程实战之从因数分解看多线程
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



## java并发编程实战从因数分解看多线程（下）
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


### Furture
> 在并发编程中，我们经常用到非阻塞的模型，在之前的多线程的三种实现中，不管是继承thread类还是实现runnable接口，都无法保证获取到之前的执行结果。通过实现Callback接口，并用Future可以来接收多线程的执行结果。
 Future表示一个可能还没有完成的异步任务的结果，针对这个结果可以添加Callback以便在任务执行成功或失败后作出相应的操作。
 
 
#### Furture的demo
 
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



## 从赛马游戏看CyclicBarrier，从斗地主看CountDownLatch

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

## 为什么要用线程池
1.为什么要用线程池  
多线程的情况下确实可以最大限度发挥多核处理器的计算能力，提高系统的吞吐量和性能。但是如果随意使用线程，对系统的性能反而有不利影响。  

比如说
```
 new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
```

这样直接创建线程，在简单应用里面看起来没有问题，创建了一个线程，并且在run()方法结束后自动回收该线程。但是如果在真实系统里面，可能会由于业务情况，开启了很多线程，当线程数量多大时，反而会耗尽cpu和内存资源。  

比如说，创建和销毁线程也需要时间，如果创建和销毁的时间远大于线程执行的时间，反而得不偿失。  
其次线程也需要占用内存空间，大量的线程会抢占宝贵的内存资源，可能会导致out of memory异常。
且大量的线程回收也会给GC带来很大的压力，延长GC的停顿时间。

比如说  开启10000000个线程的时候
```
 for (int i = 0; i <10000000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
```
>Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
	at java.lang.Thread.start0(Native Method)
	at java.lang.Thread.start(Thread.java:717)
	at demo4.ThreadDemo.main(ThreadDemo.java:17)


所以千万要警惕这种情况的发生  


最后，大量的线程也会抢占cpu的资源，cpu不停的在各个线程上下文切换中,反而没有时间去处理线程运行的时候该处理的任务。 


因此，为了避免频繁的创建和销毁线程，让创建的线程进行复用，就有了线程池的概念。  
线程池里会维护一部分活跃线程，如果有需要，就去线程池里取线程使用，用完即归还到线程池里，免去了创建和销毁线程的开销，且线程池也会线程的数量有一定的限制。

比如说刚刚的代码，通过使用线程池来构建线程
```  
ExecutorService executorService= Executors.newFixedThreadPool(10);
        for (int i = 0; i <10000000; i++) {
           Thread t= new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("启动线程");
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            executorService.submit(t);
        }

    }
```

可以看到，将不会再产生out of memory的错误，因为每次将只有10个线程在启用，其余的放在线程池内的任务队列里面。
这是使用线程池的好处之一。

但是线程池也分很多种，要视情况使用，newFixedThreadPool内部的任务队列是无界队列，如果放到的任务过多，最终也会导致内存不足。


## jdk的线程池框架分析

jdk提供了一套Executor框架，其本质就是线程池。  
首先是
```
public interface Executor {
//执行一个实现了Runnable接口的任务
    void execute(Runnable command);

```

Executor接口是所有线程池的父接口

然后 ExecutorService 继承了Executor接口，初步定义了线程池内的方法

```
public interface ExecutorService extends Executor {

//关闭线程池，将不会再接收新任务，待所有旧任务完结后就关闭
    void shutdown();
    
   //关闭线程池，不会等待正在执行的任务完成，而是中断全部，并且返回未完成任务的list
    List<Runnable> shutdownNow();

   //判断线程池是否关闭，如果关闭 返回true
    boolean isShutdown();
    //如果在关闭之前，所有任务都已经完结 返回true
    boolean isTerminated();
    
    //等待所有的任务在关闭之前完成，参数为等待的时间,如果完成则返回true
    boolean awaitTermination(long timeout, TimeUnit unit)
        throws InterruptedException;

    
    //核心方法之一，放入一个callable包装的任务，然后立即返回一个future，是异步的任务
    <T> Future<T> submit(Callable<T> task);

     //核心方法之一，放入一个Runnable包装的任务，立即返回一个future，T为结果类型
    <T> Future<T> submit(Runnable task, T result);

  
  //核心方法之一 ，放入runable任务，返回一个future 类型不确定
    Future<?> submit(Runnable task);

//放入一个任务集合，返回一个future集合
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        throws InterruptedException;

  
  //放入任务集合和超时时间和时间单位，返回时间到了之后，已经完成的任务列表
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                  long timeout, TimeUnit unit)
        throws InterruptedException;

    //放入一个callable集合，返回一个已经完成的结果
    <T> T invokeAny(Collection<? extends Callable<T>> tasks)
        throws InterruptedException, ExecutionException;

//放入一个callable集合 在超时前返回一个已经完成的结果
    <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                    long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}

```


然后 ExecutorService 继承了Executor接口，初步定义了线程池内的方法。然后 ，AbstractExecutorService又实现了ExecutorService里面定义的方法。


再最后ThreadPoolExecutor 继承了AbstractExecutorService，里面是更为细致的实现。

最后的最后，jdk定义了一个Executors工厂类，里面对ThreadPoolExecutor进行封装。
让我们用可以Executors取出来newFixedThreadPool（），newWorkStealingPool（），newSingleThreadExecutor（），newCachedThreadPool（），ScheduledExecutorService（） 等等不同特点的线程池。


## jdk提供的线程池介绍
Executor框架提供了各种类型的线程池。    
ExecutorService 继承了Executor接口，初步定义了线程池内的方法。然后 ，AbstractExecutorService又实现了ExecutorService里面定义的方法。

再最后ThreadPoolExecutor 继承了AbstractExecutorService，里面是更为细致的实现。

jdk定义了一个Executors工厂类，里面对ThreadPoolExecutor进行封装。 让我们用可以Executors取出来newFixedThreadPool（），newWorkStealingPool（），newSingleThreadExecutor（），newCachedThreadPool（），ScheduledExecutorService（） 等等不同特点的线程池。  

因此 ExecutorService=new ThreadPoolExecutor(...);
就可以创建出自己定制的线程池。   
Executors内部也是这样定制的。  
主要实现如下

```
public class Executors {
    
    /*
    newFixedThreadPool 固定线程线程数量的线程池，该线程池内的线程数量始终不变，  
    如果任务到来，内部有空闲线程，则立即执行，如果没有或任务数量大于线程数，多出来的任务，  
    则会被暂存到任务队列中，待线程空闲，按先入先出的顺序处理。
    该任务队列是LinkedBlockingQueue，是无界队列，如果任务数量特别多，可能会导致内存不足
    */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
    
    
    
    /*
    newSingleThreadExecutor(),该方法返回一个只有一个线程的线程池，多出来的任务会被放到任务队列内，  
    待线程空闲，按先入先出的顺序执行队列中的任务。队列也是无界队列
    */
     public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }
    
    
    /*
    newCachedThreadPool() ,该方法返回一个可以根据实际情况调整线程数量的线程池，线程数量不确定，初始是0，最大线程数是Integer.MAX_VALUE，如果有新的空闲线程可以复用，则会优先使用可复用的线程，如果所有线程都在工作，则创建新的线程，所有线程工作结束后，会返回线程池进行复用。但是如果超过60秒空闲，该线程会被销毁。
    
    该线程池慎用，在无限制的新增线程的场景下，很可能造成系统内存溢出
    */
     public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }
    
    
    
    /*
    newSingleThreadScheduledExecutor（） 该方法返回一个线程池大小为1，拓展了在给定时间执行某任务的功能，如在某个固定的延时之后执行，或者周期性执行某任务
    */
      public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
        return new DelegatedScheduledExecutorService
            (new ScheduledThreadPoolExecutor(1));
    }
    
    
    /*
    返回同上，但是可以自己定义线程池大小
    */
     public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return new ScheduledThreadPoolExecutor(corePoolSize);
    }

}

```  

比较特别是newScheduledThreadPool ，这个是个计划任务，会在指定的时间，对任务进行调度。

内部有两个方法。
```
 public ScheduledFuture<?> scheduleAtFixedRate(...)   
 和
 public ScheduledFuture<?> scheduleWithFixedDelay(...) 

第一个方法设置的定时任务，是从每个任务开始的时间+间隔 执行

第二个方法是 从每个任务结束的时间+间隔 执行


```


示例：
```
 ScheduledExecutorService executorService= Executors.newScheduledThreadPool(10);

        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //打印当前系统秒数
                System.out.println(System.currentTimeMillis()/1000);
            }
        },0,5,TimeUnit.SECONDS);


    }


```
输出如下:
>1546681976  
1546681981  
1546681986  
1546681991  
1546681996  

可以看到时间间隔是5秒。
但是请注意！如果任务执行时间 大于调度时间 
比如说
```
 ScheduledExecutorService executorService= Executors.newScheduledThreadPool(10);

        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //打印当前系统秒数
                try {
                //休眠3秒
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis()/1000);
            }
        },0,2,TimeUnit.SECONDS);


    }

```

输出结果：
>1546682107  
1546682110    
1546682113  
1546682116  
1546682119


这里任务的时间是3秒，但是调度时间间隔是2秒，结果就会变成定时任务3秒，也就是当任务时间大于间隔的时候，会在任务结束之后 立即调用下一次任务。并不会出现任务堆叠出现的情况。


如果使用的是scheduleWithFixedDelay(...) 方法，则是从任务结束开始调度，那么时间将变成5秒间隔。




## 线程池各个参数详解

一：为什么要自己定义线程池  

虽然jdk提供了几种常用特性的线程池给我们，但是很多时候，我还是需要自己去自定义自己需要特征的线程池。并且阿里巴巴规范手册里面，就是不建议使用jdk的线程池，而是建议程序员手动创建线程池。  
为什么呢？原文如下  
【强制】线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式，**这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险**
说明：Executors 返回的线程池对象的弊端如下： 
 
1、FixedThreadPool 和 SingleThreadPool:  
    允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。 
 
2、CachedThreadPool 和 ScheduledThreadPool:  
    允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM


但是除此之外，自己通过ThreadPoolExecutor定义线程池还有很多好处。
比如说，  
1.自己根据需求定义自己的拒绝策略，如果线程过多，任务过多 如何处理。  
2.补充完善的线程信息，比如线程名，这一点在将来如果出现线上bug的时候，你会感谢自己，因为你绝不想在线上看到什么threa-1  threa-2 等这种线程爆出的错误，而且是看到自己 “处理xxx线程 错误”，可以一眼看到  
3.可以通过ThreadPoolExecutor的beforExecute(),
afterExecute()和terminated()方法去拓展对线程池运行前，运行后，结束后等不同阶段的控制。比如说通过拓展打印日志输出一些有用的调试信息。在故障诊断是非常有用的。  
4.可以通过自定义线程创建，可以自定义线程名称，组，优先级等信息，包括设置为守护线程等，根据需求。

二： 线程池详解  


之前我们在Executors工厂里面看到的方法，

```
 /*
    newFixedThreadPool 固定线程线程数量的线程池，该线程池内的线程数量始终不变，  
    如果任务到来，内部有空闲线程，则立即执行，如果没有或任务数量大于线程数，多出来的任务，  
    则会被暂存到任务队列中，待线程空闲，按先入先出的顺序处理。
    该任务队列是LinkedBlockingQueue，是无界队列，如果任务数量特别多，可能会导致内存不足
    */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
    
    
    
    /*
    newSingleThreadExecutor(),该方法返回一个只有一个线程的线程池，多出来的任务会被放到任务队列内，  
    待线程空闲，按先入先出的顺序执行队列中的任务。队列也是无界队列
    */
     public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }
    
```
 
可以看到，本质都是对ThreadPoolExecutor进行封装。  
那么强大的ThreadPoolExecutor又是如何使用？
先看构造方法定义
```
      public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) 

```
1.corePoolSize 指定了线程池里的线程数量  
2.maximumPoolSize 指定了线程池里的最大线程数量  
3.keepAliveTime 当线程池线程数量大于corePoolSize时候，多出来的空闲线程，多长时间会被销毁。  
4.unit 时间单位  
5.workQueue 任务队列，用于存放提交但是尚未被执行的任务。   
6.threadFactory 线程工厂，用于创建线程，一般可以用默认的  
7.handler 拒绝策略，当任务过多时候，如何拒绝任务。  

主要是workQueue和handler的差异比较大  

workQueue指被提交但未执行的任务队列，它是一个BlockingQueue接口的对象，仅用于存放Runnable对象。  
ThreadPoolExecutor的构造函数中，可使用以下几种BlockingQueue  

1.直接提交队列： 即SynchronousQueue ,这是一个比较特殊的BlockKingQueue， SynchronousQueue没有容量，每一个插入操作都要等待对应的删除操作，反之 一个删除操作都要等待对应的插入操作。 也就是如果使用SynchronousQueue，提交的任务不会被真实保存，而是将新任务交给空闲线程执行，如果没有空闲线程，则创建线程，如果线程数都已经大于最大线程数，则执行拒绝策略。使用这种队列，需要将maximumPoolSize设置的非常大，不然容易执行拒绝策略。比如说

没有最大线程数限制的newCachedThreadPool()
```
 public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }
```
但是这个在大量任务的时候，会启用等量的线程去处理，有风险造成系统资源不足。

2.有界任务队列。 有界任务队列可以使用ArrayBlockingQueue实现。需要给一个容量参数表示该队列的最大值。当有新任务进来时，如果当前线程数小于corePoolSize，则会创建新线程执行任务。如果大于，则会将任务放到任务队列中，如果任务队列满了，在当前线程小于将maximumPoolSize的情况下，将会创建新线程，如果大于maximumPoolSize，则执行拒绝策略。
也就是，一阶段，当线程数小于coresize的时候，创建线程；二阶段，当线程任务数大于coresize的时候，放入到队列中；三阶段，队列满，但是还没大于maxsize的时候，创建新线程。 四阶段，队列满，线程数也大于了maxsize, 则执行拒绝策略。  
可以发现，有界任务队列，会大概率将任务保持在coresize上，只有队列满了，也就是任务非常繁忙的时候，会到达maxsie。

3.无界任务队列。  
使用linkedBlockingQueue实现，队列最大长度限制为integer.MAX。无界任务队列，不存在任务入队失败的情况， 当任务过来时候，如果线程数小于coresize ，则创建线程，如果大于，则放入到任务队列里面。也就是，线程数几乎会一直维持在coresize大小。FixedThreadPool和singleThreadPool即是如此。 风险在于，如果任务队列里面任务堆积过多，可能导致内存不足。
4.优先级任务队列。使用PrioriBlockingQueue ，特殊的无界队列，和普通的先进先出队列不同，它是优先级高的先出。   

因此 ，自定义线程池的时候，应该根据实际需要，选择合适的任务队列应对不同的场景。  



这里给出ThreadPool的核心调度代码
```
//runnable为线程内的任务
 public void execute(Runnable command) {
 //为null则抛出异常
        if (command == null)
            throw new NullPointerException();
       
       //获取工作的线程数量
        int c = ctl.get();
        //数量小于coresize
        if (workerCountOf(c) < corePoolSize) {
        //addWorker 直接执行新线程
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
        
        //如果大于corsize 则放到等待队列中
        //workQueue.offer()表示放到队列中
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            if (! isRunning(recheck) && remove(command))
                reject(command);
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
        //如果放到队列中失败，直接提交给线程池执行
        //如果提交失败，则执行reject() 拒绝策略
        else if (!addWorker(command, false))
            reject(command);
    }
```

jdk内置的拒绝策略如下：

拒绝策略  
1.AbortPolicy  该策略会直接抛出异常，阻止系统正常工作。
2.CallerRunsPolic 策略：只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务，这样做不会真正的丢弃任务，但是 任务提交线程的性能可能会急剧下降。  
3.DisCardOledestPolicy 策略： 该策略默默地丢弃无法处理的任务，不予任务处理，如果允许任务丢失，这是最好的方法了。  

jdk内置的几种线程池，主要采用的是AbortPolicy 策略，会直接抛出异常,defaultHandler如下。

```
private static final RejectedExecutionHandler defaultHandler =
        new AbortPolicy();

```


以上内置的策略均实现了RejectedExecutionHandler接口，因此我们也可以实现这个接口，自定义我们自己的策略。

```
 public static class AbortPolicy implements RejectedExecutionHandler {
      
        public AbortPolicy() { }


        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() +
                                                 " rejected from " +
                                                 e.toString());
        }
    }
```



最后的最后，所有的线程池里面，线程是从哪里来的？
答案是ThreadFactory 
这个是一个接口，里面只有一个方法，用来创建线程

```
public interface ThreadFactory {
    Thread newThread(Runnable r);
}
```

线程池的默认ThreadFactory如下
```
  static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                                  Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                          poolNumber.getAndIncrement() +
                         "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                                  namePrefix + threadNumber.getAndIncrement(),
                                  0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }


```


同理我们也可以通过实现ThreadFactory来定义我们自己的线程工厂，比如说自定义线程名称，组，优先级等信息，可以跟着线程池究竟在何时创建了多少线程等等。






