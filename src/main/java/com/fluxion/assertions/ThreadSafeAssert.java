package com.fluxion.assertions;

import org.testng.Assert;

public class ThreadSafeAssert {

    // Example wrapper for an assertEquals method
    public static void assertEquals(Object actual, Object expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    public static void assertFalse(boolean condition, String message) {
        Assert.assertFalse(condition, message);
    }

    // You can add more assertion methods if needed.
}
