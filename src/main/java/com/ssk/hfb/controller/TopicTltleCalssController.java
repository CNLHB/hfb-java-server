package com.ssk.hfb.controller;

import com.ssk.hfb.pojo.TopicTitle;
import com.ssk.hfb.pojo.TopicTitleClass;
import com.ssk.hfb.service.TopicTitleClassService;
import com.ssk.hfb.service.TopicTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("topic/title/class")
public class TopicTltleCalssController {

    @Autowired
    private TopicTitleClassService topicTitleClassService;

    @GetMapping
    public ResponseEntity<List<TopicTitleClass>> queryTopicTilteList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "cid",defaultValue = "") Integer cid
    ){
        List<TopicTitleClass> titleClasses = topicTitleClassService.queryTopicTilteList();
        return ResponseEntity.ok(titleClasses);
    }
    @PostMapping
    public ResponseEntity<Void> saveTopicTilte(
            @RequestBody TopicTitle title
    ){
        return ResponseEntity.ok(null);
    }

}
