package com.o18.redis.cache.operation;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 简单的ListSessionOperations
 */
public class DefaultListSessionOperations<V> extends DefaultSessionOperations implements ListSessionOperations<V> {

    public DefaultListSessionOperations(RedisTemplate<String, V> redisTemplate, String serviceName, String key) {
        super(redisTemplate, serviceName, key);
    }

    private BoundListOperations<String, V> getBoundOperations() {
        return this.redisTemplate.boundListOps(this.redisKey);
    }

    @Override
    public RedisOperations<String, V> getOperations() {
        return this.getBoundOperations().getOperations();
    }

    @Override
    public List<V> range(long start, long end) {
        return this.getBoundOperations().range(start, end);
    }

    @Override
    public void trim(long start, long end) {
        this.getBoundOperations().trim(start, end);
    }

    @Override
    public Long size() {
        return this.getBoundOperations().size();
    }

    @Override
    public Long leftPush(V value) {
        return this.getBoundOperations().leftPush(value);
    }

    @Override
    public Long leftPushAll(V... values) {
        return this.getBoundOperations().leftPushAll(values);
    }

    @Override
    public Long leftPushIfPresent(V value) {
        return this.getBoundOperations().leftPushIfPresent(value);
    }

    @Override
    public Long leftPush(V pivot, V value) {
        return this.getBoundOperations().leftPush(pivot, value);
    }

    @Override
    public Long rightPush(V value) {
        return this.getBoundOperations().rightPush(value);
    }

    @Override
    public Long rightPushAll(V... values) {
        return this.getBoundOperations().rightPushAll(values);
    }

    @Override
    public Long rightPushIfPresent(V value) {
        return this.getBoundOperations().rightPushIfPresent(value);
    }

    @Override
    public Long rightPush(V pivot, V value) {
        return this.getBoundOperations().rightPush(pivot, value);
    }

    @Override
    public V leftPop() {
        return this.getBoundOperations().leftPop();
    }

    @Override
    public V leftPop(long timeout, TimeUnit unit) {
        return this.getBoundOperations().leftPop(timeout, unit);
    }

    @Override
    public V rightPop() {
        return this.getBoundOperations().rightPop();
    }

    @Override
    public V rightPop(long timeout, TimeUnit unit) {
        return this.getBoundOperations().rightPop(timeout, unit);
    }

    @Override
    public Long remove(long i, Object value) {
        return this.getBoundOperations().remove(i, value);
    }

    @Override
    public V index(long index) {
        return this.getBoundOperations().index(index);
    }

    @Override
    public void set(long index, V value) {
        this.getBoundOperations().set(index, value);
    }

    @Override
    public String getKey() {
        return this.getBoundOperations().getKey();
    }

    @Override
    public DataType getType() {
        return this.getBoundOperations().getType();
    }

    @Override
    public Long getExpire() {
        return this.getBoundOperations().getExpire();
    }

    @Override
    public Boolean expire(long timeout, TimeUnit unit) {
        return this.getBoundOperations().expire(timeout, unit);
    }

    @Override
    public Boolean expireAt(Date date) {
        return this.getBoundOperations().expireAt(date);
    }

    @Override
    public Boolean persist() {
        return this.getBoundOperations().persist();
    }

    @Deprecated
    @Override
    public void rename(String newKey) {

    }
}
