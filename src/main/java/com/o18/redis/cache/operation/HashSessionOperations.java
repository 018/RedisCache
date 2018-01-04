package com.o18.redis.cache.operation;

import org.springframework.data.redis.core.BoundHashOperations;

/**
 * HashSessionOperations
 */
public interface HashSessionOperations<HK, HV> extends BoundHashOperations<String, HK, HV> {
}
