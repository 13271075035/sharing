package com.sharing.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharing.RabbitMQ.MqttReceiveConfig;
import com.sharing.common.entity.WebResult;
import com.sharing.entity.SysRoom;
import com.sharing.service.impl.SysRoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lfh
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/sysRoom")
public class SysRoomController {

    @Autowired
    private SysRoomServiceImpl impl ;

    private MqttReceiveConfig config;
    @RequestMapping("/room")
    public WebResult room(@RequestBody SysRoom room){
        System.out.println(room.getSysShopid());
        QueryWrapper<SysRoom> query = new QueryWrapper<SysRoom>();
        query.lambda().eq(SysRoom::getSysShopid,room.getSysShopid());
        return  new WebResult().ok(impl.list(query));
    }
    @RequestMapping("/addRoom")
    public WebResult addRoom(@RequestBody SysRoom room){
        config.adapter.addTopic(room.getSpareFirst(),1);
        return new WebResult().ok(impl.save(room));
    }

    @RequestMapping("/deleteRoom")
    public WebResult deleteRoom(@RequestBody SysRoom room){
        config.adapter.removeTopic(room.getSpareFirst());
        return new WebResult().ok(impl.removeById(room.getSysRoomid()));
    }
}

