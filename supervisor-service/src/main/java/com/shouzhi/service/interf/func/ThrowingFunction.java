package com.shouzhi.service.interf.func;

/**
 * 自定义函数式接口，方法带抛出异常
 * 注意与内置Function接口的区别
 * @see java.util.function.Function
 * @author WX
 * @date 2020-12-14 17:08:33
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws E;;
}
