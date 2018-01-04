package com.o18.redis.cache.operation;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 简单的ValueSessionOperations
 */
public class DefaultValueSessionOperations extends DefaultSessionOperations implements ValueSessionOperations {

    public DefaultValueSessionOperations(RedisTemplate<String, Object> redisTemplate, String serviceName, String key) {
        super(redisTemplate, serviceName, key);
    }

    @Override
    public RedisOperations getOperations() {
        return null;
    }

    @Override
    public void set(Object value) {

    }

    @Override
    public void set(Object value, long offset) {

    }

    @Override
    public void set(Object value, long timeout, TimeUnit unit) {

    }

    @Override
    public Boolean setIfAbsent(Object value) {
        return null;
    }

    @Override
    public Object get() {
        return null;
    }

    @Override
    public String get(long start, long end) {
        return null;
    }

    @Override
    public Object getAndSet(Object value) {
        return null;
    }

    @Override
    public Long increment(long delta) {
        return null;
    }

    @Override
    public Double increment(double delta) {
        return null;
    }

    @Override
    public Integer append(String value) {
        return null;
    }

    @Override
    public Long size() {
        return null;
    }

    @Override
    public Object getKey() {
        return null;
    }

    @Override
    public DataType getType() {
        return null;
    }

    @Override
    public Long getExpire() {
        return null;
    }

    @Override
    public Boolean expire(long timeout, TimeUnit unit) {
        return null;
    }

    @Override
    public Boolean expireAt(Date date) {
        return null;
    }

    @Override
    public Boolean persist() {
        return null;
    }

    @Override
    public void rename(Object newKey) {

    }
}
