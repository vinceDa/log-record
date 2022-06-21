package com.ohyoung.config;

import com.ohyoung.LogRecordOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ouyb01
 * @date 2022/6/21 15:19
 */
@Configuration
public class Config {

    @Bean
    public LogRecordPointCut logRecordPointCut() {
        LogRecordPointCut logRecordPointCut = new LogRecordPointCut();
//        logRecordPointCut.setLogRecordOperationSource(logRecordOperationSource());
        return logRecordPointCut;
    }

//    @Bean
//    public LogRecordOperationSource logRecordOperationSource(){
//        return new LogRecordOperationSource();
//    }

    /**
     * 定义切面
     */
    @Bean
    public LogRecordPointcutAdvisor logRecordPointcutAdvisor() {
        LogRecordPointcutAdvisor advisor = new LogRecordPointcutAdvisor();
        advisor.setLogRecordPointCut(logRecordPointCut());
        advisor.setAdvice(logRecordInterceptor());
        return advisor;
    }

    /**
     * 定义通知
     *
     * @return
     */
    @Bean
    public LogRecordInterceptor logRecordInterceptor() {
        return new LogRecordInterceptor();
    }
}
