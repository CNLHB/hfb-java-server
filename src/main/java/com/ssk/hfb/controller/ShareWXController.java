package com.ssk.hfb.controller;

import com.ssk.hfb.pojo.WinXinEntity;
import com.ssk.hfb.utils.WXUnitl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@RestController
@Slf4j
@RequestMapping("user")
public class ShareWXController {
    /**
     * wx分享，未实现
     * @param request
     * @return
     */
    @GetMapping("share")
    public Map<String, Object> sgture(HttpServletRequest request) {
        String strUrl=request.getParameter("url");
        log.info("===前====url========="+strUrl);
        WinXinEntity wx = WXUnitl.getWinXinEntity(strUrl);
        // 将wx的信息到给页面
        Map<String, Object> map = new HashMap<String, Object>();
        String sgture = WXUnitl.getSignature(wx.getTicket(), wx.getNoncestr(), wx.getTimestamp(), strUrl);
        map.put("sgture", sgture.trim());//签名
        map.put("timestamp", wx.getTimestamp().trim());//时间戳
        map.put("noncestr",  wx.getNoncestr().trim());//随即串
        map.put("appid","wx8e447db804ea7e3c");//你的公众号APPID
        return map;
    }

}
