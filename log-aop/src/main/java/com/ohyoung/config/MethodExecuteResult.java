package com.ohyoung.config;

/**
 * @author ohYoung
 * @date 2022/4/7 22:47
 */
public class MethodExecuteResult {

    private boolean success;

    private Throwable throwable;

    private String errorMsg;

    public MethodExecuteResult(boolean success, Exception throwable, String errMsg) {
        this.success = success;
        this.throwable = throwable;
        this.errorMsg = errMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
