package com.ssk.hfb.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.common.pojo.InfoNum;
import com.ssk.hfb.common.utils.JsonUtils;
import com.ssk.hfb.common.vo.PageResult;
import com.ssk.hfb.mapper.TopicTitleMapper;
import com.ssk.hfb.pojo.TitleClass;
import com.ssk.hfb.pojo.TopicTitle;
import com.ssk.hfb.pojo.TopicTitleClass;
import com.ssk.hfb.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class TopicTitleService {
    @Autowired
    private TopicTitleMapper topicTitleMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private TopicTitleClassService topicTitleClassService;
    @Autowired
    private TitleClsssService titleClsssService;
    public PageResult<TopicTitle> queryTopicTilteList(Integer page, Integer rows, Integer cId, String search){
        System.out.println(page);
        PageHelper.startPage(page, rows);
        Example example = new Example(TopicTitle.class);
        Example.Criteria criteria = example.createCriteria();
        if (cId!=null&&cId==1){

        }else {
            criteria.andEqualTo("cId",cId);
        }
        //关键字过滤
        if (StringUtils.isNoneBlank(search)){
            criteria.orLike("title", "%" + search + "%");
            criteria.orLike("description", "%" + search + "%");
        }
        // 排序
        String orderByClause = "create_time" + " DESC" ;
        example.setOrderByClause(orderByClause);
        criteria.andEqualTo("display",1);
        List<TopicTitle> list = topicTitleMapper.selectByExample(example);
        PageInfo<TopicTitle> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),info.getPageNum(), list);
    }
    public PageResult<Map> queryTopicTilteListV2(Integer page, Integer rows, Integer cId, String search){
        System.out.println(page);
        PageHelper.startPage(page, rows);
        Example example = new Example(TopicTitle.class);
        Example.Criteria criteria = example.createCriteria();
        if (cId==null){

        }else {
            criteria.andEqualTo("cId",cId);
        }
        //关键字过滤
        if (StringUtils.isNoneBlank(search)){
            criteria.orLike("title", "%" + search + "%");
            criteria.orLike("description", "%" + search + "%");
        }
        // 排序
        String orderByClause = "create_time" + " DESC" ;
        example.setOrderByClause(orderByClause);
        List<TopicTitle> list = topicTitleMapper.selectByExample(example);
        List<Map> process = process(list);
        PageInfo<TopicTitle> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),info.getPageNum(), process);
    }

    public void saveTopicTilte(TopicTitle title) {
        title.setCreateTime(new Date());
        title.setDisplay(true);
        title.setTotal(0);
        int insert = topicTitleMapper.insert(title);
        if (insert == 0){
            throw new CommonAdviceException(ExceptioneEnum.SAVE_ERROR);
        }
    }

    public TopicTitle queryTopicTilteInfoById(Integer id) {
        TopicTitle topicTitle = new TopicTitle();
        topicTitle.setId(id);
        TopicTitle topicTitle1 = topicTitleMapper.selectByPrimaryKey(topicTitle);
        if (topicTitle1==null){

        }
        return topicTitle1;
    }


    public void queryTopicTilteListById(Integer id, int uId) {
        TitleClass titleClass = new TitleClass();
        titleClass.setCId(id);
    }

    public  List<TopicTitle> queryTopicTilteListByUId(Integer id) {
        TopicTitle topicTitle = new TopicTitle();
        topicTitle.setId(id);
        List<TopicTitle> list = topicTitleMapper.select(topicTitle);
        if (CollectionUtils.isEmpty(list)){
            throw new CommonAdviceException(ExceptioneEnum.QUERY_RESULT_EMPTY);
        }
        return list;
    }

    public  List<TopicTitle> queryListByIds(List<Long> listId) {
        List<TopicTitle> topicTitles = topicTitleMapper.selectByIdList(listId);
        return topicTitles;

    }

    public List<TopicTitle> queryTopicTilteListByTId(Integer id) {

        return null;
    }

    public TopicTitle queryTitleByTid(Integer tId) {
        System.out.println(tId);
        System.out.println("+========================");
        List<TopicTitle> topicTitles = titleClsssService.queryList(tId);
        if (CollectionUtils.isEmpty(topicTitles)){
            return  new TopicTitle();
        }
        TopicTitle topicTitle = null;
        for (TopicTitle t: topicTitles){
            topicTitle =  t;
        }
        return topicTitle;
    }

    public void freezeTopicList(List<Integer> ids, Boolean freeze) {
        for(Integer id:ids){
            TopicTitle topicTitle = new TopicTitle();
            topicTitle.setId(id);
            topicTitle.setDisplay(freeze);
            int i = topicTitleMapper.updateByPrimaryKeySelective(topicTitle);
        }
    }
    public List<Map> process( List<TopicTitle> list){
        List<Map> listMap = new ArrayList<>();
        for (TopicTitle t: list){
            User user = userService.searchUserById(t.getUId());
            TopicTitleClass topicTitleClass = topicTitleClassService.searchTopicById(t.getCId());
            String s = JsonUtils.toString(t);
            Map<String, Object> map = JsonUtils.toMap(s, String.class, Object.class);
            map.put("uName", user.getUserName());
            map.put("cName", topicTitleClass.getTitle());
            listMap.add(map);
        }
        return listMap;
    }

    public Map selTopicTitleIncreaseTrend() {
        int i = topicTitleMapper.queryTopicCount();
        List<Map<String, Object>> weekList = topicTitleMapper.queryTopicCountWeek();
        List<Map<String, Object>> monthList = topicTitleMapper.queryTopicCountMonth();
        List<Map<String, Object>> yearList = topicTitleMapper.queryTopicCountYear();
        List<Map<String, Object>> maps = topicTitleMapper.queryCategoryTotal();
        int day = topicTitleMapper.queryCountByTime(0);
        int yesterday = topicTitleMapper.queryCountByTime(1);
        int week = topicTitleMapper.queryCountByTime(7);
        int upWeek = topicTitleMapper.queryCountByTime(15);
        int year = topicTitleMapper.queryCountByTime(30);
        Map<String, Object> map = new HashMap<>();
        map.put("day",day);
        map.put("total",i);
        map.put("week",week);
        map.put("upWeek",upWeek-week);
        map.put("year",year);
        map.put("yesterday",yesterday-day);
        map.put("weekList",weekList);
        map.put("monthList",monthList);
        map.put("yearList",yearList);
        map.put("categoryTotal",maps);
        return map;
    }
}
