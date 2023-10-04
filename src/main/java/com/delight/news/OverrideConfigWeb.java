package com.delight.news;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.stereotype.Component;

@Component
public class OverrideConfigWeb implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DefaultPartHttpMessageReader) {
            ((DefaultPartHttpMessageReader) bean).setMaxHeadersSize(1000000);
        }
        return bean;
    }
}
