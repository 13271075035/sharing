package com.sharing.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharing.common.entity.WebResult;
import com.sharing.entity.SysOrder;
import com.sharing.entity.SysUser;
import com.sharing.service.impl.SysOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.ConsoleHandler;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lfh
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/sysOrder")
public class SysOrderController {
    @Autowired
    private SysOrderServiceImpl impl;

    @RequestMapping("/order")
    public WebResult order(@RequestBody SysOrder order){
//        QueryWrapper<SysOrder> query = new QueryWrapper<SysOrder>();
//        query.lambda().eq(SysOrder::getSysUserid, order.getSysUserid());
        return new WebResult().ok(impl.list()) ;
    }

    @RequestMapping("/insertOrder")
    public WebResult insertOrder(@RequestBody SysOrder order){


        return  new WebResult().ok(impl.save(order));
    }
}

