package com.o18.redis.cache.settings;

import com.o18.redis.cache.annotation.Description;

@Description(value = "线程池设置", sort = 1)
public interface TrysSettings {

    String getMax();

    void setMax(String max);

    String getDate(String m);//error

    void setDate(String date, String m);//error

    void set(String item, Object value);

    Object get(String item);

    void set(String item, Object value, int n);//error

    Object get(String item, int n);//error
}
