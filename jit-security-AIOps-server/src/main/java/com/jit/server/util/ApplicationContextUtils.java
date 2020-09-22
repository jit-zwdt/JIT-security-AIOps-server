package com.jit.server.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description: ApplicationContextUtils
 * @Author: zengxin_miao
 * @Date: 2020/09/21
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextUtils.applicationContext == null) {
            ApplicationContextUtils.applicationContext = applicationContext;
        }
    }

    /**
     * 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     */
    public static Object getBean(String beanName) {
        return getApplicationContext().getBean(beanName);
    }

    /**
     * 通过class获取Bean.
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }


    private static Class getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Class) class1.newInstance();
    }

    /**
     * 通过classname获取Bean.
     */
    public static Object getBeanByClassName(String classname) throws Exception {
        Class<?> clazz = Class.forName(classname);
        String beanName = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
        return getApplicationContext().getBean(beanName);
    }

}