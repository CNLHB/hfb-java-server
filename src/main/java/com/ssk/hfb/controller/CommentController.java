package com.ssk.hfb.controller;


import com.ssk.hfb.common.enums.SucessEnum;
import com.ssk.hfb.common.result.SucessHandler;
import com.ssk.hfb.common.sucess.ResultSucess;
import com.ssk.hfb.common.vo.PageResult;
import com.ssk.hfb.common.vo.UserComment;
import com.ssk.hfb.pojo.Comment;
import com.ssk.hfb.service.AuthService;
import com.ssk.hfb.service.CommentService;
import com.ssk.hfb.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    AuthService authService;

    /**
     * 获取某个动态的评论
     * @param tid
     * @return
     */
    @GetMapping("{tid}")
    public ResponseEntity< PageResult<Comment>> queryBrandByPage(
            @PathVariable("tid") Integer tid
           ) {
        List<Comment> comments = commentService.selCommList(tid, 0);
        int i = commentService.selCountByTId(tid);
        Long total = new Long(i);
        PageResult<Comment> commentPageResult = new PageResult<>(total, comments);
        return ResponseEntity.ok(commentPageResult);
    }

    /**
     * 保存评论
     * @param comment
     * @return
     */
    @PostMapping
    public ResponseEntity<SucessHandler> saveCommetn(@RequestBody Comment comment) {
        Boolean aBoolean = commentService.saveComment(comment);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }

    /**
     * 删除某个动态的评论
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<SucessHandler> deleteComment(@PathVariable int id){
        commentService.deleteComment(id);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_DELETE_SUCESS));
    }

    /**
     * 获取用户评论数
     * @return
     */
    @GetMapping("/count")
    public ResponseEntity<ResultSucess<String,Object>> queryCountByUId(){
        int uId = UserContext.getId();
        int i = commentService.queryCountByUId(uId);
        Map<String, Object> map = new HashMap<>();
        map.put("count",i);
        ResultSucess<String,Object> resultSucess = new ResultSucess(SucessEnum.DATA_GET_SUCESS,map);
        return ResponseEntity.ok(resultSucess);
    }

    /**
     * 获取用户评论
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity< List<UserComment>> queryCommByUId(){
        int uId = UserContext.getId();
        List<UserComment> userComments = commentService.queryCommentByUId(uId);
        return ResponseEntity.ok(userComments);
    }
}
