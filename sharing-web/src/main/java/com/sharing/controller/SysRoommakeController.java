package com.sharing.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharing.RabbitMQ.MqttGateway;
import com.sharing.common.entity.WebResult;
import com.sharing.entity.SysRoom;
import com.sharing.entity.SysRoommake;
import com.sharing.entity.SysShop;
import com.sharing.service.impl.SysRoomServiceImpl;
import com.sharing.service.impl.SysRoommakeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private SysRoomServiceImpl roomImpl;

    @Autowired
    private MqttGateway mqttGateway;
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
    @RequestMapping("/insertRoomMake")
    public WebResult insertRoomMake(@RequestBody SysRoommake room){
        return new WebResult().ok(impl.save(room));
    }

    @RequestMapping("/ScanUnlock")
    public WebResult ScanUnlock(@RequestBody  SysRoommake make){

        QueryWrapper<SysRoom> roomQuery = new QueryWrapper<SysRoom>();
        roomQuery.lambda().eq(SysRoom::getSysRoomid, make.getSysRoomid());
        List<SysRoom> roomList =  roomImpl.list(roomQuery);
        if(Integer.parseInt(roomList.get(0).getSpareTwo()) ==0){
            System.out.println("进来了");


            QueryWrapper<SysRoommake> query = new QueryWrapper<SysRoommake>();
            query.lambda().eq(SysRoommake::getSysUserid, make.getSysUserid())
                    .eq(SysRoommake::getSysMakedate, make.getSysMakedate()).eq(SysRoommake::getSysRoomid, make.getSysRoomid());
            List<SysRoommake> list =  impl.list(query);
            String[] busin = make.getSysMaketime().split(":");
            SysRoommake Roommake = new SysRoommake();

            for(int i =0;i<list.size();i++){

                String[] businesstime =  list.get(i).getSysMaketime().split("-");
                String[] statTime = businesstime[0].split(":");
                String[] endTIme = businesstime[1].split(":");

                boolean bool =  isEffectiveDate(new Date(2020,10,10,Integer.parseInt(busin[0]),Integer.parseInt(busin[1])),
                        new Date(2020,10,10,Integer.parseInt(statTime[0]),Integer.parseInt(statTime[1])),
                        new Date(2020,10,10,Integer.parseInt(endTIme[0]),Integer.parseInt(endTIme[1])));
                if(bool ==true){
                   Roommake = list.get(i);
                    //开锁
                    mqttGateway.sendToMqtt("$APP,UNLOCK*",make.getSpareTwo());
                    String[] maketime = Roommake.getSysMaketime().split("-");
                    String[] endtime = maketime[1].split(":");


                    /**
                     * 使用还剩五分钟
                     */
                    Date date =  subtractTime(new Date(2020,10,10,Integer.parseInt(endtime[0]),Integer.parseInt(endtime[1])),300000);
                    // 时间类
                    Calendar fiveDate = Calendar.getInstance();
                    //使用结束
                    fiveDate.set(fiveDate.get(Calendar.YEAR), fiveDate.get(Calendar.MONTH), fiveDate.get(Calendar.DATE),date.getHours() , date.getMinutes(), 0);
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        public void run() {
                            mqttGateway.sendToMqtt("$APP,LASTFIVE*",make.getSpareTwo());
                        }
                    }, fiveDate.getTime());


                    /**
                     * 使用结束
                     */
                    Calendar startDate = Calendar.getInstance();
                    //使用结束
                    startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE),Integer.parseInt(endtime[0]) , Integer.parseInt(endtime[1]), 0);
                    Timer ti = new Timer();
                    ti.schedule(new TimerTask() {
                        public void run() {
                            mqttGateway.sendToMqtt("$APP,TAKEOVER*",make.getSpareTwo());
                        }
                    }, startDate.getTime() );
                    System.out.println("已开锁，欢迎光临");
                    SysRoom nextroom = new SysRoom();
                    nextroom.setSysRoomid(make.getSysRoomid());
                    nextroom.setSpareTwo("1");
                    roomImpl.updateById(nextroom);
                    return new WebResult().ok("已开锁，欢迎光临");
                }
            }
            return new WebResult().ok("您还没有预约该房间");
        }else if(Integer.parseInt(roomList.get(0).getSpareTwo()) > 0){
            mqttGateway.sendToMqtt("$APP,LOCK*",make.getSpareTwo());
            SysRoom nextroom = new SysRoom();
            nextroom.setSysRoomid(make.getSysRoomid());
            nextroom.setSpareTwo("0");
            roomImpl.updateById(nextroom);
            return new WebResult().ok("已关锁，感谢光临，再见");
        }
        return new WebResult().error("您还没有预约该房间");
    }



    /**
     *  加减对应时间后的日期
     * @param date  需要加减时间的日期
     * @param amount    加减的时间(毫秒)
     * @return  加减对应时间后的日期
     */
    private Date subtractTime(Date date, int amount) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strTime = sdf.format(date.getTime() - amount);
            Date time = sdf.parse(strTime);
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

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

