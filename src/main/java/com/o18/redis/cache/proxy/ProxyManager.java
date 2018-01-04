package com.o18.redis.cache.proxy;

import com.o18.redis.cache.SessionOperationsFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 代理管理类
 */
public class ProxyManager {
    SessionOperationsFactory sessionOperationsFactory;

    public ProxyManager(RedisTemplate<String, Object> redisTemplate, String serviceName) {
        this.sessionOperationsFactory = new SessionOperationsFactory(redisTemplate, serviceName);
    }

    public ProxyManager(SessionOperationsFactory sessionOperationsFactory) {
        this.sessionOperationsFactory = sessionOperationsFactory;
    }

    /**
     * 创建代理实例
     *
     * @param interfaceCls
     * @return
     * @throws Throwable
     */
    public Object newProxyInstance(Class<?> interfaceCls) throws Throwable {
        InvocationHandler invocationHandler = sessionOperationsFactory.create(interfaceCls);
        Object object = Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class[]{interfaceCls}, invocationHandler);
        return object;
    }

    /**
     * 创建代理实例
     *
     * @param sessionOperationsFactory
     * @param interfaceCls
     * @return
     * @throws Throwable
     */
    public static Object newProxyInstance(SessionOperationsFactory sessionOperationsFactory, Class<?> interfaceCls) {
        InvocationHandler invocationHandler = sessionOperationsFactory.create(interfaceCls);
        Object object = Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class[]{interfaceCls}, invocationHandler);
        return object;
    }

    /**
     * 创建代理实例
     *
     * @param interfaceCls
     * @return
     * @throws Throwable
     */
    public static Object newProxyInstance(RedisTemplate<String, Object> redisTemplate, String serviceName, Class<?> interfaceCls) {
        SessionOperationsFactory sessionOperationsFactory = new SessionOperationsFactory(redisTemplate, serviceName);
        InvocationHandler invocationHandler = sessionOperationsFactory.create(interfaceCls);
        Object object = Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class[]{interfaceCls}, invocationHandler);
        return object;
    }
}
