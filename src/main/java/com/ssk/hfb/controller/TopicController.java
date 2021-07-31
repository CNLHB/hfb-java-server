package com.ssk.hfb.controller;

import com.ssk.hfb.common.enums.SucessEnum;
import com.ssk.hfb.common.result.SucessHandler;
import com.ssk.hfb.common.sucess.ResultSucess;
import com.ssk.hfb.common.vo.PageResult;
import com.ssk.hfb.pojo.BrowseHistory;
import com.ssk.hfb.pojo.Collect;
import com.ssk.hfb.pojo.Topic;
import com.ssk.hfb.pojo.TopicActive;
import com.ssk.hfb.service.*;
import com.ssk.hfb.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("topic")
public class TopicController {
    @Autowired
    private TopicService topicService;
    @Autowired
    private AuthService authService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private TopicDetailService detailService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Topic>> queryTopicByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "type", defaultValue = "tuijian") String type,
            @RequestParam(value = "search",defaultValue = "") String search
            ) {
        int uId = UserContext.getId();
        System.out.println(uId);
        PageResult<Topic> result = topicService.queryTopicByPage(page,rows,type,uId,search);
        return ResponseEntity.ok(result);
    }
    @GetMapping("v2/page")
    public ResponseEntity<PageResult<Map>> queryTopicV2ByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "cid",required = false) Integer cid,
            @RequestParam(value = "search",defaultValue = "") String search
    ) {
        int uId = UserContext.getId();
        System.out.println(uId);
        PageResult<Map> result = topicService.queryTopicV2ByPage(page,rows,cid,uId,search);
        return ResponseEntity.ok(result);
    }
    @PutMapping("freeze")
    public ResponseEntity<ResultSucess> freezeTopicList(
            @RequestParam(value = "ids") List<Integer> ids,
            @RequestParam(value = "freeze") Boolean freeze

    ){
        topicService.freezeTopicList(ids, freeze);
        return ResponseEntity.ok(new ResultSucess(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @GetMapping("att")
    public ResponseEntity<PageResult> queryAttTopicByUidPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        int uId = UserContext.getId();
        PageResult pageResult = topicService.queryAttTopicList(page, rows, uId);
        return ResponseEntity.ok(pageResult);
    }
    @PutMapping
    public ResponseEntity<SucessHandler> updataTopicById(@RequestBody Topic t) {
        topicService.updataTopicById(t);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @PostMapping
    public ResponseEntity<SucessHandler> saveTopic(@RequestBody Topic t){
        topicService.saveTopic(t);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @PostMapping("active")
    public ResponseEntity<SucessHandler> likeOrTreadTopic(@RequestBody TopicActive t){
        topicService.likeOrTreadTopic(t);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @PutMapping("active/list")
    public ResponseEntity<SucessHandler> likeTopicList(
            @RequestParam(value = "ids") List<Long> ids
            ){
        detailService.likeTopic(ids);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @GetMapping("/count")
    public ResponseEntity<ResultSucess<String,Object>> queryCountByUId(){
        int uId = UserContext.getId();
        int i = topicService.queryCountByUId(uId);
        Map<String, Object> map = new HashMap<>();
        map.put("count",i);
        ResultSucess<String,Object> resultSucess = new ResultSucess(SucessEnum.DATA_GET_SUCESS,map);
        return ResponseEntity.ok(resultSucess);
    }
    @GetMapping("list/{uid}")
    public ResponseEntity< List<Map>> queryTopicByUId(
            @PathVariable(value = "uid") Integer uid){
        int meId = UserContext.getId();
        List<Map> maps = topicService.queryTopicByUId(meId,uid);
        return ResponseEntity.ok(maps);
    }
    @GetMapping("/history")
    public ResponseEntity<List<BrowseHistory>> queryTopicHistory(
            ){

        int uId = UserContext.getId();
        List<BrowseHistory> list = topicService.queryTopicById(uId);
        return ResponseEntity.ok(list);
    }
    @PostMapping("/history")
    public ResponseEntity<SucessHandler> saveTopicHistory(
            @RequestBody BrowseHistory browseHistory
            ){
        int uId = UserContext.getId();
        topicService.saveTopicHistory(uId,browseHistory);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @DeleteMapping("/history")
    public ResponseEntity<SucessHandler> deleteCollectTopicByIds(
            @RequestParam(value = "ids") List<Long> ids
    ){
        int uId = UserContext.getId();
        topicService.deleteHisTopicByIds(ids);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @PostMapping("/collect")
    public ResponseEntity<SucessHandler> collectTopic(
            @RequestBody Collect collect
    ){
        int uId = UserContext.getId();
        collectService.saveCollectTopic(uId,collect);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @GetMapping("/collect")
    public ResponseEntity<List<Collect>> queryCollectTopicList(
    ){
        int uId = UserContext.getId();
        List<Collect> list = topicService.queryCollectListByUId(uId);
        return ResponseEntity.ok(list);
    }
    @DeleteMapping("/collect")
    public ResponseEntity<SucessHandler> deleteCollectTopicList(
            @RequestParam(value = "ids") List<Long> ids
    ){
        int uId = UserContext.getId();
        collectService.deleteCollectTopic(ids);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_DELETE_SUCESS));
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Topic> queryTopicDetailById(
            @PathVariable(value = "id") Integer id
    ){
        int uId = UserContext.getId();
        Topic detail = topicService.queryTopicDetailById(id,uId);
        return ResponseEntity.ok(detail);
    }
    @GetMapping("trend")
    public ResponseEntity<ResultSucess> selTopicIncreaseTrend(){
        Map map = topicService.selTopicIncreaseTrend();
        return ResponseEntity.ok(new ResultSucess(SucessEnum.RESULT_GET_SUCESS,map));
    }
}
