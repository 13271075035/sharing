package com.sharing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lfh
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysUser对象", description="")
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户编号")
    @TableId(value = "sys_userId", type = IdType.AUTO)
    private Integer sysUserid;

    @ApiModelProperty(value = "用户名")
    @TableField("sys_userName")
    private String sysUsername;

    @ApiModelProperty(value = "用户头像")
    @TableField("sys_userImg")
    private String sysUserimg;

    @ApiModelProperty(value = "用户电话")
    @TableField("sys_userPhone")
    private String sysUserphone;

    @ApiModelProperty(value = "用户邮箱")
    @TableField("sys_userEmail")
    private String sysUseremail;

    @ApiModelProperty(value = "用户余额")
    @TableField("sys_userBalance")
    private Integer sysUserbalance;

    @ApiModelProperty(value = "备用字段")
    @TableField("spareFirst")
    private String spareFirst;

    @ApiModelProperty(value = "备用字段")
    @TableField("spareTwo")
    private String spareTwo;


    public static final String SYS_USERID = "sys_userId";

    public static final String SYS_USERNAME = "sys_userName";

    public static final String SYS_USERIMG = "sys_userImg";

    public static final String SYS_USERPHONE = "sys_userPhone";

    public static final String SYS_USEREMAIL = "sys_userEmail";

    public static final String SYS_USERBALANCE = "sys_userBalance";

    public static final String SPAREFIRST = "spareFirst";

    public static final String SPARETWO = "spareTwo";

    @Override
    protected Serializable pkVal() {
        return this.sysUserid;
    }

}
