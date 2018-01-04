package com.o18.redis.cache;

import com.o18.redis.cache.data.ClassInfo;
import com.o18.redis.cache.proxy.ProxyManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
