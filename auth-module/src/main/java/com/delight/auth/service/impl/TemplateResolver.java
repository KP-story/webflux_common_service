package com.delight.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Map;
import java.util.Objects;

@Component
public class TemplateResolver {


    @Autowired
    @Qualifier("springTemplateEngineString")
    private SpringTemplateEngine springTemplateEngineString;


    public String getEmailContentByTemplate(String template, Map<String, Object> data) {
        return processingTemplate(data, template, springTemplateEngineString);
    }

    public String processingTemplate(Map<String, Object> data, String template, TemplateEngine engine) {
        Context context = new Context();
        if (Objects.nonNull(data)) {
            context.setVariables(data);
        }
        return engine.process(template, context);
    }
}
