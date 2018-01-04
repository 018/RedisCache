package com.o18.redis.cache.operation;

import org.springframework.data.redis.core.*;

/**
 * DefaultSessionOperations
 */
public abstract class DefaultSessionOperations<V> {

    protected RedisTemplate<String, V> redisTemplate;
    protected String serviceName;
    protected String key;
    protected String redisKey;

    public DefaultSessionOperations(RedisTemplate<String, V> redisTemplate, String serviceName, String key) {
        this.redisTemplate = redisTemplate;
        this.serviceName = serviceName;
        this.key = key;

        this.redisKey = String.format("%s_%s", this.serviceName, this.key);
    }
}
