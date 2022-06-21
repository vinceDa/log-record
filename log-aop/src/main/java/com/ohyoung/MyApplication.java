package com.ohyoung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yanghui
 */
@SpringBootApplication(scanBasePackages = "com.ohyoung")
public class MyApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(MyApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
