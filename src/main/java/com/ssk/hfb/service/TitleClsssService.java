package com.ssk.hfb.service;


import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.mapper.TitleClassMapper;
import com.ssk.hfb.pojo.TitleClass;
import com.ssk.hfb.pojo.TopicTitle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TitleClsssService {
    @Autowired
    private TitleClassMapper titleClassMapper;
    @Autowired
    private TopicService topicService;
    @Autowired
    private TopicTitleService topicTitleService;
    public List<Map> queryTIdListById(Integer cid, Integer uid){
        TitleClass titleClass = new TitleClass();
        titleClass.setCId(cid);
        List<TitleClass> list = titleClassMapper.select(titleClass);

        if (CollectionUtils.isEmpty(list)){
            throw new CommonAdviceException(ExceptioneEnum.QUERY_RESULT_EMPTY);
        }
        List<Long> ids = list.stream().map(TitleClass::getTId).collect(Collectors.toList());
        List<Map> maps = topicService.queryTIdListById(ids);

        return maps;
    }
    public void saveTitleClass(List<Integer> ids, Integer tid){
        if (CollectionUtils.isEmpty(ids)){
            return;
        }
        for (Integer id : ids){
            TitleClass titleClass = new TitleClass();
            titleClass.setTId(Long.valueOf(tid));
            titleClass.setCId(id);
            int insert = titleClassMapper.insert(titleClass);

        }
    }
    public  List<TopicTitle> queryList(Integer tid){
        TitleClass titleClass = new TitleClass();
        titleClass.setTId(Long.valueOf(tid));
        List<TitleClass> list = titleClassMapper.select(titleClass);
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        List<Integer> ids = list.stream().map(TitleClass::getCId).collect(Collectors.toList());
        ArrayList<Long> listId = new ArrayList<>();

        for (Integer id : ids){
            listId.add(Long.valueOf(id));
        }
        List<TopicTitle> topicTitles = topicTitleService.queryListByIds(listId);
        return topicTitles;
    }

}
