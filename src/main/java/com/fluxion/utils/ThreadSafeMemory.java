package com.fluxion.utils;

import java.util.HashMap;
import java.util.Map;

public class ThreadSafeMemory {

    private static final ThreadLocal<Map<String, Object>> threadLocalMemory = ThreadLocal.withInitial(HashMap::new);

    // Add a key-value pair to the memory
    public static void put(String key, Object value) {
        threadLocalMemory.get().put(key, value);
    }

    // Retrieve a value by key
    public static Object get(String key) {
        return threadLocalMemory.get().get(key);
    }

    // Retrieve a value by key with a default value
    public static Object getOrDefault(String key, Object defaultValue) {
        return threadLocalMemory.get().getOrDefault(key, defaultValue);
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
