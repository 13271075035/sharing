package com.sharing.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_customer")
public class SysCustomerEntity {
    @TableId
    private  Integer id;
    /*用户姓名*/
    private  String customerName;
    /*用户手机号 */
    private  String customerPhone;
    /*用户邮箱*/
    private  String  customerEmail;
    /*是否激活*/
    private  String  customerState;
    /*密码*/
    private String    password;
    /*创建日期*/
    private Date  createTime;
    /*更新日期*/
    private  Date updateTime;
    /*是否删除*/
    private  Integer isDelete;

}
