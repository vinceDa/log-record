package com.ohyoung.function;

/**
 * @author ouyb01
 * @date 2022/1/24 21:32
 */
public interface IFunctionService {

    String apply(String functionName, String value);

    boolean beforeFunction(String functionName);

}
