package com.sharing.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharing.common.entity.WebResult;
import com.sharing.entity.SysCoupon;

import com.sharing.service.impl.SysCouponServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lfh
 * @since 2021-03-11
 */
@Controller
@RequestMapping("/sysCoupon")
public class SysCouponController {

    @Autowired
    private SysCouponServiceImpl impl;

    @RequestMapping("/selectAll")
    public WebResult selectAll(@RequestBody SysCoupon cou){
        QueryWrapper<SysCoupon> query = new QueryWrapper<SysCoupon>();
        query.lambda().eq(SysCoupon::getCouUserId,cou.getCouUserId());
        return new WebResult().ok(impl.list(query)) ;
    }
}

