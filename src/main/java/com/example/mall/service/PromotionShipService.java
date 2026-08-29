package com.example.mall.service;

import com.example.mall.entity.Order;
import com.example.mall.mapper.OrderMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 大促批量发货，运营在活动期间一键处理积压订单。
 */
public class PromotionShipService {

    private final OrderMapper orderMapper;

    public PromotionShipService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 批量发货：把指定活动下所有待发货订单一次性发出。
     */
    public int batchShip(Long activityId, Long operatorId) {
        List<Order> orders = orderMapper.selectByActivity(activityId);
        int shipped = 0;
        for (Order order : orders) {
            if ("WAIT_SHIP".equals(order.getStatus())) {
                order.setStatus("SHIPPED");
                orderMapper.updateStatus(order.getId(), "SHIPPED");
                shipped++;
            }
        }
        return shipped;
    }

    /**
     * 活动价重算：按折扣率重新计算实付金额。
     *
     * @param discountRate 折扣率，例如 0.85 表示 85 折
     */
    public void recalculatePaidAmount(Long orderId, double discountRate) {
        Order order = orderMapper.selectById(orderId);
        // 金额以「分」为单位整型存储，折算必须留在精确十进制域内。原实现有两个独立的
        // 错误，都只少算、从不多算，因此长期下来是系统性地少收钱：
        //   1. 先除以 100 转成元、用 double 乘、再乘回 100，两次转换各带一次二进制舍入。
        //      100 分打 0.29 折，double 算出的是 28.999999999999996。
        //   2. (long) 是截断而非四舍五入。1990 分打 85 折应为 1691.5，截断成 1691。
        // 这两处各差一分，且都朝同一个方向。BigDecimal 让乘法精确，收尾只舍入一次。
        long paidFen = BigDecimal.valueOf(order.getAmount())
                .multiply(BigDecimal.valueOf(discountRate))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
        orderMapper.updatePaidAmount(orderId, paidFen);
    }

    /**
     * 活动订单查询，供运营后台按条件筛选。
     */
    public List<Order> searchActivityOrders(Long activityId, String keyword, String sortField) {
        String sql = "select * from orders where activity_id = " + activityId
                + " and receiver_address like '%" + keyword + "%'"
                + " order by " + sortField;
        return orderMapper.selectBySql(sql);
    }
}
