package com.sharing.common.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by ASUS on 2019/7/23.
 */
@Data
public class UserDetail implements Serializable {
    private static final long serialVersionUID = -902069938507991740L;
    private Integer userId;
    /**
     * 登录账号
     */
    private String loginAccount;
    /**
     * 登录密码
     */
    private String loginPassword;
    /**
     * 支行名
     */
    private String branchName;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 联系电话
     */
    private String phone;

    /**
     * 用户状态（1：停用，0：正常）
     */
    private Integer userState;
    /**
     * 角色名称
     */
    private String userRoleName;
    /**
     * 角色id
     */
    private Integer userRoleId;
}
