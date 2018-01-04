package com.o18.redis.cache.proxy;

import com.o18.redis.cache.exception.NonsupportMethodException;
import org.springframework.data.redis.core.BoundKeyOperations;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * redis操作动态代理执行类
 */
public class RedisSessionOperationInvocationHandler implements InvocationHandler {
    private static final String METHOD_TOSTRING = "toString";

    BoundKeyOperations<?> sessionOperations; // 具体执行的redis对象

    public RedisSessionOperationInvocationHandler(BoundKeyOperations<?> sessionOperations) {
        this.sessionOperations = sessionOperations;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> cls = method.getDeclaringClass();
        String methodName = method.getName();

        Class<?> targetCls = sessionOperations.getClass();
        Method methodTarget = targetCls.getDeclaredMethod(methodName, method.getParameterTypes());
        if (methodTarget == null) {
            throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                    "不支持" + methodName + "方法。");
        }

        if (METHOD_TOSTRING.equals(methodName)) {
            return cls.getName();
        }

        Object result = methodTarget.invoke(sessionOperations, args);
        return result;
    }
}
