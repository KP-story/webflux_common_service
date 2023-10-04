package com.delight.auth.service.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
public class ThymeleafConfig {
    @Bean("springTemplateEngineString")
    public SpringTemplateEngine springTemplateEngineString() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(stringTemplateResolver());
        return templateEngine;
    }

    public StringTemplateResolver stringTemplateResolver() {
        StringTemplateResolver emailTemplateResolver = new StringTemplateResolver();
        emailTemplateResolver.setCacheable(false);
        emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
        return emailTemplateResolver;
    }
}
