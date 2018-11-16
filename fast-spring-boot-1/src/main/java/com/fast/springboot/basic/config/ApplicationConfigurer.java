package com.fast.springboot.basic.config;

import com.fast.springboot.basic.resolver.DecryptParamResolver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author bowen.yan
 * @date 2018-11-16
 */
@Configuration
public class ApplicationConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new DecryptParamResolver());
    }
}