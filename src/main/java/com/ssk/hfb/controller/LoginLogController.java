package com.ssk.hfb.controller;


import com.ssk.hfb.common.enums.SucessEnum;
import com.ssk.hfb.common.sucess.ResultSucess;
import com.ssk.hfb.common.vo.PageResult;
import com.ssk.hfb.pojo.User;
import com.ssk.hfb.service.AuthService;
import com.ssk.hfb.service.LoginLogService;
import com.ssk.hfb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("login/log")
public class LoginLogController {
    @Autowired
    LoginLogService loginLogService;

    /**
     * 获取登录日志
     * @param page 当前页
     * @param rows 一页条数
     * @return
     */
    @GetMapping
    public ResponseEntity<PageResult> queryLogList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows){
        PageResult pageResult = loginLogService.queryLogList(page, rows);
        return ResponseEntity.ok(pageResult);
    }



}
