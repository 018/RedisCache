package com.o18.redis.cache;

import com.o18.redis.cache.caches.OrderCaches;
import com.o18.redis.cache.settings.TrysSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 推送订单失效定时器
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "applicationContext.xml")
public class RedisCacheTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    TrysSettings trysSettings;

    @Autowired
    OrderCaches orderCaches;

    @Test
    public void test() {

        // ###### settings ######
        trysSettings.setMax("1000");
        String max = trysSettings.getMax();
        System.out.print(max);

        trysSettings.set("Max", 2000);
        Object obj = trysSettings.get("Max");
        System.out.print(obj);

        try {
            //正确的是：trysSettings.setDate("2017-05-12");
            trysSettings.setDate("2017-05-12", "2017-05-12");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //正确的是：trysSettings.getDate();
            trysSettings.getDate("2017-05-12");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //正确的是：trysSettings.set("Max", 2000);
            trysSettings.set("Max", 2000, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //正确的是：trysSettings.get("Max");
            trysSettings.get("Max", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ###### caches ######

        orderCaches.put("3243", "018");
        Object gs = orderCaches.get("3243");
        System.out.print(gs);
    }

}
