package com.fast.springboot.basic.annotation.theory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解被编译后会在项目顶层目录生成2个代理类：$Proxy0.class、$Proxy1.class
 * $Proxy0.class：   public final class $Proxy0 extends Proxy implements Retention {}
 * $Proxy1.class：   public final class $Proxy1 extends Proxy implements AnnoServiceName {}
 *
 * @author bw
 * @since 2020-11-26
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnoServiceName {
    String value();
}
