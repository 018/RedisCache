package com.o18.redis.cache.operation;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 简单的ZSetSessionOperations
 */
public class DefaultZSetSessionOperations extends DefaultSessionOperations implements ZSetSessionOperations {

    public DefaultZSetSessionOperations(RedisTemplate<String, Object> redisTemplate, String serviceName, String key) {
        super(redisTemplate, serviceName, key);
    }

    @Override
    public RedisOperations getOperations() {
        return null;
    }

    @Override
    public void intersectAndStore(Object otherKey, Object destKey) {

    }

    @Override
    public void intersectAndStore(Collection otherKeys, Object destKey) {

    }

    @Override
    public Set range(long start, long end) {
        return null;
    }

    @Override
    public Set rangeByScore(double min, double max) {
        return null;
    }

    @Override
    public Set reverseRange(long start, long end) {
        return null;
    }

    @Override
    public Set reverseRangeByScore(double min, double max) {
        return null;
    }

    @Override
    public Set<ZSetOperations.TypedTuple> rangeWithScores(long start, long end) {
        return null;
    }

    @Override
    public Set<ZSetOperations.TypedTuple> rangeByScoreWithScores(double min, double max) {
        return null;
    }

    @Override
    public Set<ZSetOperations.TypedTuple> reverseRangeWithScores(long start, long end) {
        return null;
    }

    @Override
    public Set<ZSetOperations.TypedTuple> reverseRangeByScoreWithScores(double min, double max) {
        return null;
    }

    @Override
    public void removeRange(long start, long end) {

    }

    @Override
    public void removeRangeByScore(double min, double max) {

    }

    @Override
    public void unionAndStore(Object otherKey, Object destKey) {

    }

    @Override
    public void unionAndStore(Collection otherKeys, Object destKey) {

    }

    @Override
    public Boolean add(Object value, double score) {
        return null;
    }

    @Override
    public Double incrementScore(Object value, double delta) {
        return null;
    }

    @Override
    public Long rank(Object o) {
        return null;
    }

    @Override
    public Long reverseRank(Object o) {
        return null;
    }

    @Override
    public Long remove(Object... values) {
        return null;
    }

    @Override
    public Long count(double min, double max) {
        return null;
    }

    @Override
    public Long size() {
        return null;
    }

    @Override
    public Long zCard() {
        return null;
    }

    @Override
    public Double score(Object o) {
        return null;
    }

    @Override
    public Cursor<ZSetOperations.TypedTuple> scan(ScanOptions options) {
        return null;
    }

    @Override
    public Long add(Set set) {
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
