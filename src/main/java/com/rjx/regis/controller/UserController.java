package com.rjx.regis.controller;

import cn.hutool.core.lang.PatternPool;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rjx.regis.common.CommonsConst;
import com.rjx.regis.common.R;
import com.rjx.regis.entity.User;
import com.rjx.regis.service.UserService;
import com.rjx.regis.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 移动端用户操作
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 移动端发送短信
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 获取手机号
        String phone = user.getPhone();
        // 校验手机号
        Pattern mobile = PatternPool.MOBILE;
        Matcher matcher = mobile.matcher(phone);
        if (!matcher.matches()) {
            return R.error("手机号格式有误");
        }
        if (StringUtils.isNotEmpty(phone)) {
            // 生成验证码
            String code = ValidateCodeUtils.generateValidateCode4String(4);
            log.info("regis外卖验证码：code为：" + code);

            // 调用阿里云短信服务API完成发送短信
//            SMSUtils.sendMessage("regis外卖", "", phone, code);

            // 将生成的验证码保存至session
//            session.setAttribute(phone, code);
            // 将生成的验证码保存至redis
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return R.success(code);
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     */
    @PostMapping("/login")
    @Transactional
    public R<User> login(@RequestBody Map map, HttpServletRequest request) {
        log.info(map.toString());
        // 获取手机号
        String phone = (String) map.get("phone");
        // 获取验证码
        String code = (String) map.get("code");
        // session中获取验证码
//        Object codeSession = session.getAttribute(phone);
        // redis中获取验证码
        Object codeRedis = redisTemplate.opsForValue().get(phone);
        // 比对验证码
        if (codeRedis != null && codeRedis.equals(code)) {
            // 成功，则登录
            // 判断当前用户是否为新用户，新用户自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            // 手机号查询新用户
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(CommonsConst.EMPLOYEE_STATUS_YES);
                userService.save(user);
            }
            request.getSession().setAttribute("user", user.getId());
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败,验证码有误");
    }

    /**
     * 退出功能
     * ①在controller中创建对应的处理方法来接受前端的请求，请求方式为post；
     * ②清理session中的用户id
     * ③返回结果（前端页面会进行跳转到登录页面）
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request) {
        //清理session中的用户id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
