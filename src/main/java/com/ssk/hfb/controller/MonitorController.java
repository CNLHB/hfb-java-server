package com.ssk.hfb.controller;

import com.ssk.hfb.common.pojo.MonitorInfo;
import com.ssk.hfb.service.MonitorService;
import com.ssk.hfb.utils.AddressUtils;
import com.ssk.hfb.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("monitor")
public class MonitorController {
    @Autowired
    private MonitorService monitorService;

    /**
     * 保存一个监控指标
     * @param monitorInfo
     * @param request
     */
    @PostMapping
    public void saveMonitor(@RequestBody MonitorInfo monitorInfo, HttpServletRequest request){
        String ipAddress = AddressUtils.getIpAddress(request);
        String adress = AddressUtils.getAdress(ipAddress);
        int uId = UserContext.getId();
        monitorInfo.setUserIp(ipAddress);
        monitorInfo.setUserAdress(adress);
        if (uId!=-1){
            monitorInfo.setUId(Long.valueOf(uId));
        }
        monitorService.saveMonitorInfo(monitorInfo);
    }
    @GetMapping("url")
    public List<Map<String, Object>> queryUrlCountList(){
        List<Map<String, Object>> maps = monitorService.queryUrlCountList();
        return maps;
    }
    @GetMapping("per/oline/long")
    public  List<Map<String, Object>>  queryPageByWeekPerforMance(
            @RequestParam(value = "day") Integer day,
            @RequestParam(value = "url") String url){
        List<Map<String, Object>> maps = monitorService.queryPageByWeekPerforMance(day,url);
        return maps;
    }
    @GetMapping("per/oline/day")
    public  List<Map<String, Object>>  queryDayHourAvgPagePerforMance(
            @RequestParam(value = "day") String day,
            @RequestParam(value = "url") String url
    ){
        List<Map<String, Object>> maps = monitorService.queryDayHourAvgPagePerforMance(day, url);
        return maps;
    }
    @GetMapping("per/oline/current")
    public  List<Map<String, Object>>  queryDayPagePerforMance(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "day") String day,
            @RequestParam(value = "url") String url
    ){
        List<Map<String, Object>> maps = monitorService.queryDayPagePerforMance(type,day, url);
        return maps;
    }
    @GetMapping("error/url")
    public  List<Map<String, Object>>  queryErrorUrlCountList(){
        List<Map<String, Object>> maps = monitorService.queryErrorUrlCountList();
        return maps;
    }
    @GetMapping("error/count/list")
    public  List<Map<String, Object>>  queryErrorCountList(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "day") String day,
            @RequestParam(value = "url") String url
    ){
        List<Map<String, Object>> maps = monitorService.queryErrorCountList(type, day,url);
        return maps;
    }
    @GetMapping("error/url/list")
    public  List<Map<String, Object>>  queryErrorUrlList(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "day") String day,
            @RequestParam(value = "url") String url
    ){
        List<Map<String, Object>> maps = monitorService.queryErrorUrlList(type, day,url);
        return maps;
    }
    @GetMapping("error/online/day")
    public  List<Map<String, Object>>  queryByDayError(
            @RequestParam(value = "day") String day,
            @RequestParam(value = "url") String url
    ){
        List<Map<String, Object>> maps = monitorService.queryByDayError( day,url);
        return maps;
    }
    @GetMapping("error/online/long")
    public  List<Map<String, Object>>  queryByWeekError(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "day") Integer day,
            @RequestParam(value = "url") String url
    ){
        List<Map<String, Object>> maps = monitorService.queryByWeekError( type,day,url);
        return maps;
    }
}
