package com.delight.news;

import com.delight.gaia.connector.http.config.EnableHttpClients;

import com.delight.gaia.jpa.config.EnableCustomType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;


@Configuration
@ComponentScan(basePackages = {"com.delight"})
@SpringBootApplication(exclude = {FeignAutoConfiguration.class})
@PropertySources({
        @PropertySource("classpath:application.yml"),
})
@EnableAspectJAutoProxy
@Slf4j
@EnableHttpClients(basePackages = {"com.delight"})
@EnableCustomType(basePackages = {"com.delight"})
@EnableR2dbcAuditing
@EnableScheduling
//@EnableMessaging(basePackages = {"com.delight"})
//@EnableMessagingServer
//@EnableMessagingAutoRoute
public class Application {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        SpringApplication.run(Application.class, args);
    }

    @Bean
    R2dbcMappingContext r2dbcMappingContext() {
        return new R2dbcMappingContext();
    }

}
