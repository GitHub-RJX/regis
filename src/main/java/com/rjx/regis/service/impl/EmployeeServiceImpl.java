package com.rjx.regis.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjx.regis.entity.Employee;
import com.rjx.regis.mapper.EmployeeMapper;
import com.rjx.regis.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


}
