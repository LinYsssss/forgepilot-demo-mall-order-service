package com.example.mall.demo;

/** Demo fixture: detail lookup has no ownership check or masking. */
public final class Req03DetailDemo {
    private Req03DetailDemo() {}

    public static String detail(long orderId) {
        return "order=" + orderId + ",phone=13800000000";
    }
}
