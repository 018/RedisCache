package com.o18.redis.cache.proxy;

import com.o18.redis.cache.exception.NonsupportMethodException;
import com.o18.redis.cache.operation.DefaultSimpleSessionOperations;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 简单的InvocationHandler
 */
public class SimpleSessionOperationInvocationHandler implements InvocationHandler {
    private static final String METHOD_SET = "set";
    private static final String METHOD_GET = "get";
    private static final String METHOD_TOSTRING = "toString";
    private DefaultSimpleSessionOperations defaultSimpleSessionOperations;

    public SimpleSessionOperationInvocationHandler(DefaultSimpleSessionOperations defaultSimpleSessionOperations) {
        this.defaultSimpleSessionOperations = defaultSimpleSessionOperations;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> cls = method.getDeclaringClass();
        String group = cls.getSimpleName().replace("Settings", "").replace("Setting", "");
        String methodName = method.getName();
        String methodString = null;
        String item = null;
        Object value = null;

        if (METHOD_TOSTRING.equals(methodName)) {
            return cls.getName();
        } else if (METHOD_SET.equals(methodName)) {
            // void set(String item, ? value)
            if (method.getParameterCount() != 2 || !method.getParameterTypes()[0].getSimpleName().equals(String.class.getSimpleName()) ||
                    !method.getParameterTypes()[1].getSimpleName().equals(Object.class.getSimpleName()) ||
                    args == null || args.length != 2) {
                throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                        "方法声明错误，正确为 void set(String key, ? value)。");
            }

            methodString = METHOD_SET;
            item = args[0].toString();
            value = args[1];
        } else if (METHOD_GET.equals(methodName)) {
            // ? get(String item)
            if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].getSimpleName().equals(String.class.getSimpleName()) ||
                    args == null || args.length != 1) {
                throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                        "方法声明错误，正确为 ? get(String item)。");
            }

            methodString = METHOD_GET;
            item = args[0].toString();
        } else if (methodName.startsWith(METHOD_SET)) {
            // void setXXX(? value)
            if (method.getParameterCount() != 1 ||
                    args == null || args.length != 1) {
                throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                        "方法声明错误，正确为 void setXXX(? value)。");
            }

            methodString = METHOD_SET;
            item = methodName.substring(METHOD_SET.length());
            value = args[0];
        } else if (methodName.startsWith(METHOD_GET)) {
            // Object getXXX()
            if (method.getParameterCount() != 0 ||
                    (args != null && args.length != 0)) {
                throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                        "方法声明错误，正确为 Object getXXX()。");
            }

            methodString = METHOD_GET;
            item = methodName.substring(METHOD_GET.length());
        } else {
            throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                    "不支持的方法，只能是void set(String item, ? value)、? get(String item)、void setXXX(? value)、? getXXX()。");
        }

        switch (methodString) {
            case (METHOD_GET):
                Object val = this.defaultSimpleSessionOperations.get(group, item);
                return val;
            case (METHOD_SET):
                this.defaultSimpleSessionOperations.put(group, item, value);
        }
        return null;
    }
}
