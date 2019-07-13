package proxy.jdkProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName jdkDynamicProxy
 * @Author laixiaoxing
 * @Date 2019/4/18 下午3:52
 * @Description jdk的动态代理
 * @Version 1.0
 * <p>
 * 静态代理：编译前就已经写好了代理类和代理代码 ，接口子类都是已知 代理类也是已知
 * <p>
 * 动态代理：编译期动态生成代理类，优点不是说省了那些写代理类的代码，而是在接口还未确定的时候，就已经确定了代理类的行为
 * 比如说代理类的行为是记录日志，而不会在意具体给哪个接口记日志,这个动态代理可以给任意接口记日志
 */
public class jdkDynamicProxy {

    /**
     * 接口类
     */
    interface IHello {
        void sayHello();
    }

    static class hello implements IHello {

        @Override
        public void sayHello() {
            System.out.println("hello world");
        }
    }


    /**
     * 动态代理类，实现InvocationHandler接口
     */
    static class DynamicProxy implements InvocationHandler {

        public DynamicProxy(Object orignalObject) {
            this.orignalObject = orignalObject;
        }

        private Object orignalObject;

        /**
         * 执行的代理方法
         *
         * @param proxy  代理对象
         * @param method 代理方法
         * @param args   代理参数
         * @return
         * @throws Throwable
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("代理类要执行的逻辑，比如说记录日志");

            //调用被代理的方法
            return method.invoke(orignalObject);
        }
    }

    public static void main(String[] args) {

        //被代理的接口
        IHello hello = new hello();
        //代理类的逻辑
        DynamicProxy dynamicProxy = new DynamicProxy(hello);
        //生成被代理类的子类 即代理类
        IHello proxyHello = (IHello) Proxy
                .newProxyInstance(IHello.class.getClassLoader(), new Class[]{IHello.class}, dynamicProxy);

        //调用代理类
        proxyHello.sayHello();

    }


}
