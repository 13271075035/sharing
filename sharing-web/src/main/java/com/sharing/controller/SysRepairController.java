package com.sharing.controller;




import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharing.common.entity.WebResult;
import com.sharing.entity.SysRepair;

import com.sharing.service.impl.SysRepairServiceImpl;
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
 * @since 2021-03-09
 */
@Controller
@RequestMapping("/sysRepair")
public class SysRepairController {

    @Autowired
    private SysRepairServiceImpl impl;

    @RequestMapping("/insertRepair")
    public WebResult insertRepair(@RequestBody SysRepair rep) {
        return new WebResult().ok(impl.save(rep));
    }
}

