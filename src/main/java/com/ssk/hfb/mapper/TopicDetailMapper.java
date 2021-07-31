package com.ssk.hfb.mapper;

import com.ssk.hfb.common.mapper.BaseMapper;
import com.ssk.hfb.pojo.Topic;
import com.ssk.hfb.pojo.TopicDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface TopicDetailMapper extends BaseMapper<TopicDetail> {
    @Insert("update t_topic_detail set hits=hits+5 ,like_num=like_num+1 where t_id = #{id} ")
    int likeTopic(@Param("id") Long cid);
    @Insert("update t_topic_detail set hits=hits+10 ,comment_num=comment_num+1 where t_id = #{id} ")
    int commTopic(@Param("id") Long cid);
}
