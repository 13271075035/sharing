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
@ApiModel(value="SysShop对象", description="")
public class SysShop extends Model<SysShop> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺编号")
    @TableId(value = "sys_shopId", type = IdType.AUTO)
    private Integer sysShopid;

    @ApiModelProperty(value = "店铺名称")
    @TableField("sys_shopName")
    private String sysShopname;

    @ApiModelProperty(value = "店铺经度坐标")
    @TableField("sys_coordinateX")
    private String sysCoordinatex;

    @ApiModelProperty(value = "店铺纬度坐标")
    @TableField("sys_coordinateY")
    private String sysCoordinatey;

    @ApiModelProperty(value = "店铺营业时间")
    @TableField("sys_businessTime")
    private String sysBusinesstime;

    @ApiModelProperty(value = "店铺是否正常营业 0代表营业")
    private Integer sysBusiness;

    @TableField("spareFirst")
    private String spareFirst;

    @TableField("spareTwo")
    private String spareTwo;


    public static final String SYS_SHOPID = "sys_shopId";

    public static final String SYS_SHOPNAME = "sys_shopName";

    public static final String SYS_COORDINATEX = "sys_coordinateX";

    public static final String SYS_COORDINATEY = "sys_coordinateY";

    public static final String SYS_BUSINESSTIME = "sys_businessTime";

    public static final String SYS_BUSINESS = "sys_business";

    public static final String SPAREFIRST = "spareFirst";

    public static final String SPARETWO = "spareTwo";

    @Override
    protected Serializable pkVal() {
        return this.sysShopid;
    }

}
