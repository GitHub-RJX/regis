package com.rjx.regis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjx.regis.entity.User;
import com.rjx.regis.mapper.UserMapper;
import com.rjx.regis.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
