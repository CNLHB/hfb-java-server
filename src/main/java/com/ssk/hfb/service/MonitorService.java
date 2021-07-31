package com.ssk.hfb.service;

import com.ssk.hfb.common.pojo.MonitorInfo;
import com.ssk.hfb.mapper.MonitorInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MonitorService {
    @Autowired
    private MonitorInfoMapper monitorInfoMapper;

    public void saveMonitorInfo(MonitorInfo monitorInfo){
        monitorInfo.setCreateTime(new Date());
        int insert = monitorInfoMapper.insert(monitorInfo);

    }
    public void deleteMonitorInfo(List<Long> id){
        int insert = monitorInfoMapper.deleteByIdList(id);

    }
//    "kind": "stability",//大类   kind: 'experience',//用户体验指标
//
//
//    "type": "error",//小类   type: 'xhr', type: 'paint',//统计每个阶段的时间  type: 'blank',
//  type: 'timing',//统计每个阶段的时间 type: 'firstInputDelay',//首次输入延迟
//    "errorType": "promiseError",//错误类型  eventType: type,//load error abort

    public List<MonitorInfo> queryMonitorInfo(String key){
        Example example = new Example(MonitorInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(key,key);
//        String orderByClause = "send_time" + " ASC" ;
//        example.setOrderByClause(orderByClause);
        List<MonitorInfo> monitorInfos = monitorInfoMapper.selectByExample(example);
        return monitorInfos;
    }

    public List<Map<String, Object>> queryUrlCountList() {
        List<Map<String, Object>> maps = monitorInfoMapper.queryUrlCountList();
        return maps;
    }



    public List<Map<String, Object>> queryPageByWeekPerforMance(int day ,String url) {
        List<Map<String, Object>> maps = monitorInfoMapper.queryPageByWeekPerforMance(day, url);
        return maps;

    }

    public List<Map<String, Object>> queryDayHourAvgPagePerforMance(String day ,String url) {

        List<Map<String, Object>> maps = monitorInfoMapper.queryDayHourAvgPagePerforMance(day, url);
        return maps;
    }

    public List<Map<String, Object>> queryDayPagePerforMance(String type,String day ,String url) {
        if ("week".equals(type)){
            List<Map<String, Object>> maps = monitorInfoMapper.queryWeekOrMonthPerforMance(7,url);
            return maps;
        }
        if ("month".equals(type)){
            List<Map<String, Object>> maps = monitorInfoMapper.queryWeekOrMonthPerforMance(30,url);
            return maps;
        }
        System.out.println(1);
        List<Map<String, Object>> maps = monitorInfoMapper.queryDayPagePerforMance(day, url);
        return maps;
    }

    public List<Map<String, Object>> queryErrorUrlCountList() {
        List<Map<String, Object>> maps = monitorInfoMapper.queryErrorUrlCountList();
        return maps;
    }

    public List<Map<String, Object>> queryErrorCountList(String type, String day,String url) {
        if ("day".equals(type)){
            List<Map<String, Object>> maps = monitorInfoMapper.queryErrorCountList(10,day,url);
            return maps;
        }
        if ("month".equals(type)){
            day= day.substring(0,7);
            System.out.println(day);
            List<Map<String, Object>> maps = monitorInfoMapper.queryErrorCountList(7,day,url);
            return maps;
        }
        if ("year".equals(type)){
            day= day.substring(0,4);
            System.out.println(day);
            List<Map<String, Object>> maps = monitorInfoMapper.queryErrorCountList(4,day,url);
            return maps;
        }
        if ("week".equals(type)){
            List<Map<String, Object>> maps = monitorInfoMapper.queryWeekError(url);
            return maps;
        }
        return new ArrayList<>();
    }

    public List<Map<String, Object>> queryErrorUrlList(String type, String day,String url) {
        if ("day".equals(type)){
            List<Map<String, Object>> maps = monitorInfoMapper.queryErrorUrlList(10,day,url);
            return maps;
        }
        if ("month".equals(type)){
            day= day.substring(0,7);
            List<Map<String, Object>> maps = monitorInfoMapper.queryErrorUrlList(7,day,url);

            return maps;
        }
        if ("year".equals(type)){
            day= day.substring(0,4);
            List<Map<String, Object>> maps = monitorInfoMapper.queryErrorUrlList(4,day,url);
            return maps;
        }
        if ("week".equals(type)){
            List<Map<String, Object>> maps = monitorInfoMapper.queryWeekErrorUrlList(url);
            return maps;
        }
        return new ArrayList<>();
    }

    public List<Map<String, Object>> queryByDayError( String day, String url) {
        List<Map<String, Object>> maps = monitorInfoMapper.queryByDayError(day, url);
        return maps;
    }

    public List<Map<String, Object>> queryByWeekError( String type,Integer day, String url) {
        if ("year".equals(type)){
            List<Map<String, Object>> maps = monitorInfoMapper.queryYearErrorCount(String.valueOf(day), url);
            return maps;
        }
        List<Map<String, Object>> maps = monitorInfoMapper.queryByWeekError(day, url);
        return maps;
    }
}
