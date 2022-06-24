//package com.ohyoung.config;
//
//import com.ohyoung.LogRecordAnnotationParser;
//import com.ohyoung.function.IParseFunction;
//import com.ohyoung.function.ParseFunctionFactory;
//import com.ohyoung.service.ILogRecordService;
//import com.ohyoung.service.impl.DefaultLogRecordServiceImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.ImportAware;
//import org.springframework.core.annotation.AnnotationAttributes;
//import org.springframework.core.type.AnnotationMetadata;
//
//import java.util.List;
//
///**
// * @author ouyb01
// * @date 2022/1/24 21:52
// */
//@Configuration
//public class LogRecordProxyAutoConfiguration implements ImportAware {
//
//    private static final Logger log = LoggerFactory.getLogger(LogRecordProxyAutoConfiguration.class);
//
//    private AnnotationAttributes enableLogRecord;
//
//    @Bean
//    public LogRecordAnnotationParser logRecordAnnotationParser() {
//        return new LogRecordAnnotationParser();
//    }
//
//    @Bean
//    public ParseFunctionFactory parseFunctionFactory(@Autowired List<IParseFunction> parseFunctions) {
//        return new ParseFunctionFactory(parseFunctions);
//    }
//
//    //    @Bean
//    //    @ConditionalOnMissingBean(IParseFunction.class)
//    //    public DefaultParseFunction parseFunction() {
//    //        return new DefaultParseFunction();
//    //    }
//
//
//    /**
//     * 定义切面
//     */
//    @Bean
//    public LogRecordPointcutAdvisor logRecordPointcutAdvisor() {
//        LogRecordPointcutAdvisor advisor = new LogRecordPointcutAdvisor();
//        advisor.setAdvice(logRecordInterceptor());
//        return advisor;
//    }
//
//    /**
//     * 定义通知
//     *
//     * @return
//     */
//    @Bean
//    public LogRecordInterceptor logRecordInterceptor() {
//        LogRecordInterceptor interceptor = new LogRecordInterceptor();
////        interceptor.setLogRecordAnnotationParser(logRecordAnnotationParser());
////        interceptor.setLogRecordService(logRecordService());
//        return interceptor;
//    }
//
//    //    @Bean
//    //    @ConditionalOnMissingBean(IOperatorGetService.class)
//    //    @Role(BeanDefinition.ROLE_APPLICATION)
//    //    public IOperatorGetService operatorGetService() {
//    //        return new DefaultOperatorGetServiceImpl();
//    //    }
//
//    @Bean
//    @ConditionalOnMissingBean(ILogRecordService.class)
//    public ILogRecordService logRecordService() {
//        return new DefaultLogRecordServiceImpl();
//    }
//
//    @Override
//    public void setImportMetadata(AnnotationMetadata importMetadata) {
//        this.enableLogRecord = AnnotationAttributes.fromMap(
//                importMetadata.getAnnotationAttributes(EnableLogRecord.class.getName(), false));
//        if (this.enableLogRecord == null) {
//            log.info("@EnableCaching is not present on importing class");
//        }
//    }
//}
