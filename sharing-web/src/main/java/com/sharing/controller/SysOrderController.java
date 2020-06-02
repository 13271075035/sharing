package com.sharing.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sharing.common.entity.WebResult;
import com.sharing.entity.SysFire;
import com.sharing.entity.SysOrder;
import com.sharing.entity.SysRoom;
import com.sharing.RabbitMQ.MqttGateway;
import com.sharing.entity.SysUser;
import com.sharing.service.impl.SysOrderServiceImpl;
import com.sharing.service.impl.SysRoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    @Autowired
    private SysRoomServiceImpl roomImpl;
    @Autowired
    private MqttGateway mqttGateway;

    @RequestMapping("/order")
    public WebResult order(@RequestBody SysOrder order){
        QueryWrapper<SysOrder> query = new QueryWrapper<SysOrder>();
        query.lambda().eq(SysOrder::getSysUserid, order.getSysUserid());
        return new WebResult().ok(impl.list(query)) ;
    }

    @GetMapping("/selectAllOrder")
    public WebResult selectAllOrder(Integer currentPage,Integer myts) throws ParseException {
        if(currentPage == null){
            currentPage = 1;
        }
        if(myts == null){
            myts =10;
        }
        QueryWrapper<SysOrder> query = new QueryWrapper<SysOrder>();
        query.lambda().orderByDesc(SysOrder::getSysOrderid);
        Page<SysOrder> page = new Page<>(currentPage, myts);
        IPage<SysOrder> pagelist = impl.page(page);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        for(int i =0;i<pagelist.getRecords().size();i++){
            String date = pagelist.getRecords().get(i).getSysPaymenttime();
            date = date.replace("Z", " UTC");//注意是空格+UTC
            Date d = formatter.parse(date);

            String sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
            pagelist.getRecords().get(i).setSysPaymenttime(sdf);
        }


        return  new WebResult().ok(pagelist);
    }

    @PostMapping("/closeOrder")
    public WebResult closeOrder( SysOrder order){
        QueryWrapper<SysRoom> query = new QueryWrapper<SysRoom>();
        query.lambda().eq(SysRoom::getSysRoomid,order.getSysRoomid());
        List<SysRoom> list =  roomImpl.list(query);

        list.get(0).setSpareTwo("0");
        roomImpl.updateById(list.get(0));
        order.setState(1);
        impl.updateById(order);
        mqttGateway.sendToMqtt("$APP,TAKEOVER*",list.get(0).getSpareFirst());
        return new WebResult().ok("操作成功");
    }

    @RequestMapping("/insertOrder")
    public WebResult insertOrder(@RequestBody SysOrder order){

        String[] makedate = order.getSysMakedate().split("/");
        String[] maketime = order.getSysMaketime().split("-");
        String[] endtime = maketime[1].split(":");
        QueryWrapper<SysRoom> query = new QueryWrapper<SysRoom>();
        query.lambda().eq(SysRoom::getSysRoomid,order.getSysRoomid());
        List<SysRoom> list =  roomImpl.list(query);

        Calendar startDate = Calendar.getInstance();


        startDate.set(Integer.parseInt(makedate[0]), (Integer.parseInt(makedate[1])-1), Integer.parseInt(makedate[2]),Integer.parseInt(endtime[0]) , Integer.parseInt(endtime[1]), 0);
        System.out.println(startDate);
        Timer ti = new Timer();
        ti.schedule(new TimerTask() {
            public void run() {
                mqttGateway.sendToMqtt("$APP,TAKEOVER*",list.get(0).getSpareFirst());
            }
        }, startDate.getTime() );

        return  new WebResult().ok(impl.save(order));
    }
}

