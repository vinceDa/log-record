package com.ohyoung.function;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ouyb01
 * @date 2022/1/24 21:29
 */
public class ParseFunctionFactory {

    private Map<String, IParseFunction> allFunctionMap;

    public ParseFunctionFactory(List<IParseFunction> parseFunctions) {
        if (CollectionUtils.isEmpty(parseFunctions)) {
            return;
        }
        allFunctionMap = new HashMap<>();
        for (IParseFunction parseFunction : parseFunctions) {
            if (StringUtils.hasLength(parseFunction.functionName())) {
                allFunctionMap.put(parseFunction.functionName(), parseFunction);
            }
        }
    }

    public IParseFunction getFunction(String functionName) {
        return allFunctionMap.get(functionName);
    }

    public boolean isBeforeFunction(String functionName) {
        return Objects.nonNull(allFunctionMap.get(functionName)) && allFunctionMap.get(functionName).executeBefore();
    }

}
