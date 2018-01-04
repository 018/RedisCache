package com.o18.redis.cache;

import com.o18.redis.cache.operation.*;
import com.o18.redis.cache.proxy.RedisSessionOperationInvocationHandler;
import com.o18.redis.cache.proxy.SimpleSessionOperationInvocationHandler;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationHandler;

/**
 * 操作工厂类
 */
public class SessionOperationsFactory {

    RedisTemplate<String, Object> redisTemplate;
    private String serviceName;

    public SessionOperationsFactory(RedisTemplate<String, Object> redisTemplate, String serviceName) {
        Assert.notNull(redisTemplate, "Property 'redisTemplate' is required");
        Assert.notNull(serviceName, "Property 'serviceName' is required");

        this.redisTemplate = redisTemplate;
        this.serviceName = serviceName;
    }

    /**
     * 创建动态代理执行对象
     * @param targetInterface 客户端的接口
     * @return
     */
    public InvocationHandler create(Class<?> targetInterface) {
        if (HashSessionOperations.class.isAssignableFrom(targetInterface)) {
            return new RedisSessionOperationInvocationHandler(new DefaultHashSessionOperations(this.redisTemplate, this.serviceName, targetInterface.getSimpleName()));
        } else if (ListSessionOperations.class.isAssignableFrom(targetInterface)) {
            return new RedisSessionOperationInvocationHandler(new DefaultListSessionOperations(this.redisTemplate, this.serviceName, targetInterface.getSimpleName()));
        } else if (SetSessionOperations.class.isAssignableFrom(targetInterface)) {
            return new RedisSessionOperationInvocationHandler(new DefaultSetSessionOperations(this.redisTemplate, this.serviceName, targetInterface.getSimpleName()));
        } else if (ValueSessionOperations.class.isAssignableFrom(targetInterface)) {
            return new RedisSessionOperationInvocationHandler(new DefaultValueSessionOperations(this.redisTemplate, this.serviceName, targetInterface.getSimpleName()));
        } else if (ZSetSessionOperations.class.isAssignableFrom(targetInterface)) {
            return new RedisSessionOperationInvocationHandler(new DefaultZSetSessionOperations(this.redisTemplate, this.serviceName, targetInterface.getSimpleName()));
        } else {
            return new SimpleSessionOperationInvocationHandler(new DefaultSimpleSessionOperations(this.redisTemplate, this.serviceName));
        }
    }
}
