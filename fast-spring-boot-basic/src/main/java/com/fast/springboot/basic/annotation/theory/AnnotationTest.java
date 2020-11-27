package com.fast.springboot.basic.annotation.theory;

import java.lang.reflect.Method;

/**
 * @author bw
 * @since 2020-11-26
 */
public class AnnotationTest {
    @AnnoServiceName("mainMethodOfFastApp")
    public static void main(String[] args) throws NoSuchMethodException {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        Method method = AnnotationTest.class.getMethod("main", String[].class);
        AnnoServiceName annoServiceName = method.getAnnotation(AnnoServiceName.class);
        System.out.println(annoServiceName.value());
    }
}
