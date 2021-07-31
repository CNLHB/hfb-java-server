package com.ssk.hfb.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.common.vo.PageResult;
import com.ssk.hfb.mapper.TopicDetailMapper;
import com.ssk.hfb.mapper.TopicTitleMapper;
import com.ssk.hfb.pojo.TitleClass;
import com.ssk.hfb.pojo.TopicTitle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class TopicDetailService {
    @Autowired
    private TopicDetailMapper detailMapper;
    public void likeTopic(List<Long> ids){
        for (Long id: ids){
            detailMapper.likeTopic(id);
        }
    }

}
