package com.ohyoung;

import com.ohyoung.config.EnableLogRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ouyb01
 * @date 2022/1/24 21:51
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableTransactionManagement
@EnableLogRecord(tenant = "com.mzt.test")
public class LogStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogStarterApplication.class, args);
    }

}
