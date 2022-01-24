package com.ohyoung.config;

import com.ohyoung.LogRecordOperationSource;
import com.ohyoung.function.IFunctionService;

/**
 * @author ouyb01
 * @date 2022/1/24 21:57
 */
public class LogRecordInterceptor {

    private LogRecordOperationSource logRecordOperationSource;

    private String tenant;

    private IFunctionService functionService;

    public LogRecordOperationSource getLogRecordOperationSource() {
        return logRecordOperationSource;
    }

    public void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        this.logRecordOperationSource = logRecordOperationSource;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public IFunctionService getFunctionService() {
        return functionService;
    }

    public void setFunctionService(IFunctionService functionService) {
        this.functionService = functionService;
    }
}
