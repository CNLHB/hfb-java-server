package com.ssk.hfb.service;


import com.github.pagehelper.PageHelper;
import com.ssk.hfb.common.utils.CategoryId;
import com.ssk.hfb.mapper.TopicTitleClassMapper;
import com.ssk.hfb.mapper.TopicTitleMapper;
import com.ssk.hfb.pojo.Topic;
import com.ssk.hfb.pojo.TopicTitle;
import com.ssk.hfb.pojo.TopicTitleClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class TopicTitleClassService {
    @Autowired
    private TopicTitleClassMapper topicTitleClassMapper;
    public List<TopicTitleClass> queryTopicTilteList() {
        List<TopicTitleClass> titleClasses = topicTitleClassMapper.selectAll();
        return titleClasses;
    }

    public TopicTitleClass searchTopicById(Integer cId) {
        TopicTitleClass topicTitleClass = new TopicTitleClass();
        topicTitleClass.setId(cId);
        TopicTitleClass topicTitleClass1 = topicTitleClassMapper.selectOne(topicTitleClass);
        if (topicTitleClass1==null){
            return topicTitleClass;
        }
        return topicTitleClass1;

    }
}
