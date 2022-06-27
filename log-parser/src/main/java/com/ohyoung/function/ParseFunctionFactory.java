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

    private Map<String, IParseFunction> functionMap;

    public ParseFunctionFactory(List<IParseFunction> parseFunctions) {
        if (CollectionUtils.isEmpty(parseFunctions)) {
            return;
        }
        functionMap = new HashMap<>();
        for (IParseFunction parseFunction : parseFunctions) {
            if (StringUtils.hasLength(parseFunction.functionName())) {
                functionMap.put(parseFunction.functionName(), parseFunction);
            }
        }
    }

    public IParseFunction getFunction(String functionName) {
        return functionMap.get(functionName);
    }

    public boolean isBeforeFunction(String functionName) {
        return Objects.nonNull(functionMap.get(functionName)) && functionMap.get(functionName).executeBefore();
    }

}
