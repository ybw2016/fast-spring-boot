package com.fast.springboot.basic.proxy.rawtype;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 详细讲解： https://blog.csdn.net/m0_37607945/article/details/107463353
 * 评论答案：https://bbs.csdn.net/topics/320004756
 *
 * @author yanbowen
 * @date 2021-09-12
 */
public class RawTypeService {
    private static ApplicationContext applicationContext;

    /**
     * 获取这个代理类的父类（推荐）
     * 注意：优先使用此方法，因此其他方法可能都有坑。
     * 1. 被代理bean的实际样子：JDK代理模式下，bean以Proxy; 在CGLIB代理模式下，bean以$$开头
     * 2. JDK代理就是在运行时动态创建了一个类名以Proxy开头的类实现了指定的接口, 因为不能多继承，这也是为什么JDK动态代理必须有接口存在的原因。代理类实现了被代理类的接口，是implements关系。
     * 3. CGLIB代理模式呢？是在运行时动态创建一个类继承了目标类，注意，因为是继承（extends）关系，所以是类，而不是接口。
     * 4. JDK自带的工具HSDB可以在JVM中查看类的实际结构
     *
     * @param beanClass beanFactory中获取的bean对象
     * @return 原始类型
     */
    public static Class<?> findRawTypeClassFromSpringBean(Class<?> beanClass) {
        Class<?> rawClass = ClassUtils.getUserClass(beanClass);
        return rawClass;
    }

    /**
     * 注意：此方法，在多次代理场景未验证过（暂不推荐）
     *
     * @return
     */
    public static Class<?> findRawTypeClass() {
        Class<?> clazz;
        Object proxy = applicationContext.getBean("beanName");
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
            AdvisedSupport advised = (AdvisedSupport) new DirectFieldAccessor(proxy).getPropertyValue("advised");
            clazz = advised.getTargetClass();
        } else {
            clazz = AopUtils.getTargetClass(proxy);
        }
        return clazz;
    }

    /**
     * 注意：不要使用此方法！！！
     * 该方法只能找到最外层被代理的类，如果被代理多次，此方法找到的类不准确！
     * org.springframework.aop.support.AopUtils#getTargetClass(java.lang.Object) line 110 找到result判空后就直接返回了
     *
     * @param bean
     * @return
     */
    public static Class<?> findRawTypeClassFromSpringBean(Object bean) {
        Class<?> rawClass = AopUtils.getTargetClass(bean);
        return rawClass;
    }

    /**
     * 注意：此方法，在多次代理场景未验证过（暂不推荐）
     *
     * @param obj
     * @return
     */
    public static Object getTrueTargetFrom0(Object obj) {
        try {
            //获取第一个拦截器
            Field field = obj.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            field.setAccessible(true);
            MethodInterceptor interceptor = (MethodInterceptor) field.get(obj);

            //获取拦截器的属性advised
            Field advised = interceptor.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            AdvisedSupport advisedSupport = (AdvisedSupport) advised.get(interceptor);
            TargetSource targetSource = null;
            if (advisedSupport != null) {
                targetSource = advisedSupport.getTargetSource();
            }
            return targetSource != null ? targetSource.getTarget() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
