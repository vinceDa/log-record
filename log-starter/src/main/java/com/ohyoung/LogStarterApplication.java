package com.ohyoung;

//import com.ohyoung.config.EnableLogRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author ouyb01
 * @date 2022/1/24 21:51
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@EnableLogRecord(tenant = "com.ohyoung")
public class LogStarterApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(LogStarterApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
