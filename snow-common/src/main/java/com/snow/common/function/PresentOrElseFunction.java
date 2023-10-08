package com.snow.common.function;

import java.util.function.Consumer;

/**
 * @author qimingjin
 * @date 2022-11-29 13:36
 * @Description:
 */
@FunctionalInterface
public interface PresentOrElseFunction<T extends Object> {
    /**
     * 值不为空时执行消费操作
     * 值为空时执行其他的操作
     *
     * @param action 值不为空时，执行的消费操作
     * @param emptyAction 值为空时，执行的操作
     * @return void
     **/
    void presentOrElseHandle(Consumer<? super T> action, Runnable emptyAction);
}
