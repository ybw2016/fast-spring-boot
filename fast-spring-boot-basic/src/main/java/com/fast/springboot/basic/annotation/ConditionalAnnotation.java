package com.fast.springboot.basic.annotation;

import com.fast.springboot.basic.annotation.model.Abc;
import com.fast.springboot.basic.annotation.model.MyCondition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author bowen.yan
 * @date 2018-11-14
 */
@Configuration
public class ConditionalAnnotation {
    @Conditional(MyCondition.class)
    @Bean
    public String condition() {
        System.err.println("Conditional -> @Conditional(MyCondition.class)");
        return "";
    }

    /**
     * 该Abc class位于类路径上时
     */
    @ConditionalOnClass(Abc.class)
    @Bean
    public String abc() {
        System.err.println("ConditionalOnClass -> @ConditionalOnClass(Abc.class)");
        return "";
    }


    //@ConditionalOnClass(TomcatWebServer.class)
    @Bean
    public Abc abcBean() {
        System.err.println("Bean -> @Bean -> creating Abc's instance return new Abc()");
        return new Abc();
    }

    /**
     * 类不存在于类路径上时
     */
    @ConditionalOnMissingClass({"com.fast.springboot.basic.annotation.model.Abc1"})
    @Bean
    public String conditionalOnMissingClass() {
        System.err.println("ConditionalOnMissingClass -> @ConditionalOnMissingClass({\"Abc\"})");
        return "";
    }

    /**
     * 存在Abc类的实例时
     */
    @ConditionalOnBean(Abc.class)
    @Bean
    public String bean() {
        System.err.println("ConditionalOnBean -> @ConditionalOnBean(Abc.class)");
        return "";
    }

    @ConditionalOnMissingBean(Abc.class)
    @Bean
    public String missBean() {
        System.err.println("ConditionalOnBean -> @ConditionalOnMissingBean(Abc.class)");
        return "";
    }

    /**
     * 表达式为true时
     */
    @ConditionalOnExpression(value = "true")
    @Bean
    public String expression() {
        System.err.println("ConditionalOnExpression -> @ConditionalOnExpression(value = \"true\")");
        return "";
    }

    /**
     * 配置文件属性是否为true
     */
    @ConditionalOnProperty(value = {"abc.url"}, matchIfMissing = false)
    @Bean
    public String propertyExist() {
        System.err.println("ConditionalOnProperty -> property [abc.url] is existing in property file");
        return "";
    }

    /**
     * 配置文件属性是否为true
     */
    @ConditionalOnProperty(value = {"abc.url"}, havingValue = "http://www.baidu.com")
    @Bean
    public String propertyEqual() {
        System.err.println("ConditionalOnProperty -> property [abc.url] is equal");
        return "";
    }
}
