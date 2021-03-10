package com.shouzhi.service.common;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 去重包装，用于Class级别的对象去重；
 * java8 stream的distinct()只能对普通元素进行去重，而本包装器可以用于Class级别的对象去重
 * @author WX
 * @date 2021-01-25 15:22:40
 */
public class DistinctWrapper {

    /**
     * 根据对象中的某个字段去重，需配合java8 stream的filter一起使用
     * @param keyExtractor
     * @param <T>
     */
    public static <T> Predicate<T> byKey(Function<? super T, Object> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
