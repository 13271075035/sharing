package com.sharing.controller;

import com.sharing.common.entity.WebResult;
import com.sharing.dto.SysCustomerDto;
import com.sharing.service.SysCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysCustomerController {

    @Autowired
    SysCustomerService sysCustomerService;
    public WebResult login(){

        SysCustomerDto sysCustomerDto = sysCustomerService.get((long) 1);
        System.out.println(sysCustomerDto);
        return  null;


    }

}
