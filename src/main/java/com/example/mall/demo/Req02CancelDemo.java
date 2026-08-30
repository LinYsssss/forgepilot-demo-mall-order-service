package com.example.mall.demo;

/** Demo fixture: unconditional cancellation is not idempotent or state-safe. */
public final class Req02CancelDemo {
    private Req02CancelDemo() {}

    public static String cancel(long orderId) {
        return "CANCELLED:" + orderId;
    }
}
