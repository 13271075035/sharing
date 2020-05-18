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
 * @since 2020-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysRoom对象", description="")
public class SysRoom extends Model<SysRoom> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "房间id")
    @TableId(value = "sys_roomId", type = IdType.AUTO)
    private Integer sysRoomid;

    @ApiModelProperty(value = "房间名称")
    @TableField("sys_roomName")
    private String sysRoomname;

    @ApiModelProperty(value = "房间类型")
    @TableField("sys_roomType")
    private Integer sysRoomtype;

    @ApiModelProperty(value = "是否有WIFI")
    @TableField("sys_WIFI")
    private Boolean sysWifi;

    @ApiModelProperty(value = "是否有USB充电")
    @TableField("sys_USB")
    private Boolean sysUsb;

    @ApiModelProperty(value = "平面图")
    private String sysPlan;

    @ApiModelProperty(value = "房间单价")
    private String sysPrice;

    @TableField("spareFirst")
    private String spareFirst;

    @TableField("spareTwo")
    private String spareTwo;

    @ApiModelProperty(value = "店铺id")
    @TableField("sys_shopId")
    private Integer sysShopid;


    public static final String SYS_ROOMID = "sys_roomId";

    public static final String SYS_ROOMNAME = "sys_roomName";

    public static final String SYS_ROOMTYPE = "sys_roomType";

    public static final String SYS_WIFI = "sys_WIFI";

    public static final String SYS_USB = "sys_USB";

    public static final String SYS_PLAN = "sys_plan";

    public static final String SYS_PRICE = "sys_price";

    public static final String SPAREFIRST = "spareFirst";

    public static final String SPARETWO = "spareTwo";

    public static final String SYS_SHOPID = "sys_shopId";

    @Override
    protected Serializable pkVal() {
        return this.sysRoomid;
    }

}
