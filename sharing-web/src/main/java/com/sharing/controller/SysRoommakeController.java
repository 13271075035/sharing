package com.sharing.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharing.common.entity.WebResult;
import com.sharing.entity.SysRoommake;
import com.sharing.service.impl.SysRoommakeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
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
@RequestMapping("/sysRoommake")
public class SysRoommakeController {
    @Autowired
    private SysRoommakeServiceImpl impl;
    @RequestMapping("/roomMake")
    public WebResult roomMake(@RequestBody SysRoommake room){
        QueryWrapper<SysRoommake> query = new QueryWrapper<SysRoommake>();
       query.lambda().eq(SysRoommake::getSysMakedate, room.getSysMakedate()).eq(SysRoommake::getSysShopid,room.getSysShopid());
        return new WebResult().ok(impl.list(query));
    }

    @RequestMapping("/selectRoomMake")
    public WebResult selectRoomMake(@RequestBody SysRoommake room){
        QueryWrapper<SysRoommake> query = new QueryWrapper<SysRoommake>();
        query.lambda().eq(SysRoommake::getSysMakedate, room.getSysMakedate());
        return new WebResult().ok(impl.list(query));
    }


}

