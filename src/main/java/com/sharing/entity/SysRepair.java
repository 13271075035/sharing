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
@ApiModel(value="SysRepair对象", description="")
public class SysRepair extends Model<SysRepair> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "rep_id", type = IdType.AUTO)
    private Integer repId;

    private Integer repRoomId;

    private LocalTime repTime;

    private String repStatu;


    public static final String REP_ID = "rep_id";

    public static final String REP_ROOM_ID = "rep_room_id";

    public static final String REP_TIME = "rep_time";

    public static final String REP_STATU = "rep_statu";

    @Override
    protected Serializable pkVal() {
        return this.repId;
    }

}
