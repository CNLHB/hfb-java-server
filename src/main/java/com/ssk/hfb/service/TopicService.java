package com.ssk.hfb.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.common.pojo.InfoNum;
import com.ssk.hfb.common.utils.CategoryId;
import com.ssk.hfb.common.utils.JsonUtils;
import com.ssk.hfb.common.vo.PageResult;
import com.ssk.hfb.mapper.*;
import com.ssk.hfb.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


@Service
public class TopicService {

    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TopicDetailMapper detailMapper;
    @Autowired
    private TopicActivelMapper activelMapper;
    @Autowired
    private AttentionService userActiveService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private HistroyService histroyService;
    @Autowired
    private AttentionService attentionService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private TitleClsssService titleClsssService;
    @Autowired
    private TopicTitleMapper topicTitleMapper;

    /**
     *
     * @param page 当前页
     * @param rows 条数（一页多少条数据）
     * @param key  查看分类
     * @param id  用户id
     * @param search 搜索关键词
     * @return
     */
    public PageResult queryTopicByPage(Integer page, Integer rows,String key, Integer id, String search) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Topic.class);
        Example.Criteria criteria = example.createCriteria();
        if (key.equals("tuijian")){
        }else {
            Integer cid = CategoryId.getCid(key);
            criteria.andEqualTo("cId",cid);
        }
        //关键字过滤
        if (StringUtils.isNoneBlank(search)){
            criteria.andLike("title","%" + search + "%");
        }
        // 排序
        String orderByClause = "create_time" + " DESC" ;
        example.setOrderByClause(orderByClause);
        criteria.andEqualTo("display",1);
        List<Topic> list = topicMapper.selectByExample(example);
        List<Map> listMap = process(list, id);
        PageInfo<Topic> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),info.getPageNum(), listMap);
    }
    public PageResult<Map> queryTopicV2ByPage(Integer page, Integer rows, Integer cid, int uId, String search) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Topic.class);
        Example.Criteria criteria = example.createCriteria();
        if (cid!=null){
            criteria.andEqualTo("cId",cid);
        }
        //关键字过滤
        if (StringUtils.isNoneBlank(search)){
            criteria.andLike("title","%" + search + "%");
        }
        // 排序
        String orderByClause = "create_time" + " DESC" ;
        example.setOrderByClause(orderByClause);
        List<Topic> list = topicMapper.selectByExample(example);
        List<Map> maps = processV2(list);
        PageInfo<Topic> info = new PageInfo<>(list);
        PageResult<Map> objectPageResult = new PageResult<>(info.getTotal(), info.getPageNum(), maps);
        return objectPageResult;
    }
    public Boolean updataTopicById(Topic t) {
        int count = topicMapper.updateByPrimaryKey(t);
        if (count==0){
            throw  new CommonAdviceException(ExceptioneEnum.TOPIC_UPDATA_ERROR);
        }
        return true;

    }

    public void saveTopic(Topic t) {
        t.setDisplay(true);
        t.setCreateTime(new Date());
        int count = topicMapper.insert(t);
        titleClsssService.saveTitleClass(t.getIds(), t.getId());
        TopicDetail td = new TopicDetail();
        td.setTId(t.getId());
        td.setCommentNum(0);
        td.setLikeNum(0);
        td.setTreadNum(0);
        td.setHits(0);
        int insert = detailMapper.insert(td);
        if (!CollectionUtils.isEmpty(t.getIds())){
            for (Integer id : t.getIds()){
                topicTitleMapper.updateTotalById(Long.valueOf(id));
            }
        }
        if (count==0 || insert==0){
            throw  new CommonAdviceException(ExceptioneEnum.TOPIC_SAVE_ERROR);
        }
    }

    public void likeOrTreadTopic(TopicActive t) {
        Example example =new Example(TopicActive.class);
        //where 条件
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tId",t.getTId());
        criteria.andEqualTo("uId",t.getUId());
        int count = activelMapper.updateByExampleSelective(t, example);
        detailMapper.likeTopic(Long.valueOf(t.getTId()));
        if (count==0 ){
            int i = activelMapper.insert(t);
            if (i==0){
                throw  new CommonAdviceException(ExceptioneEnum.TOPIC_SAVE_ERROR);
            }
        }
    }
    public Boolean deleteTopicById(int id){
        Topic topic = new Topic();
        topic.setId(id);
        int count = topicMapper.deleteByPrimaryKey(topic);
        if (count==0){
            throw new CommonAdviceException(ExceptioneEnum.DETELE_ERROR);
        }
        TopicDetail topicDetail = new TopicDetail();
        topicDetail.setTId(id);
        int i = detailMapper.deleteByPrimaryKey(topicDetail);
        if (i==0){
            throw new CommonAdviceException(ExceptioneEnum.DETELE_ERROR);
        }
        return true;
    }

    public int queryCountByUId(int uId) {
        Topic topic = new Topic();
        topic.setUId(uId);
        int i = topicMapper.selectCount(topic);
        return i;

    }

    public List<Map>  queryTopicByUId(Integer meId,Integer uid) {
        Topic topic = new Topic();
        topic.setUId(uid);
        List<Topic> list = topicMapper.select(topic);
        List<Map> listMap = process(list, meId);
        return listMap;

    }
    public List<Map> processV2( List<Topic> list){
        List<Map> listMap = new ArrayList<>();
        for (Topic t: list){
            String cName = categoryService.queryCNameById(t.getCId());
            User user = userService.searchUserById(t.getUId());
            InfoNum infoNum =new InfoNum(0,0,0);
            TopicDetail topicDetail = detailMapper.selectByPrimaryKey(Long.valueOf(t.getId()));
            if (topicDetail==null){
                t.setCommentNum(0);
            }else {
                infoNum.setLikeNum(topicDetail.getLikeNum());
                t.setCommentNum(topicDetail.getCommentNum());
            }
            int commNum = commentService.selCountByTId(t.getId());
            List<TopicTitle> topicTitles = titleClsssService.queryList(t.getId());
            t.setInfoNum(infoNum);
            t.setLable(topicTitles);
            t.setCommentNum(commNum);
            t.setUsername(user.getUserName());
            t.setUserpic(user.getAuthorUrl());
            t.setUGender(user.getGender());
//            t.setCollect(aBoolean);
            String s = JsonUtils.toString(t);
            Map<String, Object> map = JsonUtils.toMap(s, String.class, Object.class);
            map.put("cName", cName);
            listMap.add(map);
        }
        return listMap;
    }
    public List<Map> process( List<Topic> list, Integer meId){
        List<Map> listMap = new ArrayList<>();
        for (Topic t: list){
            String cName = categoryService.queryCNameById(t.getCId());
            if (meId==-1){
            }
            User user = userService.searchUserById(t.getUId());
            Boolean att = userActiveService.isAtt(meId, t.getUId());
            InfoNum infoNum = queryTopicInfoNum(t.getId(), meId);
//            Boolean aBoolean = collectService.queryCollectTopic(t.getId(), meId);
            int commNum = commentService.selCountByTId(t.getId());
            TopicDetail topicDetail = detailMapper.selectByPrimaryKey(Long.valueOf(t.getUId()));
            if (topicDetail==null){
                t.setCommentNum(commNum);
            }else {
                t.setCommentNum(topicDetail.getCommentNum());
            }
            List<TopicTitle> topicTitles = titleClsssService.queryList(t.getId());
            t.setInfoNum(infoNum);
            t.setLable(topicTitles);
            t.setIsguanzhu(att);
            t.setUsername(user.getUserName());
            t.setUserpic(user.getAuthorUrl());
            t.setUGender(user.getGender());
//            t.setCollect(aBoolean);
            String s = JsonUtils.toString(t);
            Map<String, Object> map = JsonUtils.toMap(s, String.class, Object.class);
            map.put("cName", cName);
            listMap.add(map);
        }
        return listMap;
    }
    public PageResult queryAttTopicList(Integer page,Integer rows ,Integer uid){
        PageHelper.startPage(page, rows);
        List<User> users = attentionService.selAttListByUid(uid);
        Example example = new Example(Topic.class);
        Example.Criteria criteria = example.createCriteria();
        for (User u : users){
            criteria.orEqualTo("uId",u.getId());
        }
        String orderByClause = "create_time" + " DESC" ;
        example.setOrderByClause(orderByClause);
        List<Topic> list = topicMapper.selectByExample(example);
        List<Map> listMap = process(list, uid);
        PageInfo<Topic> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),info.getPageNum(), listMap);
    }


    public List<BrowseHistory>  queryTopicById(Integer id) {
        List<BrowseHistory> list = histroyService.queryHistoryListByUId(id);
        if (CollectionUtils.isEmpty(list)){
            System.out.println("empty");
            throw new CommonAdviceException(ExceptioneEnum.QUERY_RESULT_EMPTY);
        }
        return  list;
    }



    public InfoNum queryTopicInfoNum(Integer tid,Integer uid ){
//        int likeNum = queryTopicLikeNum(tid);
//        int treadNum = queryTopicTreadNum(tid);
        TopicDetail topicDetail = detailMapper.selectByPrimaryKey(Long.valueOf(tid));
        int active = queryTopicActive(uid, tid);

        if (topicDetail==null){
            return new InfoNum(active,0,0);

        }
        return new InfoNum(active,0,topicDetail.getLikeNum());
    }
    public int queryTopicLikeNum(Integer tid){
        TopicActive topicActive = new TopicActive();
        topicActive.setTId(tid);
        topicActive.setTActive(1);
        int i = activelMapper.selectCount(topicActive);
        return i;
    }
    public int queryTopicTreadNum( Integer tid){
        TopicActive topicActive = new TopicActive();
        topicActive.setTId(tid);
        topicActive.setTActive(2);
        int i = activelMapper.selectCount(topicActive);
        return i;
    }
    public int queryTopicActive(Integer uid, Integer tid){
        TopicActive topicActive = new TopicActive();
        topicActive.setTId(tid);
        topicActive.setUId(uid);
        TopicActive topicActive1 = activelMapper.selectOne(topicActive);
        if (topicActive1==null){
            return 0;
        }
        return topicActive1.getTActive();
    }

    public void saveTopicHistory(int meId, BrowseHistory browseHistory) {
        browseHistory.setCreateTime(new Date());
        histroyService.saveTopicHistory(meId, browseHistory);
    }

    public List<Collect> queryCollectListByUId(int meId) {
        List<Collect> collects = collectService.queryCollectListByUId(meId);
        if (CollectionUtils.isEmpty(collects)){
            System.out.println("empty");
            throw new CommonAdviceException(ExceptioneEnum.QUERY_RESULT_EMPTY);
        }

        return collects;
    }

    public void deleteCollectTopicByIds(List<Long> ids) {
        collectService.deleteCollectTopic(ids);
    }
    public void deleteHisTopicByIds(List<Long> ids) {
        histroyService.deleteHisTopic(ids);
    }

    public Topic queryTopicDetailById(Integer id, int meId) {
        Topic topic = new Topic();
        topic.setId(id);
        Topic t = topicMapper.selectOne(topic);
        if (t == null){
            throw new CommonAdviceException(ExceptioneEnum.QUERY_RESULT_EMPTY);
        }
        String cName = categoryService.queryCNameById(t.getCId());
        if (meId==-1){

        }
        User user = userService.searchUserById(t.getUId());
        Boolean att = userActiveService.isAtt(meId, t.getUId());
        InfoNum infoNum = queryTopicInfoNum(t.getId(), meId);
        Boolean aBoolean = collectService.queryCollectTopic(t.getId(), meId);
        int commNum = commentService.selCountByTId(t.getId());
        t.setInfoNum(infoNum);
        t.setCommentNum(commNum);
        t.setIsguanzhu(att);
        t.setUsername(user.getUserName());
        t.setUserpic(user.getAuthorUrl());
        t.setUGender(user.getGender());
        t.setCollect(aBoolean);
        String s = JsonUtils.toString(t);
        return t;
    }

    public  List<Map>  queryTIdListById(List<Long> ids) {
        List<Topic> list = topicMapper.selectByIdList(ids);
        List<Map> listMap = process(list, 1);
        return listMap;
    }

    public Topic queryMeTopicById(Integer tId) {
        Topic topic = new Topic();
        topic.setId(tId);
        Topic topic1 = topicMapper.selectOne(topic);
        return topic1;
    }


    public void freezeTopicList(List<Integer> ids, Boolean freeze) {
        for(Integer id : ids){
            Topic topic = new Topic();
            topic.setId(id);
            topic.setDisplay(freeze);
            int i = topicMapper.updateByPrimaryKeySelective(topic);
        }

    }
    public Map selTopicIncreaseTrend() {
        int i = topicMapper.queryTopicCount();
        List<Map<String, Object>> weekList = topicMapper.queryTopicCountWeek();
        List<Map<String, Object>> monthList = topicMapper.queryTopicCountMonth();
        List<Map<String, Object>> yearList = topicMapper.queryTopicCountYear();
        List<Map<String, Object>> maps = topicMapper.queryCategoryTotal();

        int day = topicMapper.queryCountByTime(0);
        int yesterday = topicMapper.queryCountByTime(1);
        int week = topicMapper.queryCountByTime(7);
        int upWeek = topicMapper.queryCountByTime(15);
        int year = topicMapper.queryCountByTime(30);
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
