package com.ssk.hfb.service;


import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.mapper.HistoryMapper;
import com.ssk.hfb.pojo.BrowseHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class HistroyService {
    @Autowired
    private HistoryMapper historyMapper;
    public List<BrowseHistory> queryHistoryListByUId(Integer uid){
        Example example = new Example(BrowseHistory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uId", uid);
        String orderByClause = "create_time" + " DESC" ;
        example.setOrderByClause(orderByClause);
        List<BrowseHistory> historyMappers = historyMapper.selectByExample(example);
        return historyMappers;
    }
    public void saveTopicHistory(int meId, BrowseHistory his) {
        Example example = new Example(BrowseHistory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tId",his.getTId());
        criteria.andEqualTo("uId",his.getUId());
        int i = historyMapper.updateByExampleSelective(his, example);
        if (i ==0 ){
            int insert = historyMapper.insert(his);
            if (insert==0){
                throw new CommonAdviceException(ExceptioneEnum.SAVE_ERROR);
            }
        }


    }

    public void deleteHisTopic(List<Long> ids ) {
        int i = historyMapper.deleteByIdList(ids);

        if (i ==0 ){
            throw new CommonAdviceException(ExceptioneEnum.DETELE_ERROR);
        }

    }
}
