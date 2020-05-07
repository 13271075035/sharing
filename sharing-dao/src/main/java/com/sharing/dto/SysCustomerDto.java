package com.sharing.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SysCustomerDto {
    /*用户姓名*/
    private  String customerName;
    /*用户手机号 */
    private  String customerPhone;
    /*用户邮箱*/
    private  String  customerEmail;
     /*密码*/
    private String    password;
    /*创建日期*/
    private Date createTime;
    /*更新日期*/
    private  Date updateTime;

}
