package com.ohyoung.config;

import com.ohyoung.LogRecordAnnotationParser;
import com.ohyoung.evaluate.LogRecordValueParser;
import com.ohyoung.function.IParseFunction;
import com.ohyoung.function.ParseFunctionFactory;
import com.ohyoung.service.ILogRecordService;
import com.ohyoung.service.impl.DefaultLogRecordServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * @author ouyb01
 * @date 2022/1/24 21:52
 */
@Configuration
public class LogRecordProxyAutoConfiguration implements ImportAware {

    private static final Logger log = LoggerFactory.getLogger(LogRecordProxyAutoConfiguration.class);

    private AnnotationAttributes enableLogRecord;
    @Autowired
    private List<IParseFunction> parseFunctions;

    @Bean
    public LogRecordAnnotationParser logRecordAnnotationParser() {
        LogRecordAnnotationParser parser = new LogRecordAnnotationParser();
        parser.setParseFunctionFactory(parseFunctionFactory());
        return parser;
    }

    @Bean
    public ParseFunctionFactory parseFunctionFactory() {
        return new ParseFunctionFactory(parseFunctions);
    }

//        @Bean
//        @ConditionalOnMissingBean(IParseFunction.class)
//        public DefaultParseFunction parseFunction() {
//            return new DefaultParseFunction();
//        }


    @Bean
    public LogRecordPointCut logRecordPointCut() {
        return new LogRecordPointCut();
    }

    /**
     * 定义切面
     */
    @Bean
    public LogRecordPointcutAdvisor logRecordPointcutAdvisor() {
        LogRecordPointcutAdvisor advisor = new LogRecordPointcutAdvisor();
        advisor.setAdvice(logRecordInterceptor());
        advisor.setLogRecordPointCut(logRecordPointCut());
        return advisor;
    }

    /**
     * 注册日志存储的默认实现, 不然interceptor中注入的ILogRecordService会报错
     */
    @Bean
    @ConditionalOnMissingBean(ILogRecordService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public ILogRecordService logRecordService() {
        return new DefaultLogRecordServiceImpl();
    }

    /**
     * 定义通知
     *
     * @return
     */
    @Bean
    public LogRecordInterceptor logRecordInterceptor() {
        LogRecordInterceptor interceptor = new LogRecordInterceptor();
        interceptor.setLogRecordAnnotationParser(logRecordAnnotationParser());
        return interceptor;
    }

    //    @Bean
    //    @ConditionalOnMissingBean(IOperatorGetService.class)
    //    @Role(BeanDefinition.ROLE_APPLICATION)
    //    public IOperatorGetService operatorGetService() {
    //        return new DefaultOperatorGetServiceImpl();
    //    }


    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
//        this.enableLogRecord = AnnotationAttributes.fromMap(
//                importMetadata.getAnnotationAttributes(EnableLogRecord.class.getName(), false));
//        if (this.enableLogRecord == null) {
//            log.info("@EnableCaching is not present on importing class");
//        }
    }
}
