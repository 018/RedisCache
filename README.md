# 利用redis做业务缓存和配置项

---

## 背景

​	从以前的C/S应用到现在的B/S，系统配置项都是必不可少的。一般都有一个SettingUtils类，提供read和write方法，然后就一大堆作为Key的常量。通过这样来实现：

```java
String ip = SettingUtils.read(SettingConsts.IP);//获取ip
SettingUtils.write(SettingConsts.IP, "127.0.0.1");//设置ip
```

​	然而，现在并发要求越来越高，缓存是个标配。无论是**业务数据**还是**配置项**，都可以往缓存里扔。缓存，也离不开Key，一大堆作为Key的常量。治理这些Key是个大问题。

## 遇到动态代理

​	动态代理，早些年就了解过，可一直没真正用到项目里，直到一次研究了一下mybatis源代码，发现其核心代码就是动态代理。那什么是动态代理呢？我就不详细解释了，对它不了解的还是乖乖的 [百度一下动态代理](https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=2&tn=baiduhome_pg&wd=java%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86&rsv_spt=1&oq=java%25E5%258A%25A8%25E6%2580%2581%25E4%25BB%25A3%25E7%2590%2586%25E5%258E%259F%25E7%2590%2586&rsv_pq=d10693e20000bea4&rsv_t=381a1iPoaHvDJs4d0JkBkiNXQHNDUHC7qL6WPo%2F%2FNbspRjPxPaioTBY92WXv47OsUg4z&rqlang=cn&rsv_enter=0&rsv_sug3=2&rsv_sug1=2&rsv_sug7=100&inputT=985&rsv_sug4=1058&rsv_sug=2) 。这里从网上投了一张图，如下：

