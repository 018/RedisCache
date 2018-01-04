package com.o18.redis.cache.operation;

import org.springframework.data.redis.core.BoundZSetOperations;

/**
 * ZSetSessionOperations
 */
public interface ZSetSessionOperations<K, V> extends BoundZSetOperations<K, V> {
}
