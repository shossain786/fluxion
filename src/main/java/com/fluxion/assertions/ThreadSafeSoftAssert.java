package com.fluxion.assertions;
import org.testng.asserts.SoftAssert;

public class ThreadSafeSoftAssert {

    // ThreadLocal for SoftAssert
    private static final ThreadLocal<SoftAssert> softAssertThreadLocal = ThreadLocal.withInitial(SoftAssert::new);

    // Get the current thread's SoftAssert instance
    public static SoftAssert get() {
        return softAssertThreadLocal.get();
    }

    // Clear the SoftAssert instance (useful after assertion verification)
    public static void clear() {
        softAssertThreadLocal.remove();
    }
}
