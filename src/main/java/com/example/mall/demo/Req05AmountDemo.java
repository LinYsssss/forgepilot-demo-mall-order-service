package com.example.mall.demo;

/** Demo fixture: double arithmetic can lose cents. */
public final class Req05AmountDemo {
    private Req05AmountDemo() {}

    public static long paid(long amountFen, double discount) {
        return (long) ((amountFen / 100.0) * discount * 100);
    }
}
