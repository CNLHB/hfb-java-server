package com.ssk.hfb.service;

import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.mapper.TopicActivelMapper;
import com.ssk.hfb.mapper.UserMapper;
import com.ssk.hfb.pojo.TopicActive;
import com.ssk.hfb.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicActiveService {



    @Autowired
    private TopicActivelMapper activelMapper;

    public Boolean saveTopicActive(User user){


        return true;
    }
    public int selUserLikeNum(Integer uid){
        TopicActive topicActive = new TopicActive();
        topicActive.setTUid(uid);
        int i = activelMapper.selectCount(topicActive);
        return i;
    }

}
