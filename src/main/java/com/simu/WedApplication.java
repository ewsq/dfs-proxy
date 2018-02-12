package com.simu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author DengrongGuan
 * @create 2018-02-05 下午5:22
 **/
@EnableAutoConfiguration
@SpringBootApplication
public class WedApplication {
    public static void main(String[] args) {
        SpringApplication.run(WedApplication.class, args);
    }
}
