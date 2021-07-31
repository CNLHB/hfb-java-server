package com.ssk.hfb.mapper;

import com.ssk.hfb.common.mapper.BaseMapper;
import com.ssk.hfb.pojo.Attention;
import com.ssk.hfb.pojo.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {
//    @Select("SELECT b.* FROM t_comment WHERE id = #{tid} and p_id = #{pid}")
//    List<Comment> selCommentByid(@Param("tid") Integer tid, @Param("pid") Integer pid);
}