![动态代理](https://github.com/018/RedisCache/raw/master/res/动态代理.png)

它大概就是我们可以动态的自定义的控制实现。给你Object proxy、Method method、Object[] args三个参数，然后你自己决定怎么实现。给个简单的例子：

```java
/**
* 接口
*/
public interface Something {
    String get(String key);
    String set(String key, String value);
}

/**
 * 调用处理器
 */
public class MyInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getName() + " doing ...");
        System.out.println("proxy is " + proxy.getClass().getName());
        System.out.println("method is " + method.getName());
        System.out.println("args is " + Arrays.toString(args));
        System.out.println(method.getName() + " done!");

        return method.getName() + " invoked";
    }
}

public class Test {
    public static void main(String[] args) {
        Something somethingProxy = (Something) java.lang.reflect.Proxy.newProxyInstance(Something.class.getClassLoader(),
                new Class<?>[]{Something.class},
                new MyInvocationHandler());

        System.out.println("somethingProxy.get(\"name\"): " + somethingProxy.get("name"));
        System.out.println();

        System.out.println("somethingProxy.set(\"name\", \"018\"): " + somethingProxy.set("name", "018"));
        System.out.println();
    }
}
```

以上代码的输出结果：

```
get doing ...
proxy is com.sun.proxy.$Proxy0
method is get
args is [name]
get done!
somethingProxy.get("name"): get invoked

set doing ...
proxy is com.sun.proxy.$Proxy0
method is set
args is [name, 018]
set done!
somethingProxy.set("name", "018"): set invoked
```

- 通过Proxy.newProxyInstance创建一个Something的代理对象somethingProxy。
- 通过somethingProxy实例调用其方法get/set时，会执行MyInvocationHandler.invoke方法。

## 思考

​	缓存，通过Key，返回值。

​	动态代理，通过Method(方法)，执行返回值。

​	怎么把它们关联起来呢？方法有方法名，那能不能把**Method method**的方法名对应到Key？**能！！！**

## 方案

​	在最开始的例子获取ip就应该这样写：

```java
public interface DataSourceSettings {
    String getIp();
    void setIp(String ip);
  
    int getPort();
    void setPort(int port);
  
  	// 其他项 ...
}

public class SettingsInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        // TODO: 
      	// 1、去掉get/set，截图后面的字符串作为Key
      	// 2、redis客户端通过Key获取值返回
    }
}
```

配置项完美解决了。

​	但业务缓存呢？相对来说，配置项的Key是固定的，而业务缓存的Key则是不固定的。比如缓存商品信息，商品id为001和002等等，得缓存不同的Key。就得有一个动态Key的解决方案，即`productCaches.put(商品id, 商品实体)`这样的实现方式。

​	参考spring-data-redis的BoundHashOperations，可以对其进行扩展实现这一功能。这样我们就可以这样定义一个商品缓存接口：

```java
public interface HashSessionOperations<HK, HV> extends BoundHashOperations<String, HK, HV> {
}

public interface ProductCaches extends HashSessionOperations {
}
```

缓存数据和获取数据则如下：

```java
productCaches.put(product1.prod_id, product1);//缓存数据
Product product = (Product)productCaches.get(prod_id);//获取缓存数据
```

至此，业务缓存也完美解决。

​	当然，我们对BoundListOperations、BoundSetOperations、BoundValueOperations、BoundZSetOperations进行对应的扩展。这样，这些不仅仅做业务缓存，也可以用它来作为redis的一个客户端使用。

​	看到这里，只看到了接口，也即是结果，知道了怎么使用它应用到项目中。但，怎么实现的呢？但是是动态代理。来，废话不多说，两个InvocationHandler码上来：

```java
/**
 * 简单的InvocationHandler
 * 主要用于执行配置项
 */
public class SimpleSessionOperationInvocationHandler implements InvocationHandler {
    private static final String METHOD_SET = "set";
    private static final String METHOD_GET = "get";
    private static final String METHOD_TOSTRING = "toString";
    private DefaultSimpleSessionOperations defaultSimpleSessionOperations;

    public SimpleSessionOperationInvocationHandler(DefaultSimpleSessionOperations defaultSimpleSessionOperations) {
        this.defaultSimpleSessionOperations = defaultSimpleSessionOperations;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> cls = method.getDeclaringClass();
        String group = cls.getSimpleName().replace("Settings", "").replace("Setting", "");
        String methodName = method.getName();
        String methodString = null;
        String item = null;
        Object value = null;

        if (METHOD_TOSTRING.equals(methodName)) {
            return cls.getName();
        } else if (METHOD_SET.equals(methodName)) {
            // void set(String item, ? value)
            if (method.getParameterCount() != 2 || !method.getParameterTypes()[0].getSimpleName().equals(String.class.getSimpleName()) ||
                    !method.getParameterTypes()[1].getSimpleName().equals(Object.class.getSimpleName()) ||
                    args == null || args.length != 2) {
                throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                        "方法声明错误，正确为 void set(String key, ? value)。");
            }

            methodString = METHOD_SET;
            item = args[0].toString();
            value = args[1];
        } else if (METHOD_GET.equals(methodName)) {
            // ? get(String item)
            if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].getSimpleName().equals(String.class.getSimpleName()) ||
                    args == null || args.length != 1) {
                throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                        "方法声明错误，正确为 ? get(String item)。");
            }

            methodString = METHOD_GET;
            item = args[0].toString();
        } else if (methodName.startsWith(METHOD_SET)) {
            // void setXXX(? value)
            if (method.getParameterCount() != 1 ||
                    args == null || args.length != 1) {
                throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                        "方法声明错误，正确为 void setXXX(? value)。");
            }

            methodString = METHOD_SET;
            item = methodName.substring(METHOD_SET.length());
            value = args[0];
        } else if (methodName.startsWith(METHOD_GET)) {
            // Object getXXX()
            if (method.getParameterCount() != 0 ||
                    (args != null && args.length != 0)) {
                throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                        "方法声明错误，正确为 Object getXXX()。");
            }

            methodString = METHOD_GET;
            item = methodName.substring(METHOD_GET.length());
        } else {
            throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                    "不支持的方法，只能是void set(String item, ? value)、? get(String item)、void setXXX(? value)、? getXXX()。");
        }

        switch (methodString) {
            case (METHOD_GET):
                Object val = this.defaultSimpleSessionOperations.get(group, item);
                return val;
            case (METHOD_SET):
                this.defaultSimpleSessionOperations.put(group, item, value);
        }
        return null;
    }
}
```

```java

/**
 * redis操作动态代理执行类
 * 主要用于执行业务缓存
 */
public class RedisSessionOperationInvocationHandler implements InvocationHandler {
    private static final String METHOD_TOSTRING = "toString";

    BoundKeyOperations<?> sessionOperations; // 具体执行的redis对象

    public RedisSessionOperationInvocationHandler(BoundKeyOperations<?> sessionOperations) {
        this.sessionOperations = sessionOperations;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> cls = method.getDeclaringClass();
        String methodName = method.getName();

        Class<?> targetCls = sessionOperations.getClass();
        Method methodTarget = targetCls.getDeclaredMethod(methodName, method.getParameterTypes());
        if (methodTarget == null) {
            throw new NonsupportMethodException(cls.getPackage().getName(), cls.getSimpleName(), methodName,
                    "不支持" + methodName + "方法。");
        }

        if (METHOD_TOSTRING.equals(methodName)) {
            return cls.getName();
        }

        Object result = methodTarget.invoke(sessionOperations, args);
        return result;
    }
}
```

至于接口创建代理，就交给ClassScannerConfigurer吧。

```java
/**
 * 类扫描配置类
 */
public class ClassScannerConfigurer implements InitializingBean, ApplicationContextAware, BeanFactoryAware, BeanNameAware {

    /**
     * 待扫描的包
     */
    private String basePackage;
    private String beanName;
    private DefaultListableBeanFactory beanFactory;
    private ApplicationContext applicationContext;
    private SessionOperationsFactory sessionOperationsFactory;
    private List<ClassInfo> classInfos;

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public SessionOperationsFactory getSessionOperationsFactory() {
        return sessionOperationsFactory;
    }

    public void setSessionOperationsFactory(SessionOperationsFactory sessionOperationsFactory) {
        this.sessionOperationsFactory = sessionOperationsFactory;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.basePackage, "Property 'basePackage' is required");

        // 扫描并创建接口的动态代理
        ClassPathScanner scanner = new ClassPathScanner();
        scanner.setResourceLoader(this.applicationContext);
        Set<Class> classes = scanner.doScans(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));

        if (Objects.isNull(classInfos)) {
            classInfos = new ArrayList<>(classes.size());
        }

        for (Class<?> cls : classes) {
            Object proxyObject = ProxyManager.newProxyInstance(this.sessionOperationsFactory, cls);
            String beanName = cls.getSimpleName().substring(0, 1).toLowerCase() + cls.getSimpleName().substring(1);
            this.beanFactory.registerSingleton(beanName, proxyObject);

            ClassInfo classInfo = new ClassInfo(beanName, cls, proxyObject);
            classInfos.add(classInfo);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
```

怎么加载这些呢，交给spring吧。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="
       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!--Redis 线程池配置 -->
	<bean id="jpoolConfig" class="redis.clients.jedis.JedisPoolConfig">
		<property name="maxIdle" value="200"></property>
		<property name="testOnBorrow" value="true"></property>
	</bean>

	<!--连接工厂 -->
	<bean id="connectionFactory"
		  class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
		<property name="hostName" value="127.0.0.1"></property>
		<property name="port" value="6339"></property>
		<property name="usePool" value="true"></property>
		<property name="timeout" value="100000"></property>
		<property name="poolConfig" ref="jpoolConfig"></property>
	</bean>

	<!--数据模板 -->
	<bean id="redisTemplate" class="org.springframework.data.redis.core.RedisTemplate">
		<property name="connectionFactory" ref="connectionFactory"></property>
	</bean>

	<!--redis template 缓存管理 -->
	<bean id="cacheManager" class="org.springframework.data.redis.cache.RedisCacheManager">
		<constructor-arg ref="redisTemplate"></constructor-arg>
		<property name="usePrefix" value="true"></property>
		<property name="loadRemoteCachesOnStartup" value="true"></property>
	</bean>

    <!-- 上面是配置redis的，从这里开始才是 -->
	<!-- 配置settingSessionFactory -->
	<bean id="settingSessionFactory" class="com.o18.redis.cache.SessionOperationsFactory">
		<constructor-arg ref="redisTemplate"/>
		<constructor-arg value="ORDER"/>
	</bean>

	<!-- 扫描Setting -->
	<bean class="com.o18.redis.cache.ClassScannerConfigurer">
		<property name="sessionOperationsFactory" ref="settingSessionFactory" />
		<property name="basePackage" value="com.o18.redis.cache.caches.*;com.o18.redis.cache.settings.*" />
	</bean>

</beans>
```

## 优点

- **代码统一管理**。一个包是系统配置项com.\*\*.settings.\*，一个包是业务缓存com.\*\*.caches.\*。
- **配置项随时改随时生效**。有些调优的参数，有些在特殊时期需要即时调整的。通过用web管理界面，友好的解决。

## 扩展

​	上面提到用web管理界面来即时修改配置项，即可以用一些特性，扫描所有配置项提供修改，分组、排序等等都是可以做到的。

​	还有，等等...

## 反思

- **安全**。原来配置项安安全全的在properties文件躺着，这样强行把它拉到安全的问题上来，当然祈祷redis安全！
- **默认方法不行**。接口上有默认方法，那段代码就相对于废了。
- **性能**。没实际做过压测，但鉴于mybatis，如果出现性能问题，那就是我写的代码需要优化，不是方案问题。

## 总结

通过mybatis的动态代理，实现基于redis的配置项即时修改生效。还扩展了业务缓存，使其代码集中。该方案中核心是动态代理，依赖于spring-data-redis。

此方案供学习，也提供一种思路让大家思考。如文中有bug，可以联系我。如有更好的方案，也联系我。如觉得不错想打赏，非常感谢。

![微信打赏](https://github.com/018/me/raw/master/res/wechat_pay.png)

![支付宝打赏](https://github.com/018/me/raw/master/res/ali_pay.png)

![扫码加我](https://github.com/018/me/raw/master/res/wechat.png)

