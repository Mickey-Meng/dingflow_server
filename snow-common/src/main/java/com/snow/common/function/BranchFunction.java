package com.snow.common.function;

/**
 * @author qimingjin
 * @date 2022-11-29 13:34
 * @Description:
 */
@FunctionalInterface
public interface BranchFunction {

    /**
     * 分支操作
     *
     * @param trueHandle 为true时要进行的操作
     * @param falseHandle 为false时要进行的操作
     * @return void
     **/
    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);
}
