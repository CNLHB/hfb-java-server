package com.ssk.hfb.controller;


import com.ssk.hfb.common.enums.SucessEnum;
import com.ssk.hfb.common.sucess.ResultSucess;
import com.ssk.hfb.pojo.User;
import com.ssk.hfb.service.AuthService;
import com.ssk.hfb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;

    /**
     *
     * @param token  请求认证，验证请求header中token是否合法
     * @return 成功返回新的token，失败返回错误提示
     */
    @GetMapping("verify")
    public ResponseEntity<ResultSucess<String,Object>> verify(@RequestHeader("Authorization") String token){
        String newToken = authService.verifyToken(token);
        System.out.println(newToken);
        int uId = authService.getUId(token);
        User user = userService.searchUserById(uId);
        Map<String, Object> map = new HashMap<>();
        map.put("token",newToken);
        map.put("userInfo",user);

        ResultSucess<String,Object> resultSucess = new ResultSucess(SucessEnum.TOKEN_VERIFY_SUCESS,map);
        return ResponseEntity.ok(resultSucess);
    }

}
