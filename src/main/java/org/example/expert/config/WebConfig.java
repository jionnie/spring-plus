package org.example.expert.config;

import org.example.expert.domain.log.aop.LoggingAspect;
import org.example.expert.domain.log.service.LogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public LoggingAspect loggingAspect(LogService logService) {
        return new LoggingAspect(logService);
    }
}
