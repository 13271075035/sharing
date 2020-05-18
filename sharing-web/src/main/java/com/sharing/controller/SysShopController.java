package com.sharing.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharing.common.entity.WebResult;
import com.sharing.entity.SysShop;
import com.sharing.entity.SysUser;
import com.sharing.service.impl.SysShopServiceImpl;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
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
@RequestMapping("/sysShop")
public class SysShopController {

    @Autowired
    private SysShopServiceImpl impl;
    @RequestMapping("/mapMake")
    public WebResult mapMake( @RequestBody  SysShop shop){
        QueryWrapper<SysShop> query = new QueryWrapper<SysShop>();
        query.lambda().like(SysShop::getSysShopname,shop.getSysShopname());
        List<SysShop> list = impl.list(query);
        for(int i = 0;i<list.size();i++){
            String[] splitShop = list.get(i).getSysBusinesstime().split("-");
            String[] splitTimeslot = shop.getSysBusinesstime().split("-");
            boolean bool = isDatesBetween(splitTimeslot[0],splitTimeslot[1],splitShop[0],splitShop[1]);
            if(bool==false){
             list.remove(i);
            }
        }
        System.out.println(JSON.toJSONString(list));
        return new WebResult().ok(list);
    }
    public Date dateParse(String dateString){
        String[] time = dateString.split(":");
        return new Date(2020,10,10,Integer.parseInt(time[0]),Integer.parseInt(time[1]));
    }

    public int dateCompare(String dateString,String compareDateString){
        long dateTime =  dateParse(dateString).getTime();
        long compareDateTime = dateParse(compareDateString).getTime();
        if(compareDateTime > dateTime){
            return 1;
        }else if(compareDateTime == dateTime){
            return 0;
        }else{
            return -1;
        }

    }

    public boolean isDatesBetween(String startDateString,String endDateString,
                                  String  startDateCompareString,String  endDateCompareString){
        boolean flag = false;
        boolean startFlag = (dateCompare(startDateString,startDateCompareString) < 1);
        boolean endFlag = (dateCompare(endDateString,endDateCompareString) > -1);
        if(startFlag && endFlag){
            flag = true;
        }
        return flag;
    }

    @RequestMapping("/selectShop")
    public WebResult selectShop(SysShop shop){
        List<SysShop> list =  impl.list();
        for(int i =0;i<list.size();i++){
            list.get(i).getSysBusinesstime();
        }
       return new WebResult();
    }
}

