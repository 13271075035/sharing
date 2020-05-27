package com.sharing.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sharing.common.entity.WebResult;
import com.sharing.entity.SysFire;
import com.sharing.entity.SysRoom;
import com.sharing.service.impl.SysFireServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lfh
 * @since 2020-05-25
 */
@RestController
@RequestMapping("/sysFire")
public class SysFireController {

    @Autowired
    private SysFireServiceImpl impl;

    @GetMapping("/selectAll")
    private WebResult selectFire(Integer currentPage,Integer myts){

        if(currentPage == null){
            currentPage = 1;
        }
        if(myts == null){
            myts =10;
        }
        QueryWrapper<SysFire> query = new QueryWrapper<SysFire>();
        query.lambda().orderByDesc(SysFire::getSysFire);
        Page<SysFire> page = new Page<>(currentPage, myts);

        return  new WebResult().ok(impl.page(page,query));
    }
}

