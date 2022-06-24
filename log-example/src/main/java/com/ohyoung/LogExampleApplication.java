package com.ohyoung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ouyb01
 * @date 2022/6/24 14:52
 */
@SpringBootApplication
public class LogExampleApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(LogExampleApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
