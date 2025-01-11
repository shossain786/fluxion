package com.fluxion.core;

import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private static final ThreadLocal<Map<String, Object>> threadLocalMemory = ThreadLocal.withInitial(HashMap::new);

    public static void put(String key, Object value) {
        threadLocalMemory.get().put(key, value);
    }

    public static Object get(String key) {
        return threadLocalMemory.get().get(key);
    }

    public static Object getOrDefault(String key, Object defaultObjectValue) {
        return threadLocalMemory.get().getOrDefault(key, defaultObjectValue);
    }
    // Check if a key exists
    public static boolean containsKey(String key) {
        return threadLocalMemory.get().containsKey(key);
    }

    // Remove a key from the memory
    public static void remove(String key) {
        threadLocalMemory.get().remove(key);
    }

    // Clear all key-value pairs in the memory
    public static void clear() {
        threadLocalMemory.get().clear();
    }
}
