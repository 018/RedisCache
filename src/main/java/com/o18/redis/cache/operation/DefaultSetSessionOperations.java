package com.o18.redis.cache.operation;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 简单的SetSessionOperations
 */
public class DefaultSetSessionOperations extends DefaultSessionOperations implements SetSessionOperations {

    public DefaultSetSessionOperations(RedisTemplate<String, Object> redisTemplate, String serviceName, String key) {
        super(redisTemplate, serviceName, key);
    }

    @Override
    public RedisOperations getOperations() {
        return null;
    }

    @Override
    public Set diff(Object key) {
        return null;
    }

    @Override
    public Set diff(Collection keys) {
        return null;
    }

    @Override
    public void diffAndStore(Object key, Object destKey) {

    }

    @Override
    public void diffAndStore(Collection keys, Object destKey) {

    }

    @Override
    public Set intersect(Object key) {
        return null;
    }

    @Override
    public Set intersect(Collection keys) {
        return null;
    }

    @Override
    public void intersectAndStore(Object key, Object destKey) {

    }

    @Override
    public void intersectAndStore(Collection keys, Object destKey) {

    }

    @Override
    public Set union(Object key) {
        return null;
    }

    @Override
    public Set union(Collection keys) {
        return null;
    }

    @Override
    public void unionAndStore(Object key, Object destKey) {

    }

    @Override
    public void unionAndStore(Collection keys, Object destKey) {

    }

    @Override
    public Long add(Object[] values) {
        return null;
    }

    @Override
    public Boolean isMember(Object o) {
        return null;
    }

    @Override
    public Set members() {
        return null;
    }

    @Override
    public Boolean move(Object destKey, Object value) {
        return null;
    }

    @Override
    public Object randomMember() {
        return null;
    }

    @Override
    public Set distinctRandomMembers(long count) {
        return null;
    }

    @Override
    public List randomMembers(long count) {
        return null;
    }

    @Override
    public Long remove(Object... values) {
        return null;
    }

    @Override
    public Object pop() {
        return null;
    }

    @Override
    public Long size() {
        return null;
    }

    @Override
    public Cursor scan(ScanOptions options) {
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
