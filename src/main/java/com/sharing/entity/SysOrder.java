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
 * @since 2020-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysOrder对象", description="")
public class SysOrder extends Model<SysOrder> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    @TableId(value = "sys_orderId", type = IdType.AUTO)
    private Integer sysOrderid;

    @ApiModelProperty(value = "用户id")
    @TableField("sys_userId")
    private Integer sysUserid;

    @ApiModelProperty(value = "店铺编号")
    @TableField("sys_shopId")
    private Integer sysShopid;

    @ApiModelProperty(value = "房间id")
    @TableField("sys_roomId")
    private Integer sysRoomid;

    @ApiModelProperty(value = "房间预约日期")
    @TableField("sys_makeDate")
    private String sysMakedate;

    @ApiModelProperty(value = "房间预约时间")
    @TableField("sys_makeTime")
    private String sysMaketime;

    @ApiModelProperty(value = "房间使用时长")
    @TableField("sys_useTIme")
    private String sysUsetime;

    @ApiModelProperty(value = "支付金额")
    private Integer sysMoney;

    @ApiModelProperty(value = "支付方式")
    @TableField("sys_paymentMethod")
    private String sysPaymentmethod;

    @ApiModelProperty(value = "支付时间")
    @TableField("sys_paymentTime")
    private String sysPaymenttime;

    @TableField("spareFirst")
    private String spareFirst;

    @TableField("spareTwo")
    private String spareTwo;

    private Integer state;


    public static final String SYS_ORDERID = "sys_orderId";

    public static final String SYS_USERID = "sys_userId";

    public static final String SYS_SHOPID = "sys_shopId";

    public static final String SYS_ROOMID = "sys_roomId";

    public static final String SYS_MAKEDATE = "sys_makeDate";

    public static final String SYS_MAKETIME = "sys_makeTime";

    public static final String SYS_USETIME = "sys_useTIme";

    public static final String SYS_MONEY = "sys_money";

    public static final String SYS_PAYMENTMETHOD = "sys_paymentMethod";

    public static final String SYS_PAYMENTTIME = "sys_paymentTime";

    public static final String SPAREFIRST = "spareFirst";

    public static final String SPARETWO = "spareTwo";

    public static final String STATE = "state";

    @Override
    protected Serializable pkVal() {
        return this.sysOrderid;
    }

}
