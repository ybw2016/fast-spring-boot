package com.fast.springboot.basic.annotation.model;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author bowen.yan
 * @date 2018-11-14
 */

public class MyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        //判断当前系统是Mac，Windows，Linux
        return conditionContext.getEnvironment().getProperty("os.name").contains("Mac");
    }
}