package com.o18.redis.cache.operation;

import org.springframework.data.redis.core.BoundListOperations;

/**
 * ListSessionOperations
 */
public interface ListSessionOperations<V> extends BoundListOperations<String, V> {
}
