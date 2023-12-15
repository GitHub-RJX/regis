package com.rjx.regis.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjx.regis.common.CommonsConst;
import com.rjx.regis.common.R;
import com.rjx.regis.entity.Employee;
import com.rjx.regis.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 员工控制类
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService = null;

    /**
     * 登录请求处理
     * TODO 后续改进将业务处理的代码放入业务层，这里只做数据请求与返回
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,
                             @RequestBody Employee employee) {
        // 将页面提交的密码进行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 根据用户名查数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        // 查不到返回登录失败结果
        if (emp == null) {
            return R.error(CommonsConst.LOGIN_FAIL);
        }
        // 比对密码
        if (!emp.getPassword().equals(password)) {
            return R.error(CommonsConst.LOGIN_FAIL);
        }
        // 查看员工状态
        if (emp.getStatus() == CommonsConst.EMPLOYEE_STATUS_NO) {
            return R.error(CommonsConst.LOGIN_ACCOUNT_STOP);
        }
        // 登录成功将员工的ID放入session中
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 后管登出
     */
    @PostMapping("/logout")
    public R<String> loginOut(HttpServletRequest request) {
        // 去除session
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工信息：{}", employee.toString());
        // 设置默认密码为123456 并进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(CommonsConst.INIT_PASSWORD.getBytes()));
        // 设置创建时间
        //employee.setCreateTime(LocalDateTime.now());
        // 设置更新时间
        //employee.setUpdateTime(LocalDateTime.now());
        // 用户ID设置（session中取得）
        //Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
        // 调用存储方法
        employeeService.save(employee);
        return R.success("添加成功");
    }

    /**
     * 员工信息分页查询
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);
        // 构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        // 构造条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name).or()
                .like(StringUtils.isNotEmpty(name), Employee::getUsername, name);
        // 添加排序
        queryWrapper.orderByAsc(Employee::getCreateTime);
        // 执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据用户ID去修改用户状态
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        long id = Thread.currentThread().getId();
        log.info("线程ID为：{}", id);
        // 获取员工ID
        // Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据ID查询员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据ID查询员工信息");
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
