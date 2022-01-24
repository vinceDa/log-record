package com.ohyoung.config;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @author ouyb01
 * @date 2022/1/24 14:44
 */
public class LogRecordPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private LogRecordPointCut logRecordPointCut;

    @Override
    public Pointcut getPointcut() {
        return logRecordPointCut;
    }

    void setLogRecordPointCut(LogRecordPointCut logRecordPointCut) {
        this.logRecordPointCut = logRecordPointCut;
    }

}
