package com.simu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author DengrongGuan
 * @create 2018-02-12
 **/
@Configuration
public class MultipartResolver {
    @Bean("multipartResolver")
    public CommonsMultipartResolver getCommonsMultipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(25000);
        return commonsMultipartResolver;
    }

}
