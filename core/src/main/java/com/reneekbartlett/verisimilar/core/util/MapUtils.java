//package com.reneekbartlett.verisimilar.core.util;
//
//import java.util.HashMap;
//import java.util.Optional;
//import java.util.function.Consumer;
//
//public class MapUtils {
//
//    // custom helper for handling existing values
//    public static <K, V> void getWithCallback(HashMap<K,V> map, K key, Consumer<V> callback, Runnable runnable) {
//        V value = map.get(key);
//        if(value != null) {
//            callback.accept(value);
//        }
//
//        Optional.ofNullable(map.get(key)).ifPresentOrElse(callback, runnable);
//    }
//
//}
