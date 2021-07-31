package com.ssk.hfb.service;


import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.mapper.CollectMapper;
import com.ssk.hfb.mapper.HistoryMapper;
import com.ssk.hfb.pojo.BrowseHistory;
import com.ssk.hfb.pojo.Collect;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


@Service
public class CollectService {
    @Autowired
    private CollectMapper collectMapper;
    public List<Collect> queryCollectListByUId(Integer uid){
        Example example = new Example(BrowseHistory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uId", uid);
        String orderByClause = "create_time" + " DESC" ;
        example.setOrderByClause(orderByClause);
        List<Collect> historyMappers = collectMapper.selectByExample(example);

        return historyMappers;
    }
    public void saveCollectTopic(int meId, Collect collect) {

        int i = collectMapper.selectCount(collect);
        if (i ==0 ){
            collect.setCreateTime(new Date());
            int insert = collectMapper.insert(collect);
            if (insert==0){
                throw new CommonAdviceException(ExceptioneEnum.SAVE_ERROR);
            }
        }else {
            int delete = collectMapper.delete(collect);

        }
    }
//    public void deleteCollectTopic(List<Integer> cid ) {
//
//        int insert = collectMapper.deleteByPrimaryKey(cid);
//        if (insert ==0 ){
//            throw new CommonAdviceException(ExceptioneEnum.DETELE_ERROR);
//        }
//
//    }
    public Boolean queryCollectTopic(Integer tid, Integer uid) {
        Collect collect = new Collect();
        collect.setUId(uid);
        collect.setTId(Long.valueOf(tid));
        int i = collectMapper.selectCount(collect);
        if (i ==0 ){
            return false;
        }
        return true;
    }

    public int queryCountByUid(int uId) {
        Collect collect = new Collect();
        collect.setUId(uId);
        int i = collectMapper.selectCount(collect);
        return i;
    }
    public void deleteCollectTopic(List<Long> ids ) {
        int i = collectMapper.deleteByIdList(ids);
        if (i ==0 ){
            throw new CommonAdviceException(ExceptioneEnum.DETELE_ERROR);
        }

    }

}
