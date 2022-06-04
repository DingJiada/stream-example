package com.shouzhi.service.common;

import com.shouzhi.service.interf.func.ThrowingFunction;

import java.util.function.Function;

/**
 * ThrowingFunction的包装类
 * @author WX
 * @date 2020-12-14 17:13:16
 */
public class FunctionWrapper {


    /**
     * 抛出Checked受检异常
     * @param throwingFunction
     * @param <T>
     * @return
     */
    public static <T, R> Function<T, R> throwingFunctionWrapper(ThrowingFunction<T, R, Exception> throwingFunction) {
        return i -> {
            try {
                return throwingFunction.apply(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    // 处理Checked受检异常 用到再写
}
