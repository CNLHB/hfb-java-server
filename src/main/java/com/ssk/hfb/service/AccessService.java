package com.ssk.hfb.service;

import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.common.pojo.UserInfo;
import com.ssk.hfb.common.utils.JwtUtils;
import com.ssk.hfb.common.utils.RsaUtils;
import com.ssk.hfb.mapper.AccessMapper;
import com.ssk.hfb.pojo.Access;
import com.ssk.hfb.pojo.User;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@Service
@Slf4j
public class AccessService {
    @Autowired
    private AccessMapper accessMapper;
    public String forMatDate(){
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH); //  System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
        int month =(now.get(Calendar.MONTH) + 1);//  System.out.println("月: " + now.get(Calendar.MONTH));
        int year = now.get(Calendar.YEAR); //  System.out.println("年: " + now.get(Calendar.DAY_OF_MONTH));
        String date = String.valueOf(year)+String.valueOf(month)+String.valueOf(day);
        return date;
    }
    public Boolean saveAccess(Access access){
        String date = forMatDate();
        access.setDay(date);
        Access access1 = accessMapper.selectOne(access);
        if (access1!=null){
            return false;
        }
        access.setCreateTime(new Date());
        int insert = accessMapper.insert(access);
        if (insert==0){
            throw new CommonAdviceException(ExceptioneEnum.SAVE_ERROR);
        }
        return  true;
    }
    public Map<String, Integer> queryAccess(Integer uid){
        String date = forMatDate();
        Access access = new Access();
        access.setToId(uid);
        int allAcc = accessMapper.selectCount(access);
        access.setDay(date);
        int dayAcc = accessMapper.selectCount(access);
        Map<String, Integer> map = new HashMap<>();
        map.put("allAcc", allAcc);
        map.put("dayAcc", dayAcc);
        return  map;
    }


}
