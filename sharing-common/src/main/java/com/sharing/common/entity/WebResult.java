
package com.sharing.common.entity;

import com.sharing.common.exception.ErrorCode;
import com.sharing.common.utils.MessageUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 响应数据
 *
 * @author tecsmile@outlook.com
 * @since 1.0.0
 */
@ApiModel(value = "响应")
public class WebResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 编码：0表示成功，其他值表示失败
     */
    @ApiModelProperty(value = "编码：0表示成功，其他值表示失败")
    private int code = 0;
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String msg = "success";
    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据")
    private T data;

    public WebResult<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public boolean success(){
        return code == 0 ? true : false;
    }

    public WebResult<T> error() {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public WebResult<T> error(int code) {
        this.code = code;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public WebResult<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public WebResult<T> error(String msg) {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
