package com.sharing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalTime;
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
@ApiModel(value="SysCoupon对象", description="")
public class SysCoupon extends Model<SysCoupon> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cou_id", type = IdType.AUTO)
    private Integer couId;

    private Integer couUserId;

    private Integer couMoney;

    private Integer couCondition;

    private LocalTime couCreateTime;


    public static final String COU_ID = "cou_id";

    public static final String COU_USER_ID = "cou_user_id";

    public static final String COU_MONEY = "cou_money";

    public static final String COU_CONDITION = "cou_condition";

    public static final String COU_CREATE_TIME = "cou_create_time";

    @Override
    protected Serializable pkVal() {
        return this.couId;
    }

}
