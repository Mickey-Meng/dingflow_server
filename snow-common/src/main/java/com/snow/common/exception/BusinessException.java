package com.snow.common.exception;

/**
 * 业务异常
 * 
 * @author snow
 */
public class BusinessException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private final String message;

    private Integer code;

    public BusinessException(String message) {
        super();
        this.message = message;
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        super();
        this.code=code;
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
