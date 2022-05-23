package com.cards.zokudo.config;

import com.google.common.collect.Lists;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@EnableSwagger2
public class ApplicationConfig {

    @Value("${swagger.enable}")
    private String isSwaggerEnable;

    @Value("${mail.host}")
    private String emailHost;

    @Value("${mail.port}")
    private String emailPort;

    @Value("${mail.username}")
    private String emailUserName;

    @Value("${mail.password}")
    private String emailPassword;

    @Value("${mail.starttlsenable}")
    private String emailStartSenable;

    @Value("${mail.debug}")
    private String emailDebug;


    @Bean
    public Client client() {
        return ClientBuilder
                .newBuilder()
                .register(new LoggingFeature(Logger.getLogger(this.getClass().getName()),
                        Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, null))
                .build();
    }

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build().apiInfo(metaData()).securitySchemes(Lists.newArrayList(new BasicAuth("basicAuth")))
                .enable(Boolean.parseBoolean(isSwaggerEnable));
    }

    private ApiInfo metaData() {
        return new ApiInfo(
                "Customer Microservice",
                "Customer Microservice",
                "2.0",
                "Terms of service",
                new Contact("Autobots", "https://www.autobots.com", ""),
                "Version 2.0",
                "",
                Collections.emptyList());
    }

    @Bean
    public JavaMailSender getMailSender(){
        JavaMailSenderImpl mailsender = new JavaMailSenderImpl();
        mailsender.setHost(emailHost);
        mailsender.setPort(Integer.valueOf(emailPort));
        mailsender.setUsername(emailUserName);
        mailsender.setPassword(emailPassword);

        Properties javaMailProperties= new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable",emailStartSenable);
        javaMailProperties.put("mail.smpt.auth","true");
        javaMailProperties.put("mail.transport.protocol","smtp");
        javaMailProperties.put("mail.debug",emailDebug);
        mailsender.setJavaMailProperties(javaMailProperties);
        return mailsender;

    }


}
