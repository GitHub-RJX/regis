package com.rjx.regis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.rjx.regis.entity.ShoppingCart;
import com.rjx.regis.mapper.ShoppingCartMapper;
import com.rjx.regis.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
