package com.ssk.hfb.controller;

import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.enums.SucessEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.common.pojo.UserInfo;
import com.ssk.hfb.common.result.SucessHandler;
import com.ssk.hfb.common.sucess.ResultSucess;
import com.ssk.hfb.common.utils.NumberUtils;
import com.ssk.hfb.pojo.Attention;
import com.ssk.hfb.pojo.ChatList;
import com.ssk.hfb.pojo.Message;
import com.ssk.hfb.pojo.User;
import com.ssk.hfb.service.*;
import com.ssk.hfb.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("chat")
public class ChatListController {

    @Autowired
    private AttentionService attentionService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private AuthService authService;

    /**
     * 获取用户聊天列表
     * @return
     */
    @GetMapping("list")
    public List<ChatList> queryList(
    ){
        int uId = UserContext.getId();
        List<ChatList> chatLists = attentionService.selAttListByUId(uId);
        return chatLists;
    }

    /**
     * 保存聊天列表
     * @param chatList
     * @return
     */
    @PostMapping
    public ResponseEntity<ChatList> saveChat(
            @RequestBody ChatList chatList
    ){
        int uId = UserContext.getId();
        ChatList chat = attentionService.saveChat(uId,chatList);
        return ResponseEntity.ok(chat);
    }

    /**
     * 未读聊天列表设置为已读，一个或多个
     * @param mids 消息id数组
     * @return
     */
    @PutMapping("read")
    public ResponseEntity<SucessHandler> readMsg(
            @RequestParam(value = "mids") List<Integer> mids
            ){
        int uId = UserContext.getId();
        messageService.readMsg(uId,mids);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }

    /**
     * 删除聊天列表
     * @param cids
     * @return
     */
    @DeleteMapping
    public ResponseEntity<SucessHandler> deleteChatList(
            @RequestParam(value = "cids")  List<Integer> cids
    ){
        int uId = UserContext.getId();
//        messageService.deleteMsgList(uId, cids);
        attentionService.deleteChatList(uId,cids);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.DATA_GET_SUCESS));
    }

    /**
     * 获取某个聊天的所有消息
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    public ResponseEntity<ChatList> queryChatByCId(
            @PathVariable(value = "cid") Integer cid
    ){
        int uId = UserContext.getId();
        ChatList chatList = attentionService.queryChatByCId(uId, cid);
        return ResponseEntity.ok(chatList);
    }
}
