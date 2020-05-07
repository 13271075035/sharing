package com.sharing.service;

import com.sharing.common.entity.WebResult;
import com.sharing.common.service.CrudService;
import com.sharing.dto.SysCustomerDto;
import com.sharing.entity.SysCustomerEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: xiang
 * @Date: 2020/5/6 18:30
 * @Description:
 */

public interface SysCustomerService extends CrudService<SysCustomerEntity, SysCustomerDto> {

    WebResult login(SysCustomerDto sysCustomerDto, HttpServletResponse response);
}