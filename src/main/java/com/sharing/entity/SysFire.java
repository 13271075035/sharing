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
@ApiModel(value="SysFire对象", description="")
public class SysFire extends Model<SysFire> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sys_fire", type = IdType.AUTO)
    private Integer sysFire;

    private String sysEquipmentName;

    private String sysDeviceAddress;

    private Integer sysState;

    @TableField("spareFirst")
    private String spareFirst;

    @TableField("spareTwo")
    private String spareTwo;


    public static final String SYS_FIRE = "sys_fire";

    public static final String SYS_EQUIPMENT_NAME = "sys_equipment_name";

    public static final String SYS_DEVICE_ADDRESS = "sys_device_address";

    public static final String SYS_STATE = "sys_state";

    public static final String SPAREFIRST = "spareFirst";

    public static final String SPARETWO = "spareTwo";

    @Override
    protected Serializable pkVal() {
        return this.sysFire;
    }

}
