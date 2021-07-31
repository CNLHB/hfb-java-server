package com.ssk.hfb.service;

import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.common.vo.PageResult;
import com.ssk.hfb.common.vo.UserComment;
import com.ssk.hfb.mapper.AttentionMapper;
import com.ssk.hfb.mapper.CommentMapper;
import com.ssk.hfb.mapper.TopicDetailMapper;
import com.ssk.hfb.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {



    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private TopicService topicService;
    @Autowired
    private UserService userService;
    @Autowired
    private TopicTitleService topicTitleService;
    @Autowired
    private TopicDetailMapper detailMapper;
    public int selCountByTId(Integer tid){
        Comment comment = new Comment();
        comment.setTId(tid);
        int i = commentMapper.selectCount(comment);
        return i;
    }
    public Boolean saveComment( Comment comment){
        comment.setChild(false);
        comment.setCreateTime(new Date());
        int insert = commentMapper.insert(comment);
        detailMapper.commTopic(Long.valueOf(comment.getTId()));
        if (insert ==0){
            throw new CommonAdviceException(ExceptioneEnum.COMMENT_SAVE_ERROR);
        }

        if (comment.getParentId()!=0){
            Comment comment1 = new Comment();
            comment1.setId(comment.getParentId());
            comment1.setChild(true);
            commentMapper.updateByPrimaryKeySelective(comment1);
        }
        return true;
    }


    public List<Comment> selCommList(int tid, int pid){
        Example example =new Example(Comment.class);
        //where 条件
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tId",tid);
        criteria.andEqualTo("parentId",pid);

        String orderByClause = "create_time" + " DESC" ;
        example.setOrderByClause(orderByClause);
        Comment comment = new Comment();
        comment.setTId(tid);
        comment.setParentId(pid);
        List<Comment> comments = commentMapper.selectByExample(example);
        for (Comment m : comments){
            deepComm(m,tid);
        }

        return comments;
    }
    public void deepComm( Comment m,int tid){
        User user = userService.searchUserById(m.getUId());
            if (user==null){

            }else {
                m.setUsername(user.getUserName());
                m.setUserpic(user.getAuthorUrl());
            }


            if (m.getChild()==true){
                List<Comment> list = selCommList(tid, m.getId());
                m.setChildren(list);
            }
    }

    public Boolean deleteComment(int id) {
        Comment comment = new Comment();
        comment.setId(id);
        int count = commentMapper.deleteByPrimaryKey(comment);
        if (count==0){
            throw new CommonAdviceException(ExceptioneEnum.DETELE_ERROR);
        }
        return true;
    }

    public int queryCountByUId(int uId) {
        Comment comment = new Comment();
        comment.setUId(uId);
        int i = commentMapper.selectCount(comment);
        return i;
    }
    public  List<UserComment> queryCommentByUId(int uId) {
        Example example = new Example(Comment.class);
        Example.Criteria criteria = example.createCriteria();
        // 排序
        criteria.andEqualTo("uId",uId);
        String orderByClause = "create_time" + " DESC" ;
        example.setOrderByClause(orderByClause);
        List<Comment> select = commentMapper.selectByExample(example);
        System.out.println(select);
        List<UserComment> list = new ArrayList<>();
        for (Comment c : select){
            User user = userService.searchUserById(c.getUId());
            if (c.getTId()==null){
                continue;
            }
            System.out.println(c.getTId());
            TopicTitle topicTitle = topicTitleService.queryTitleByTid(c.getTId());
            Topic topic = topicService.queryMeTopicById(c.getTId());
            UserComment userComment = new UserComment(user,topic,c,topicTitle);
            list.add(userComment);
        }
        return list;
    }



}
