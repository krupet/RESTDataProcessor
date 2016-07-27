package com.krupet.config;

import com.krupet.service.DataProcessorService;
import com.krupet.service.impl.DataProcessorServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.krupet.Application;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.Executors;

/**
 * The class ApplicationConfig.
 */
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackageClasses = Application.class)
@EnableWebMvc
public class ApplicationConfig {

    /** Number of simultaneously processed files. */
    @Value("#{new Integer('${numberOfWorkerThreads}')}")
    private int numberOfWorkerThreads;

    @Bean
    public DataProcessorService dataProcessorService() {
        return new DataProcessorServiceImpl(Executors.newFixedThreadPool(numberOfWorkerThreads));
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