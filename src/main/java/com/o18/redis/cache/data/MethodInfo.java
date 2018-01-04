package com.o18.redis.cache.data;

import com.o18.redis.cache.annotation.Description;

import java.lang.reflect.Method;

/**
 * 方法信息
 */
public class MethodInfo {
    private Method method;
    private String methodName;
    private String description;
    private int sort;

    public MethodInfo(Method method) {
        this.method = method;
        this.methodName = method.getName();

        this.loadDescription();
    }

    protected void loadDescription() {
        Description description = method.getDeclaredAnnotation(Description.class);
        if (description != null) {
            this.description = description.value();
            this.sort = description.sort();
        } else {
            this.description = null;
            this.sort = this.methodName.hashCode();
        }
    }

    public Method getMethod() {
        return method;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getDescription() {
        return description;
    }

    public int getSort() {
        return sort;
    }
}