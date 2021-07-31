package com.ssk.hfb.service;

import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.mapper.AttentionMapper;
import com.ssk.hfb.mapper.ChatListMapper;
import com.ssk.hfb.pojo.Attention;
import com.ssk.hfb.pojo.ChatList;
import com.ssk.hfb.pojo.Message;
import com.ssk.hfb.pojo.User;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttentionService {



    @Autowired
    private AttentionMapper activelMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatListMapper chatListMapper;
    public Boolean saveAttention(Attention att){

        if (att.getFromId()==null){
            throw new CommonAdviceException(ExceptioneEnum.DETELE_ERROR);
        }
        int i = activelMapper.selectCount(att);
        if (i == 0){
            int count = activelMapper.insert(att);
            if (count==0){
                throw new CommonAdviceException(ExceptioneEnum.SAVE_ERROR);
            }
            return true;
        }
        int delete = activelMapper.delete(att);
        if (delete==0){
            throw new CommonAdviceException(ExceptioneEnum.DETELE_ERROR);
        }
        return true;
    }
    public Attention searchAttention(Integer fromId, Integer toId){
        Attention att = new Attention();
        att.setFromId(fromId);
        att.setToId(toId);
        Attention topicActive = activelMapper.selectOne(att);
        return topicActive;
    }


    public Boolean isAtt(Integer fromId, Integer toId){
        Attention att = new Attention();
        att.setFromId(fromId);
        att.setToId(toId);

        int i = activelMapper.selectCount(att);
        if (i == 0){
            return false;
        }
        return true;
    }
    public Boolean deleteAttention(Attention att){
        if (att.getFromId()==null){
            throw new CommonAdviceException(ExceptioneEnum.DETELE_ERROR);
        }
        int count = activelMapper.delete(att);
        if (count==0){
            throw new CommonAdviceException(ExceptioneEnum.DETELE_ERROR);
        }

        return true;
    }
    public List<ChatList> selAttListByUId(int uid){

        Example example = new Example(ChatList.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fromId",uid).andEqualTo("status",true);
//        criteria.orEqualTo("toId", uid).andEqualTo("status",true);
        String orderByClause = "create_time" + " DESC" ;
        example.setOrderByClause(orderByClause);
        List<ChatList> chatLists = chatListMapper.selectByExample(example);

        if (chatLists !=null){
         for (ChatList s : chatLists){
             User user = userService.searchUserById(s.getToId());
             s.setUsername(user.getUserName());
             s.setUserpic(user.getAuthorUrl());

             List<Message> messages = messageService.queryMsgList(uid,s.getFromId(), s.getToId());
             if (messages != null){
                 s.setMessages(messages);
             }
         }
        }

        return chatLists;
    }

    public void deleteChatList(int uId,  List<Integer>  cids) {
        ArrayList<Long> list = new ArrayList<>();
        for(Integer cid : cids) {
            messageService.deleteMsgList(uId,cid);
            Long i = Long.valueOf(cid);
            list.add(i);
        }
        int i = chatListMapper.deleteByIdList(list);
        if (i==0){
            throw new CommonAdviceException(ExceptioneEnum.DETELE_ERROR);
        }
    }
    public List<User> selAttListByUid(int uid){
        Attention attention = new Attention();
        attention.setFromId(uid);
        List<Attention> list = activelMapper.select(attention);
        if (CollectionUtils.isEmpty(list)){
            throw new CommonAdviceException(ExceptioneEnum.QUERY_RESULT_EMPTY);
        }
        List<Integer> ids = list.stream().map(Attention::getToId).collect(Collectors.toList());
        ArrayList<Long> lids = new ArrayList<>();
        for (Integer id : ids){
            lids.add(id.longValue());
        }
        List<User> users = userService.selUserListByIds(lids);
        return users;
    }
    public List<User> selEachAttListByUid(int uid){
        Attention attention = new Attention();
        attention.setToId(uid);
        List<User> users = selAttListByUid(uid);
        List<User> list = new ArrayList<>();
        for (User u : users){
            attention.setFromId(u.getId());
            int i = activelMapper.selectCount(attention);
            if (i!=0){
                list.add(u);
            }
        }
        return list;
    }
    public List<User> selFansByUid(int uid){
        Attention attention = new Attention();
        attention.setToId(uid);
        List<Attention> list = activelMapper.select(attention);
        System.out.println(list);
        if (CollectionUtils.isEmpty(list)){
            throw new CommonAdviceException(ExceptioneEnum.SUCESS_QUERY_RESULT_EMPTY);
        }
        List<Integer> ids = list.stream().map(Attention::getFromId).collect(Collectors.toList());
        System.out.println(ids);
        ArrayList<Long> lids = new ArrayList<>();
        for (Integer id : ids){
            lids.add(id.longValue());
        }
        List<User> users = userService.selUserListByIds(lids);
        return users;
    }
    public int selAttCountByUid(int uid){
        Attention attention = new Attention();
        attention.setFromId(uid);
        int i = activelMapper.selectCount(attention);
        return i;
    }
    public int selFansCountByUid(int uid){
        Attention attention = new Attention();
        attention.setToId(uid);
        int i = activelMapper.selectCount(attention);
        return i;
    }

    public ChatList saveChat(Integer uid ,ChatList chat) {
        Example example = new Example(ChatList.class);
        Example example1 = new Example(ChatList.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("toId",chat.getFromId()).andEqualTo("fromId",chat.getToId());
        criteria.andEqualTo("fromId",chat.getFromId()).andEqualTo("toId",chat.getToId());
        ChatList chatList1 = chatListMapper.selectOneByExample(example1);
        if (chatList1==null){
            ChatList chatList2 = new ChatList();
            System.out.println(chat);
            chatList2.setFromId(chat.getToId());
            chatList2.setToId(chat.getFromId());
            chatList2.setStatus(true);
            Date date = new Date();
            chatList2.setCreateTime(date);
            chatList2.setAfterTime(date);
            int count = chatListMapper.insert(chatList2);
        }
        ChatList chatList = chatListMapper.selectOneByExample(example);
        if (chatList != null){
            User user = userService.searchUserById(chatList.getToId());
            chatList.setUsername(user.getUserName());
            chatList.setUserpic(user.getAuthorUrl());
            List<Message> messages = messageService.queryMsgList(uid,chatList.getFromId(), chatList.getToId());
            chatList.setMessages(messages);
            return chatList;
        }
        chat.setStatus(true);
        Date date = new Date();
        chat.setCreateTime(date);
        chat.setAfterTime(date);
        int insert = chatListMapper.insert(chat);
        if (insert==0){
            throw new CommonAdviceException(ExceptioneEnum.SAVE_ERROR);
        }
        User user = userService.searchUserById(chat.getToId());
        chat.setUsername(user.getUserName());
        chat.setUserpic(user.getAuthorUrl());
        List<Message> messages = messageService.queryMsgList(uid,chat.getFromId(), chat.getToId());
        chat.setMessages(messages);
        return chat;
    }

    public ChatList queryChatByCId(int uId, Integer cid) {
        ChatList chatList = new ChatList();
        chatList.setId(cid);
//        chatList.setToId(uId);
        ChatList chatList1 = chatListMapper.selectByPrimaryKey(chatList);
        System.out.println(chatList1);
        if (chatList1==null){
            return null;
        }
        User user1 = userService.queryUserById(chatList1.getToId());
        chatList1.setUsername(user1.getUserName());
        chatList1.setUserpic(user1.getAuthorUrl());
        return chatList1;
    }
}
