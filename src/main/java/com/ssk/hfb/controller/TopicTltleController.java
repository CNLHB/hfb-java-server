package com.ssk.hfb.controller;

import com.ssk.hfb.common.enums.SucessEnum;
import com.ssk.hfb.common.sucess.ResultSucess;
import com.ssk.hfb.common.vo.PageResult;
import com.ssk.hfb.pojo.TitleClass;
import com.ssk.hfb.pojo.Topic;
import com.ssk.hfb.pojo.TopicTitle;
import com.ssk.hfb.service.TitleClsssService;
import com.ssk.hfb.service.TopicTitleService;
import com.ssk.hfb.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("topic/title")
public class TopicTltleController {

    @Autowired
    private TopicTitleService topicTitleService;
    @Autowired
    private TitleClsssService titleClsssService;

    @GetMapping
    public ResponseEntity< PageResult<TopicTitle> > queryTopicTilteList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "cid",defaultValue = "") Integer cid,
            @RequestParam(value = "search",required = false) String search
    ){
        PageResult<TopicTitle> topicTitlePageResult = topicTitleService.queryTopicTilteList(page, rows, cid, search);
        return ResponseEntity.ok(topicTitlePageResult);
    }
    @GetMapping("v2")
    public ResponseEntity< PageResult<Map>> queryTopicTilteListV2(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "cid",defaultValue = "") Integer cid,
            @RequestParam(value = "search",required = false) String search
    ){
        PageResult<Map> mapPageResult = topicTitleService.queryTopicTilteListV2(page, rows, cid, search);
        return ResponseEntity.ok(mapPageResult);
    }
    @PutMapping("freeze")
    public ResponseEntity<ResultSucess> freezeTopicList(
            @RequestParam(value = "ids") List<Integer> ids,
            @RequestParam(value = "freeze") Boolean freeze

    ){
        topicTitleService.freezeTopicList(ids, freeze);
        return ResponseEntity.ok(new ResultSucess(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @PostMapping
    public ResponseEntity<Void> saveTopicTilte(
            @RequestBody TopicTitle title
    ){
        topicTitleService.saveTopicTilte(title);
        return ResponseEntity.ok(null);
    }
    @GetMapping("{id}")
    public ResponseEntity<TopicTitle> queryTopicTilteInfoById(
            @PathVariable(value = "id") Integer id
    ){
        TopicTitle topicTitle = topicTitleService.queryTopicTilteInfoById(id);
        return ResponseEntity.ok(topicTitle);
    }
    @GetMapping("list/{id}")
    public ResponseEntity<List<Map>> queryTopicTilteListById(
            @PathVariable(value = "id") Integer id
    ){
        int uId = UserContext.getId();
        List<Map> maps = titleClsssService.queryTIdListById(id, uId);
        return ResponseEntity.ok(maps);
    }
    @GetMapping("user/{id}")
    public ResponseEntity<List<TopicTitle>> queryTopicTilteListByUId(
            @PathVariable(value = "id") Integer id
    ){
        List<TopicTitle> topicTitles = topicTitleService.queryTopicTilteListByUId(id);
        return ResponseEntity.ok(topicTitles);
    }
    @GetMapping("page/{id}")
    public ResponseEntity<List<TopicTitle>> queryTopicTilteListByTId(
            @PathVariable(value = "id") Integer id
    ){
        List<TopicTitle> topicTitles = topicTitleService.queryTopicTilteListByTId(id);
        return ResponseEntity.ok(topicTitles);
    }
    @GetMapping("trend")
    public ResponseEntity<ResultSucess> selTopicTitleIncreaseTrend(){
        Map map = topicTitleService.selTopicTitleIncreaseTrend();
        return ResponseEntity.ok(new ResultSucess(SucessEnum.RESULT_GET_SUCESS,map));
    }
}
