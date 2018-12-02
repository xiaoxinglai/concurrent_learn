package proxy.proxyDao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TestInvocationHandler  implements InvocationHandler{

    private Object o;

    public TestInvocationHandler(Object o) {
        this.o = o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("aaaaaa");
        return method.invoke(o);
    }
}
