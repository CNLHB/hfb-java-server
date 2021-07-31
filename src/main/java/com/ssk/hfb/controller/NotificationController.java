package com.ssk.hfb.controller;


import com.ssk.hfb.pojo.Message;
import com.ssk.hfb.service.MessageService;
import com.ssk.hfb.utils.UserContext;
import com.ssk.hfb.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

/**
 * WebSocketController
 */
@RestController
@Slf4j
public class NotificationController {
    @Autowired
    private MessageService messageService;

    /**
     * 消息推送 a->b
     * @param message
     * @return
     * @throws IOException
     */
    @PostMapping("/push/message")
    public ResponseEntity<Message> pushToWeb(@RequestBody Message message) throws IOException {
        System.out.println(message);
        int uId = UserContext.getId();
        if (uId==message.getFromId()){
            Message message1 = messageService.saveMsg(message);
            WebSocketServer.sendInfo(message1);
            log.info(message.toString());
            return ResponseEntity.ok(message1);
        }
        return ResponseEntity.ok(null);
    }
}

