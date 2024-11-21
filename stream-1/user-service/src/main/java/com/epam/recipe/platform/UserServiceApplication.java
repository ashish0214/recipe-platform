package com.epam.recipe.platform;

import com.epam.recipe.platform.config.RsaConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(RsaConfigurationProperties.class)
public class UserServiceApplication {

    public static void main(String[] args){
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
