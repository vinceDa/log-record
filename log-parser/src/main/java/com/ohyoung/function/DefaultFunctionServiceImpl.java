package com.ohyoung.function;

import java.util.Objects;

/**
 * 根据传入的函数名称 functionName 找到对应的 IParseFunction，
 * 然后把参数传入到 IParseFunction 的 apply 方法上最后返回函数的值
 * @author ouyb01
 * @date 2022/1/24 21:33
 */
public class DefaultFunctionServiceImpl implements IFunctionService{

    private final ParseFunctionFactory parseFunctionFactory;

    public DefaultFunctionServiceImpl(ParseFunctionFactory parseFunctionFactory) {
        this.parseFunctionFactory = parseFunctionFactory;
    }

    @Override
    public String apply(String functionName, String value) {
        IParseFunction function = parseFunctionFactory.getFunction(functionName);
        if (Objects.isNull(function)) {
            return null;
        }
        return function.apply(value);
    }

    @Override
    public boolean beforeFunction(String functionName) {
        return parseFunctionFactory.isBeforeFunction(functionName);
    }
}
