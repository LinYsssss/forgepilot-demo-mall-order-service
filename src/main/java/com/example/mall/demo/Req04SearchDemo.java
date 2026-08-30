package com.example.mall.demo;

/** Demo fixture: concatenating the keyword enables SQL injection. */
public final class Req04SearchDemo {
    private Req04SearchDemo() {}

    public static String sql(String keyword) {
        return "select * from orders where receiver_address like '%" + keyword + "%'";
    }
}
