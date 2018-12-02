package proxy.dao;

public class test {

    //代理 代码的增强
    // 静态代理  1.继承 ，代理对象继承目标对象，重写目标对象当中需要被代理的方法
    //代理对象是父类，子类是被代理对象
     // 2 聚合（通过接口实现）  a.代理对象和目标对象实现了同一个接口 b.代理对象包含了实现了同一个接口
    //优先用聚合
    //静态代理都必须实现子类，如果需要代理都类很多，就要写很多，
    //此时应该用动态代理


    //动态代理
    //jdk动态代理是基于接口的
    //本质上是自动生成了一个代理类，
    //重写了接口的代理方法，并接收了一个目标对象的引用，调用目标对象的方法前后执行代理逻辑
    //为什么jdk只能用接口而不能用继承，因为jdk底层源码 它生成的代理类已经继承了一个叫proxy的类
    //java是单继承的，，所以就只能用接口了

}
