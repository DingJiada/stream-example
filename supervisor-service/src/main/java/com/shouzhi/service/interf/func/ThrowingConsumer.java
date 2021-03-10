package com.shouzhi.service.interf.func;

/**
 * 自定义函数式接口，方法带抛出异常
 * 注意与内置Consumer接口的区别
 * @see java.util.function.Consumer
 * @author WX
 * @date 2020-06-13 23:39:06
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}
