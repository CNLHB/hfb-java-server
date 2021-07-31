package com.ssk.hfb.common.result;

import com.ssk.hfb.common.enums.ExceptioneEnum;
import lombok.Data;

@Data
public class ResultException {
    public Integer code;
    public Integer status;
    public String message;
    public long timestamp;
    public String data = "";
    public ResultException(ExceptioneEnum en){
        this.code = en.getCode();
        this.status = en.getStatus();
        this.message = en.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
