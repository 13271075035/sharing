package com.sharing.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qcloud.cos.utils.Md5Utils;
import com.sharing.common.constant.Constant;
import com.sharing.common.entity.WebResult;
import com.sharing.common.exception.ErrorCode;
import com.sharing.common.service.impl.CrudServiceImpl;
import com.sharing.common.utils.JedisUtil;
import com.sharing.common.utils.MD5Util;
import com.sharing.common.utils.TokenGenerator;
import com.sharing.dao.SysCustomerDao;
import com.sharing.dto.SysCustomerDto;
import com.sharing.entity.SysCustomerEntity;
import com.sharing.service.SysCustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SysCustomerServiceImpl extends CrudServiceImpl<SysCustomerDao, SysCustomerEntity, SysCustomerDto> implements SysCustomerService {
    @Override
    public QueryWrapper<SysCustomerEntity> getWrapper(Map<String, Object> params) {
        return null;
    }
   @Autowired
   SysCustomerDao sysCustomerDao;
    @Autowired
    JedisUtil jedisUtil;
    @Value("${frontEnd.domain}")
    private  String domain;

    @Override
    public WebResult login(@RequestBody SysCustomerDto sysCustomerDto, HttpServletResponse response) {

        WebResult webResult = new WebResult();
        HashMap<String, Object> map = new HashMap<>(2);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_email",sysCustomerDto.getCustomerEmail());
        queryWrapper.eq("is_delete", Constant.FDELETE);
        List<SysCustomerEntity> customerList = sysCustomerDao.selectList(queryWrapper);
        if(customerList!=null&&customerList.size()>0){

            String token = jedisUtil.hget(Constant.REDIS_USER_KEY + sysCustomerDto.getCustomerEmail(), "token");

            if(customerList.get(0).getPassword().equals(MD5Util.md5(sysCustomerDto.getPassword()))){

                String loginAcount = jedisUtil.get(Constant.REDIS_TOKEN_KEY + token);
                if (StringUtils.isBlank(token)||StringUtils.isBlank(loginAcount)) {
                   token = TokenGenerator.generateValue();
                    HashMap<String, String> customerInfo = new HashMap<>();
                    customerInfo.put("customerName",customerList.get(0).getCustomerName());
                    customerInfo.put("customerEmail",customerList.get(0).getCustomerEmail());
                    customerInfo.put("token",token);
                    customerInfo.put("customerPhone",customerList.get(0).getCustomerPhone());
                    jedisUtil.setExString(Constant.REDIS_TOKEN_KEY+token,Constant.REDIS_TOKEN_TIMEOUT,sysCustomerDto.getCustomerEmail());
                    jedisUtil.hmset(Constant.REDIS_USER_KEY+sysCustomerDto.getCustomerEmail(),customerInfo);

                }
            }else {
                return webResult.error(ErrorCode.ACCOUNT_PASSWORD_ERROR);
            }
            map.put(Constant.TOKEN_HEADER, token);
            Cookie cookie = new Cookie(Constant.TOKEN_HEADER, token);
            cookie.setHttpOnly(false);
            cookie.setDomain(domain);
            cookie.setPath("/");
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
        }else {
            return webResult.error(ErrorCode.ACCOUNT_NOT_EXIST);

        }

        return webResult.ok(map);
    }
}
