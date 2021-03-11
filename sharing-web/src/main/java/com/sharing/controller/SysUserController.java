package com.sharing.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharing.RabbitMQ.MqttReceiveConfig;
import com.sharing.common.entity.WebResult;
import com.sharing.entity.SysUser;
import com.sharing.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lfh
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserServiceImpl impl;




    @RequestMapping("/login")
    public WebResult login(@RequestBody  SysUser user){

        QueryWrapper<SysUser> query = new QueryWrapper<SysUser>();
        query.lambda().eq(SysUser::getSysUseremail, user.getSysUseremail()).eq(SysUser::getSpareFirst, user.getSpareFirst());
        List<SysUser> list = impl.list(query);

        if(list.size()>0){

            return new WebResult().ok(list.get(0));
        }else{
            return new WebResult().error(500,"用户名或密码错误");
        }

    }

    @RequestMapping("/userinfo")
    public WebResult userinfo(@RequestBody SysUser user){
        QueryWrapper<SysUser> query = new QueryWrapper<SysUser>();
        query.lambda().eq(SysUser::getSysUserid, user.getSysUserid());
       return new WebResult().ok(impl.list(query).get(0)) ;
    }

    @RequestMapping("/updateMoney")
    public WebResult updateMoney(@RequestBody  SysUser user){
        System.out.println(user);
        return new WebResult().ok(impl.updateById(user)) ;
    }

    @RequestMapping("/updateEmail")
    public WebResult updateEmail(@RequestBody SysUser user){
        QueryWrapper<SysUser> query = new QueryWrapper<SysUser>();
        query.lambda().eq(SysUser::getSysUserid,user.getSysUserid());
        Boolean bool  = impl.update(user,query);
        if(bool){
            return new WebResult().ok(impl.list(query).get(0)) ;
        }else{
            return new WebResult().error();
        }

    }

    @RequestMapping("/updatePhone")
    public WebResult updatePhone(@RequestBody SysUser user){
        QueryWrapper<SysUser> query = new QueryWrapper<SysUser>();
        query.lambda().eq(SysUser::getSysUserid,user.getSysUserid());
        Boolean bool  = impl.update(user,query);
        if(bool){
            return new WebResult().ok(impl.list(query).get(0)) ;
        }else{
            return new WebResult().error();
        }

    }

    @RequestMapping("/recharge")
    public WebResult recharge(@RequestBody SysUser user){
        QueryWrapper<SysUser> query = new QueryWrapper<SysUser>();
        query.lambda().eq(SysUser::getSysUserid,user.getSysUserid());
        Boolean bool  = impl.update(user,query);
        if(bool){

            return new WebResult().ok(impl.list(query).get(0)) ;
        }else{
            return new WebResult().error();
        }

    }







    @RequestMapping("/test")
    public WebResult test(){
        return new WebResult().ok(impl.list()) ;
    }



}

