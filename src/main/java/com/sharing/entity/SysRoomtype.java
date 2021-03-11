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
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysRoomtype对象", description="")
public class SysRoomtype extends Model<SysRoomtype> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "房间类型id")
    @TableId(value = "sys_roomTypeId", type = IdType.AUTO)
    private Integer sysRoomtypeid;

    @ApiModelProperty(value = "房间类型名称")
    @TableField("sys_roomTypeName")
    private String sysRoomtypename;

    @TableField("spareFirst")
    private String spareFirst;


    public static final String SYS_ROOMTYPEID = "sys_roomTypeId";

    public static final String SYS_ROOMTYPENAME = "sys_roomTypeName";

    public static final String SPAREFIRST = "spareFirst";

    @Override
    protected Serializable pkVal() {
        return this.sysRoomtypeid;
    }

}
