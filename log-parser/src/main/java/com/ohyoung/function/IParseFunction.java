package com.ohyoung.function;

import java.lang.reflect.Method;

/**
 * @author ouyb01
 * @date 2022/1/24 21:28
 */
public interface IParseFunction {

    /**
     * 自定义函数是否在业务代码执行之前解析
     * @return 默认为否
     */
    default boolean executeBefore() {
        return false;
    }

    /**
     * 获取方法名
     * @return 返回函数名称
     */
    String functionName();

    /**
     * 自定义函数的方法对象, 用于给SpEL的context注册自定义函数
     * registerFunction(String name, Method method)
     */
    Method functionMethod();

    /**
     * 执行
     * @param value 值
     */
    String apply(String value);

}
