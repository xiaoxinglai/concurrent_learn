package proxy.main;

import com.sun.org.apache.bcel.internal.generic.DADD;
import jdk.nashorn.internal.codegen.Label;
import proxy.dao.*;
import proxy.proxyDao.ProxyFactor;
import proxy.proxyDao.TestInvocationHandler;
import sun.misc.ProxyGenerator;

import java.awt.*;
import java.lang.reflect.Proxy;
import java.util.HashMap;

public class Proxymain {
    public static void main(String[] args) {
////        继承
//        IndexDao dao=new IndexLogDao();
//        dao.query();

        //接口
//        Dao dao=new IndexDaoImpl();
//        IndexDaoLogImpl DaoLog=new IndexDaoLogImpl(dao);
//        DaoLog.query();


        //动态代理
//        Dao targetObject = new IndexDao();
//        //获取到代理对象
//        Dao proxyObeject=(Dao)ProxyFactor.getProxy(targetObject);


//
//        //jdk动态代理
//        Dao dao=new IndexDaoImpl();
//        TestInvocationHandler invocationHandler=new TestInvocationHandler(dao);
//        Dao proxyDao=(Dao) Proxy.newProxyInstance(Dao.class.getClassLoader(),new Class[]{Dao.class},invocationHandler);
//        proxyDao.query();
//
//      //  ProxyGenerator.generateProxyClass()//生成代理类字节码的方法 里面应该是调用了一个native方法

        for (int j = 0; j <10; j++) {
            System.out.println("j"+j);
            for (int i = 0; i < 100; i++) {
                System.out.println(i);
                break ;
            }
        }



    }
}
