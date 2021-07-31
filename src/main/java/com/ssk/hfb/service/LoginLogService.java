package com.ssk.hfb.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssk.hfb.common.vo.PageResult;
import com.ssk.hfb.mapper.AccessMapper;
import com.ssk.hfb.mapper.LoginLogMapper;
import com.ssk.hfb.pojo.LoginLog;
import com.ssk.hfb.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
@Slf4j
public class LoginLogService {
    @Autowired
    private LoginLogMapper loginLogMapper;
   public void save(LoginLog loginLog){
       int i = loginLogMapper.insertSelective(loginLog);
   }
   public PageResult queryLogList(Integer page, Integer rows){
       PageHelper.startPage(page, rows);
       Example example = new Example(User.class);
       Example.Criteria criteria = example.createCriteria();
       String orderByClause = "create_time" + " DESC" ;
       example.setOrderByClause(orderByClause);
       List<LoginLog> loginLogs = loginLogMapper.selectByExample(example);
       if (CollectionUtils.isEmpty(loginLogs)) {
           return new PageResult<>();
       }
       PageInfo<LoginLog> info = new PageInfo<>(loginLogs);
       return new PageResult<>(info.getTotal(),info.getPageNum(), loginLogs);
   }


}
