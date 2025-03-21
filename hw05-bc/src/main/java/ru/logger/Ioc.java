package ru.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    static MyClassInterface createMyClass() {
        MyClassInterface myClass = new MyClassImpl();
        if (!Arrays.stream(myClass.getClass().getDeclaredMethods()).anyMatch(m -> m.isAnnotationPresent(Log.class)))
            return myClass;
        InvocationHandler handler = new DemoInvocationHandler(myClass);
        return (MyClassInterface)
                Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {MyClassInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final Object myClass;

        private Map<Method, Boolean> mapMethodNeedLog = new HashMap<>();

        DemoInvocationHandler(Object myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //если в кеше нет метода, то добавляем
            if (!mapMethodNeedLog.containsKey(method)) {
                if (myClass.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Log.class)) {
                    mapMethodNeedLog.put(method, true);
                } else {
                    mapMethodNeedLog.put(method, false);
                }
            }

            if (mapMethodNeedLog.get(method)) {
                logger.info("execute method: {}, param: {}", method.getName(), args);
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
