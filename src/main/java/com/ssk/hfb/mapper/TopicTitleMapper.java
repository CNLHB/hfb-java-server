package com.ssk.hfb.mapper;

import com.ssk.hfb.common.mapper.BaseMapper;
import com.ssk.hfb.pojo.TopicTitle;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface TopicTitleMapper extends BaseMapper<TopicTitle> {
    @Insert("update t_topic_title set total=total+1 where id = #{id} ")
    int updateTotalById(@Param("id") Long cid);
    @Select("SELECT COUNT(*) FROM t_topic_title where DATE_SUB(CURDATE(), INTERVAL #{num} DAY) <= date(create_time)")
    int queryCountByTime(@Param("num") int num);
    @Select("    select COUNT(*) as total,tc.name\n" +
            "    from t_topic_title t,t_category tc\n" +
            "    WHERE t.c_id=tc.id\n" +
            "    GROUP BY tc.name")
    List<Map<String,Object>> queryCategoryTotal();

    @Select(" select count(*) as total from t_topic_title")
    int queryTopicCount();
    @Select(" SELECT IFNULL(COUNT(t.create_time), 0) AS total,DATE_FORMAT(c.datelist, '%Y-%m-%d') AS current_day\n" +
            "FROM t_topic_title t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d')\n" +
            "WHERE c.datelist < CURDATE()\n" +
            "AND c.datelist >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)\n" +
            "GROUP BY c.datelist")
    List<Map<String,Object>> queryTopicCountWeek();
    @Select(" SELECT IFNULL(COUNT(t.create_time), 0) AS total,DATE_FORMAT(c.datelist, '%Y-%m-%d') AS current_day\n" +
            "FROM t_topic_title t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d')\n" +
            "WHERE c.datelist < CURDATE()\n" +
            "AND c.datelist >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)\n" +
            "GROUP BY c.datelist")
    List<Map<String,Object>> queryTopicCountMonth();

    @Select("SELECT\n" +
            "\tDATE_FORMAT( create_time, \"%Y-%m\" ) AS months,\n" +
            "\tcount( id ) AS total \n" +
            "FROM\n" +
            "\tt_topic_title\n" +
            "GROUP BY\n" +
            "\tmonths;")
    List<Map<String,Object>> queryTopicCountYear();

}
