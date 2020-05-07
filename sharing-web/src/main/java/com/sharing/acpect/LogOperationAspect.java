package com.sharing.acpect;


import com.sharing.common.annotation.LogOperation;
import com.sharing.common.constant.Constant;
import com.sharing.common.entity.WebResult;
import com.sharing.common.exception.DIYException;
import com.sharing.common.utils.HttpContextUtils;
import com.sharing.common.utils.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * 操作日志，切面处理类
 *
 * @author tecsmile@outlook.com
 */

@Aspect
@Component
@Slf4j
public class LogOperationAspect {
    @Autowired
    private JedisUtil jedisUtil;

    @Pointcut("@annotation(com.sharing.common.annotation.LogOperation)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        String phone = null;
        try {
            //执行方法
            Object result = point.proceed();
            String token = null;
            HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (StringUtils.equals(cookie.getName(), Constant.TOKEN_HEADER)) {
                        token = cookie.getValue();

                    }
                }
            }
            String loginAccount = jedisUtil.getString(Constant.REDIS_TOKEN_KEY + token);
            phone = jedisUtil.hget(Constant.REDIS_USER_KEY + loginAccount, "phone");
            if (StringUtils.isBlank(phone) || phone.equals("null")) {
                phone = jedisUtil.hget(Constant.REDIS_USER_KEY + loginAccount, "userId");
            }
            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            saveLog(point, time, result,phone);
            return result;
        }catch(DIYException e) {
            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            //保存日志
            WebResult error = new WebResult().error(e.getCode(),e.getMsg());
            saveLog(point, time, error,phone);
            return error;
        } catch (Exception e) {
            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            //保存日志
            WebResult error = new WebResult().error(e.getMessage());
            saveLog(point, time, error,phone);
            return new WebResult<>().error();
        }
    }

    /**
     * 如果需要将日志存库，那么自己添加
     * @param joinPoint
     * @param time
     * @param result
     */
    private void saveLog(ProceedingJoinPoint joinPoint, long time, Object result,String phone) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogOperation annotation = method.getAnnotation(LogOperation.class);
        WebResult webResult = new WebResult();
        try {
            BeanUtils.copyProperties(result,webResult);
            String code = "000000";
            int length = String.valueOf(webResult.getCode()).length();
            if (length<6) {
                int index = 6-length;
                code = code.substring(0,index) + webResult.getCode();
            } else {
                code = String.valueOf(webResult.getCode());
            }
            log.info("mart|{}|{}|{}|{}|{}|{}",annotation.apiType(),annotation.apiPath(),code,time,phone,webResult.getMsg());
        } catch (BeansException e) {
            log.info("mart|{}|{}|{}|{}|{}|{}",annotation.apiType(),annotation.apiPath(),"500",time,phone,"非通用操作结果！");
        }
    }
}
