package com.ohyoung.config;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
