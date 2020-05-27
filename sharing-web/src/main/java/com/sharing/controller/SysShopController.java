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
import java.util.Calendar;
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
        List<SysShop> zList = new ArrayList<>();
        for(int i = 0;i<list.size();i++){
            String[] splitShop = list.get(i).getSysBusinesstime().split("-");
            String[] splitTimeslot = shop.getSysBusinesstime().split("-");
            boolean bool = isDatesBetween(splitTimeslot[0],splitTimeslot[1],splitShop[0],splitShop[1]);
            if(bool==true){
             zList.add(list.get(i));
            }
        }
        System.out.println(JSON.toJSONString(zList));
        return new WebResult().ok(zList);
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
    public WebResult selectShop(@RequestBody  SysShop shop){
        List<SysShop> list =  impl.list();
        System.out.println(JSON.toJSONString(shop));
        String[] busin = shop.getSysBusinesstime().split(":");
        List<SysShop> listshop = new ArrayList<SysShop>();
        for(int i =0;i<list.size();i++){
           String[] businesstime =  list.get(i).getSysBusinesstime().split("-");
           String[] statTime = businesstime[0].split(":");
           String[] endTIme = businesstime[1].split(":");


           boolean bool =  isEffectiveDate(new Date(2020,10,10,Integer.parseInt(busin[0]),Integer.parseInt(busin[1])),
                    new Date(2020,10,10,Integer.parseInt(statTime[0]),Integer.parseInt(statTime[1])),
                    new Date(2020,10,10,Integer.parseInt(endTIme[0]),Integer.parseInt(endTIme[1])));
           if(bool==true){
               listshop.add(list.get(i));
           }
        }
       return new WebResult().ok(listshop);
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}

