package org.lzx.lakemart.exception;

public class BusinessException extends RuntimeException {
    // 可选：错误码，方便前端处理不同业务错误
    private String code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}