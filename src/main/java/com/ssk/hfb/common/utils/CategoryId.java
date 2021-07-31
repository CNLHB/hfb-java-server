package com.ssk.hfb.common.utils;

import java.util.HashMap;
import java.util.Map;

public  class  CategoryId {
    public static Map<String,Integer> cid = new HashMap<>();
    public static Integer getCid(String key){
        cid.put("hanfu",1);
        cid.put("yule",2);
        cid.put("ershou",3);
        cid.put("zhoubian",4);
        try{
            return cid.get(key);
        }catch (Exception e){
            return 5;
        }

    }
}
