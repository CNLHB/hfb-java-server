package com.ssk.hfb.common.sucess;

import com.ssk.hfb.common.enums.SucessEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultSucess<T,V> {
    public Integer code;
    public Integer status;
    public String message;
    public Map<T,V> data = new HashMap();
    public ResultSucess(SucessEnum en, Map<T,V> map){
        this.code = en.getCode();
        this.status = en.getStatus();
        this.message = en.getMsg();
        this.data = map;
    }
    public ResultSucess(SucessEnum en){
        this.code = en.getCode();
        this.status = en.getStatus();
        this.message = en.getMsg();
    }
}
