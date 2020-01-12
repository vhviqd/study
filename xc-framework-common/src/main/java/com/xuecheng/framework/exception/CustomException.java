package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;

/**
 * 自定义异常  ，继承RuntimeException 为了不报错
 */
@Data
public class CustomException extends RuntimeException {

    /**
     * 返回的代码
     */
    private ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }


}
