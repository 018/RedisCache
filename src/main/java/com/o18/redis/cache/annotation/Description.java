package com.o18.redis.cache.annotation;

import java.lang.annotation.*;

/**
 * 描述
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Description {
    String value();
    int sort();
}
