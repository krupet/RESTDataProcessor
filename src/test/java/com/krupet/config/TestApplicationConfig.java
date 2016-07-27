package com.krupet.config;

import com.google.common.util.concurrent.MoreExecutors;
import com.krupet.Application;
import com.krupet.service.DataProcessorService;
import com.krupet.service.impl.DataProcessorServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.ExecutorService;

@Configuration
@PropertySource("classpath:test-application.properties")
@ComponentScan(basePackageClasses = Application.class)
@EnableWebMvc
public class TestApplicationConfig {

    @Value("#{new Integer('${numberOfWorkerThreads}')}")
    private int numberOfWorkerThreads;

    @Bean
    public DataProcessorService dataProcessorService() {
        ExecutorService executor = MoreExecutors.newDirectExecutorService();
        return new DataProcessorServiceImpl(executor);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1000000);
        return multipartResolver;
    }

}