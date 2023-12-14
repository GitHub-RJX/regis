package com.rjx.regis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjx.regis.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
