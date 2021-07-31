package com.ssk.hfb.service;

import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.mapper.SignInMapper;
import com.ssk.hfb.pojo.SignIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SignInService {
    @Autowired
    private SignInMapper signInMapper;
    public List<SignIn> querySignLisyByUId(Integer uid){
        SignIn signIn = new SignIn();
        signIn.setUId(uid);
        List<SignIn> select = signInMapper.select(signIn);
        return select;
    }
    public Integer saveSignIn(SignIn signIn){
        SignIn signIn1 = signInMapper.selectOne(signIn);
        if (signIn1 == null){
            signIn.setCreateTime(new Date());
            int insert = signInMapper.insert(signIn);
            return insert;
        }else {
            throw new CommonAdviceException(ExceptioneEnum.SAVE_ERROR);
        }
    }

}
