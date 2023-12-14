package com.rjx.regis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjx.regis.entity.OrderDetail;
import com.rjx.regis.mapper.OrderDetailMapper;
import com.rjx.regis.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
