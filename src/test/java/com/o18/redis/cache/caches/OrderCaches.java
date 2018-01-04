package com.o18.redis.cache.caches;

import com.o18.redis.cache.annotation.Description;
import com.o18.redis.cache.operation.HashSessionOperations;

@Description(value = "线程池设置", sort = 1)
public interface OrderCaches extends HashSessionOperations {
}

