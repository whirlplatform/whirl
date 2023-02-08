package org.whirlplatform.server.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Работа с кешем EhCache
 */
public class CacheUtil {

    // private static final Log _log = LogFactory.getLog(CacheUtil.class);

    private static final Cache<String, Object> cache = CacheBuilder
        .newBuilder().maximumSize(3000)
        .expireAfterWrite(20, TimeUnit.MINUTES).build();

    private static Cache<String, Object> getCache() {
        return cache;
    }

    /**
     * Помещение элементов в кеш с ключом @param key, занчением @param value
     */
    private static void put(String key, Object value) {
        getCache().put(key, value);
    }

    /**
     * Получение значения из кеша по ключу @param key
     */
    private static <V> V get(String key) {
        return (V) getCache().getIfPresent(key);
    }

    /**
     * Создание ключа кеширования
     */
    private static String getCacheKey(String function, List<String> params) {
        return function + params;
    }

    /**
     * Получение результатов выполнения функции из кеша
     */
    public static <V> V getFunction(String function, List<String> params) {
        String key = getCacheKey(function, params);
        return get(key);
    }

    /**
     * Помещение результатов выполнения функции в кеш
     */
    public static void putFunction(String function, List<String> params,
                                   Object value) {
        String key = getCacheKey(function, params);
        put(key, value);
    }

    /**
     * Очистить кеши
     */
    public static void clearCache() {
        cache.invalidateAll();
        cache.cleanUp();
    }

    /**
     * Удалить из кэша ключ-значение
     */
    public void removeFunction(String function, List<String> params) {
        String key = getCacheKey(function, params);
        getCache().invalidate(key);
    }

}
