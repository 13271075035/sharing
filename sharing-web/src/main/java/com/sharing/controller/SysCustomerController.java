package com.sharing.controller;

import com.sharing.common.entity.WebResult;
import com.sharing.dto.SysCustomerDto;
import com.sharing.service.SysCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("sys/sysCustomer")
public class SysCustomerController {

    @Autowired
    SysCustomerService sysCustomerService;
    @PostMapping("/login")
    public WebResult login(@RequestBody SysCustomerDto sysCustomerDto, HttpServletResponse response){

     
        return sysCustomerService.login(sysCustomerDto,response);


    }

}
