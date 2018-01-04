package com.o18.redis.cache.operation;

import org.springframework.data.redis.core.BoundValueOperations;

/**
 * ValueSessionOperations
 */
public interface ValueSessionOperations<K, V> extends BoundValueOperations<K, V> {
}
