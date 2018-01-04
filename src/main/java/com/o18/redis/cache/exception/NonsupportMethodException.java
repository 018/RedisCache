package com.o18.redis.cache.exception;

/**
 * 不支持的方法
 */
public class NonsupportMethodException extends Exception {
    private String packageName;
    private String className;
    private String methodName;

    public NonsupportMethodException(String packageName, String className, String methodName, String message) {
        super(message);

        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
    }

    public String getMethodPath() {
        return String.format("%s.%s.%s", this.getPackageName(), this.getClassName(), this.getMethodPath());
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }
}
