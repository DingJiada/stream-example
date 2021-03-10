package com.shouzhi.service.common;

import com.shouzhi.service.interf.func.ThrowingConsumer;

import java.util.function.Consumer;

/**
 * ThrowingConsumer的包装类
 * @author WX
 * @date 2020-06-13 23:42:58
 */
public class ConsumerWrapper {


    /**
     * 抛出Checked受检异常
     * @param throwingConsumer
     * @param <T>
     * @return
     */
    public static <T> Consumer<T> throwingConsumerWrapper(ThrowingConsumer<T, Exception> throwingConsumer) {

        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    /**
     * 处理Checked受检异常
     * @param throwingConsumer
     * @param exceptionClass
     * @param <T>
     * @param <E>
     */
    public static <T, E extends Exception> Consumer<T> handlingConsumerWrapper(
            ThrowingConsumer<T, E> throwingConsumer, Class<E> exceptionClass) {

        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = exceptionClass.cast(ex);
                    System.err.println(
                            "Exception occured : " + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }
}
