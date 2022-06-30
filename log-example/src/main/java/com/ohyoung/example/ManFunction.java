package com.ohyoung.example;

import com.ohyoung.function.IParseFunction;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author ouyb01
 * @date 2022/6/23 15:26
 */
@Component
public class ManFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "manDetail";
    }

    @Override
    public String apply(String value) {
        return "apply: 【value : " + value + "姓名: 欧阳帅奔, 家产: 100000000000000000000000】";
    }
}
