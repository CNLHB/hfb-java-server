package com.ssk.hfb.service;

import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.mapper.ChatListMapper;
import com.ssk.hfb.mapper.MessageMapper;
import com.ssk.hfb.pojo.ChatList;
import com.ssk.hfb.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {


    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private ChatListMapper chatListMapper;

    public List<Message> queryMsgList(int uid,int fromId, int toId){
        Example example = new Example(Message.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.orEqualTo("fromId",fromId).andEqualTo("toId",toId)
                .andEqualTo("belong",uid).andIsNull("deleteTime");
        criteria.orEqualTo("toId",fromId).andEqualTo("fromId",toId)
                .andEqualTo("belong",uid).andIsNull("deleteTime");
        String orderByClause = "send_time" + " ASC" ;
        example.setOrderByClause(orderByClause);
        List<Message> messages = messageMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(messages)){
            messages = new ArrayList<>();
        }
        return messages;
    }
    public void readMsg(Integer uid,List<Integer> mids) {
        for (int mid : mids){
            Message message = new Message();
            message.setId(mid);
            message.setStatus(true);
            int i = messageMapper.updateByPrimaryKeySelective(message);

        }
    }
    public void deleteMsgList(Integer uid, Integer cid){
            Example example = new Example(Message.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("belong",uid);
            criteria.andEqualTo("cId",cid);
            Message message = new Message();
            message.setDeleteTime(new Date());
            int i = messageMapper.updateByExampleSelective(message, example);


    }
    public Message saveMsg(Message message){
        message.setStatus(false);
        message.setSendTime(new Date());
        ChatList chatList = new ChatList();
        chatList.setId(message.getCId());
        chatList.setAfterTime(new Date());
        message.setBelong(message.getFromId());
        int t = message.getFromId();
        int f = message.getToId();
        int from = messageMapper.insert(message);
        message.setBelong(message.getToId());
        message.setId(null);

        int i = chatListMapper.updateByPrimaryKeySelective(chatList);
        ChatList chatList1 = new ChatList();
        chatList1.setFromId(message.getToId());
        chatList1.setToId(message.getFromId());
        ChatList chatList2 = chatListMapper.selectOne(chatList1);
        if (chatList2!=null){
            chatList2.setAfterTime(new Date());
            chatListMapper.updateByPrimaryKeySelective(chatList2);
            message.setCId(chatList2.getId());
            int to = messageMapper.insert(message);
        }else {
            chatList1.setAfterTime(new Date());
            chatList1.setCreateTime(new Date());
            chatList1.setStatus(true);
            chatListMapper.insert(chatList1);
            message.setCId(chatList1.getId());
            int to = messageMapper.insert(message);

        }

        if (from==0){
                throw new CommonAdviceException(ExceptioneEnum.SAVE_ERROR);
            }
        System.out.println("保存数据库");
         return message;
    }
}
