package com.o18.redis.cache.operation;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * 简单的SimpleSessionOperations
 */
public class DefaultSimpleSessionOperations<V> extends DefaultSessionOperations implements SimpleSessionOperations<V> {

    public DefaultSimpleSessionOperations(RedisTemplate<String, V> redisTemplate, String serviceName) {
        super(redisTemplate, serviceName, "SIMPLE");
    }

    private BoundHashOperations<String, String, V> getBoundOperations() {
        return this.redisTemplate.boundHashOps(this.redisKey);
    }

    @Override
    public V get(String group, String item) {
        return this.getBoundOperations().get(String.format("%s_%s", group, item));
    }

    @Override
    public void put(String group, String item, V value) {
        this.getBoundOperations().put(String.format("%s_%s", group, item), value);
    }
}
