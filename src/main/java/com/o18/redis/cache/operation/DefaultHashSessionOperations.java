package com.o18.redis.cache.operation;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 简单的HashSessionOperations
 */
public class DefaultHashSessionOperations<HK, HV> extends DefaultSessionOperations implements HashSessionOperations<HK, HV> {

    public DefaultHashSessionOperations(RedisTemplate<String, HV> redisTemplate, String serviceName, String key) {
        super(redisTemplate, serviceName, key);
    }

    private BoundHashOperations<String, HK, HV> getBoundOperations() {
        return this.redisTemplate.boundHashOps(this.redisKey);
    }

    @Override
    public RedisOperations getOperations() {
        return this.getBoundOperations().getOperations();
    }

    @Override
    public Boolean hasKey(Object key) {
        return this.getBoundOperations().hasKey(key);
    }

    @Override
    public Long increment(HK key, long delta) {
        return this.getBoundOperations().increment(key, delta);
    }

    @Override
    public Double increment(HK key, double delta) {
        return this.getBoundOperations().increment(key, delta);
    }

    @Override
    public HV get(Object key) {
        return this.getBoundOperations().get(key);
    }

    @Override
    public void put(HK key, HV value) {
        this.getBoundOperations().put(key, value);
    }

    @Override
    public Boolean putIfAbsent(HK key, HV value) {
        return this.getBoundOperations().putIfAbsent(key, value);
    }

    @Override
    public List<HV> multiGet(Collection<HK> keys) {
        return this.getBoundOperations().multiGet(keys);
    }

    @Override
    public void putAll(Map<? extends HK, ? extends HV> m) {
        this.getBoundOperations().putAll(m);
    }

    @Override
    public Set<HK> keys() {
        return this.getBoundOperations().keys();
    }

    @Override
    public List<HV> values() {
        return this.getBoundOperations().values();
    }

    @Override
    public Long size() {
        return this.getBoundOperations().size();
    }

    @Override
    public void delete(Object... keys) {
        this.getBoundOperations().delete(keys);
    }

    @Override
    public Map<HK, HV> entries() {
        return this.getBoundOperations().entries();
    }

    @Override
    public Cursor<Map.Entry<HK, HV>> scan(ScanOptions options) {
        return this.getBoundOperations().scan(options);
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
