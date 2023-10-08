package com.snow.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.snow.common.exception.BusinessException;
import com.snow.common.function.BranchFunction;
import com.snow.common.function.PresentOrElseFunction;
import com.snow.common.function.ThrowExceptionFunction;
import com.snow.common.function.ThrowExceptionMsgFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FunUtils {
    private static Logger logger = LoggerFactory.getLogger(FunUtils.class);

    /**
     *  如果参数为true抛出异常
     *
     * @param b
     * @return com.snow.common.function.ThrowExceptionFunction
     **/
    public static ThrowExceptionFunction isTure(boolean b){
        return (errorMessage) -> {
            if (b){
                throw new BusinessException(errorMessage);
            }
        };
    }

    /**
     *  如果参数为true抛出异常
     *  打印带日志的
     * @param b
     * @return com.snow.common.function.ThrowExceptionFunction
     **/
    public static ThrowExceptionMsgFunction isTureMsg(boolean b){
        return (code,errorMessage) -> {
            if (b){
                throw new BusinessException(code,errorMessage);
            }
        };
    }

    /**
     * 参数为true或false时，分别进行不同的操作
     *
     * @param b
     * @return com.snow.common.function.BranchFunction
     **/
    public static BranchFunction isTureOrFalse(boolean b){

        return (trueHandle, falseHandle) -> {
            if (b){
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }

    /**
     * 参数为true或false时，分别进行不同的操作
     *
     * @param str
     * @return com.snow.common.function.PresentOrElseFunction
     **/
    public static PresentOrElseFunction<?> isBlankOrNoBlank(String str){

        return (consumer, runnable) -> {
            if (str == null || str.length() == 0){
                runnable.run();
            } else {
                consumer.accept(str);
            }
        };
    }

    /**
     * 参数为true或false时，分别进行不同的操作
     *
     * @param str
     * @return com.snow.common.function.PresentOrElseFunction
     **/
    public static PresentOrElseFunction<?> isEmptyOrNoEmpty(Object str){

        return (consumer, runnable) -> {
            if (ObjectUtil.isEmpty(str)){
                runnable.run();
            } else {
                consumer.accept(str);
            }
        };
    }
}
