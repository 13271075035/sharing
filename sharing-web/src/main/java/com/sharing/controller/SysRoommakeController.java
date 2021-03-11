package com.sharing.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharing.RabbitMQ.MqttGateway;
import com.sharing.common.entity.WebResult;
import com.sharing.entity.SysOrder;
import com.sharing.entity.SysRoom;
import com.sharing.entity.SysRoommake;
import com.sharing.entity.SysShop;
import com.sharing.service.impl.SysOrderServiceImpl;
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
    private SysOrderServiceImpl orderImpl;

    @Autowired
    private MqttGateway mqttGateway;
    @RequestMapping("/roomMake")
    public WebResult roomMake(@RequestBody SysRoommake room){
        QueryWrapper<SysRoommake> query = new QueryWrapper<SysRoommake>();
       query.lambda().eq(SysRoommake::getSysMakedate, room.getSysMakedate()).eq(SysRoommake::getSysShopid,room.getSysShopid());
        return new WebResult().ok(impl.list(query));
    }

    @RequestMapping("/roomMakeRoomid")
    public WebResult roomMakeRoomid(@RequestBody SysRoommake room){
        QueryWrapper<SysRoommake> query = new QueryWrapper<SysRoommake>();
        query.lambda().eq(SysRoommake::getSysMakedate, room.getSysMakedate()).eq(SysRoommake::getSysRoomid,room.getSysRoomid());
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
        Date now  = new Date();
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
                String[] makedate = list.get(i).getSysMakedate().split("/");

                boolean bool =  isEffectiveDate(
                        new Date(now.getYear(),now.getMonth(),
                                now.getDate(),Integer.parseInt(busin[0]),Integer.parseInt(busin[1]),Integer.parseInt(busin[2])),
                        new Date(now.getYear(),Integer.parseInt(makedate[1])-1,
                                Integer.parseInt(makedate[2]),Integer.parseInt(statTime[0]),Integer.parseInt(statTime[1])),
                        new Date(now.getYear(),Integer.parseInt(makedate[1])-1,
                                Integer.parseInt(makedate[2]),Integer.parseInt(endTIme[0]),Integer.parseInt(endTIme[1])));
                if(bool ==true){
                   Roommake = list.get(i);
                    //开锁
                    mqttGateway.sendToMqtt("$APP,UNLOCK*",make.getSpareTwo());
                    String[] maketime = Roommake.getSysMaketime().split("-");
                    String[] endtime = maketime[1].split(":");
                    String[] starttime = maketime[0].split(":");


                    /**
                     * 使用还剩五分钟
                     */
                    Calendar fiveDate = Calendar.getInstance();
                    Date date =  subtractTime(new Date(Integer.parseInt(makedate[0]),(Integer.parseInt(makedate[1])-1),
                            Integer.parseInt(makedate[2]),Integer.parseInt(endtime[0]),Integer.parseInt(endtime[1])),300000);
                    fiveDate.set(date.getYear(), date.getMonth(), date.getDate(),date.getHours() , date.getMinutes(), 0);
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        public void run() {
                            mqttGateway.sendToMqtt("$APP,LASTFIVE*",make.getSpareTwo());
                        }
                    }, fiveDate.getTime());
                    System.out.println(fiveDate.getTime());


                    /**
                     * 使用结束
                     */
                    Calendar startDate = Calendar.getInstance();

                    startDate.set(date.getYear(), date.getMonth(), date.getDate(),Integer.parseInt(endtime[0]) , Integer.parseInt(endtime[1]), 0);
                    Timer ti = new Timer();
                    ti.schedule(new TimerTask() {
                        public void run() {
                            mqttGateway.sendToMqtt("$APP,TAKEOVER*",make.getSpareTwo());
                        }
                    }, startDate.getTime() );
                    System.out.println(startDate.getTime());
                    System.out.println("已开锁，欢迎光临");
                    SysRoom nextroom = new SysRoom();
                    nextroom.setSysRoomid(make.getSysRoomid());
                    nextroom.setSpareTwo("1");
                    roomImpl.updateById(nextroom);
                    Date orderTime = new Date(now.getYear(),now.getMonth(),
                            now.getDate(),Integer.parseInt(busin[0]),Integer.parseInt(busin[1]),Integer.parseInt(busin[2]));
                    Date endTime = new Date(now.getYear(),Integer.parseInt(makedate[1])-1,
                            Integer.parseInt(makedate[2]),Integer.parseInt(endTIme[0]),Integer.parseInt(endTIme[1]));

                    Map data = new HashMap();
                    data.put("message","已开锁，欢迎光临");
                    data.put("sysRoomname",roomList.get(0).getSysRoomname());
                    data.put("sysRoomId",roomList.get(0).getSysRoomid());
                    data.put("sysShopid",roomList.get(0).getSysShopid());
                    data.put("endTime",endTime);
                    data.put("orderTime",orderTime);
                    data.put("useTime",list.get(i).getSysUsetime());
                    return new WebResult().ok(data);
                }
            }
            return new WebResult().ok("您还没有预约该房间");
        }else if(Integer.parseInt(roomList.get(0).getSpareTwo()) > 0){
            System.out.println("--------------");
            mqttGateway.sendToMqtt("$APP,LOCK*",make.getSpareTwo());
            SysRoom nextroom = new SysRoom();
            nextroom.setSysRoomid(make.getSysRoomid());
            nextroom.setSpareTwo("0");
            roomImpl.updateById(nextroom);
            SysOrder order = new SysOrder();
            order.setState(1);
            order.setSysOrderid(make.getSysRoommakeid());
            orderImpl.updateById(order);
            return new WebResult().ok("已关锁，感谢光临，再见");
        }
        return new WebResult().error("您还没有预约该房间");
    }



    /**
     *  加减对应时间后的日期
     * @param date  需要加减时间的日期
     * @param amount    加减的时间(毫秒)
     * @return  加减insertRoomMake对应时间后的日期
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

