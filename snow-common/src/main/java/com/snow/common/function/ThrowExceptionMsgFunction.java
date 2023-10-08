package com.snow.common.function;

/**
 * @author qimingjin
 * @date 2022-11-29 13:30
 * @Description:
 */
@FunctionalInterface
public interface ThrowExceptionMsgFunction {

    void throwMessage(Integer code, String msg);
}
