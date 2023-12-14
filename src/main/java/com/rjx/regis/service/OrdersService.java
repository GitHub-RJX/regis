package com.rjx.regis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjx.regis.entity.Orders;

public interface OrdersService extends IService<Orders> {

    /**
     * 提交用户订单
     */
    void submit(Orders orders);
}
