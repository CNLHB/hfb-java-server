package com.ssk.hfb.mapper;

import com.ssk.hfb.common.mapper.BaseMapper;
import com.ssk.hfb.pojo.Topic;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface TopicMapper extends BaseMapper<Topic> {
    @Select("SELECT COUNT(*) FROM t_topic where DATE_SUB(CURDATE(), INTERVAL #{num} DAY) <= date(create_time)")
    int queryCountByTime(@Param("num") int num);
    @Select("    select COUNT(*) as total,tc.name\n" +
            "    from t_topic t,t_category tc\n" +
            "    WHERE t.c_id=tc.id\n" +
            "    GROUP BY tc.name")
    List<Map<String,Object>> queryCategoryTotal();

    @Select(" select count(*) as total from t_topic")
    int queryTopicCount();
    @Select(" SELECT IFNULL(COUNT(t.create_time), 0) AS total,DATE_FORMAT(c.datelist, '%Y-%m-%d') AS current_day\n" +
            "FROM t_topic t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d')\n" +
            "WHERE c.datelist < CURDATE()\n" +
            "AND c.datelist >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)\n" +
            "GROUP BY c.datelist")
    List<Map<String,Object>> queryTopicCountWeek();
    @Select(" SELECT IFNULL(COUNT(t.create_time), 0) AS total,DATE_FORMAT(c.datelist, '%Y-%m-%d') AS current_day\n" +
            "FROM t_topic t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d')\n" +
            "WHERE c.datelist < CURDATE()\n" +
            "AND c.datelist >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)\n" +
            "GROUP BY c.datelist")
    List<Map<String,Object>> queryTopicCountMonth();

    @Select("SELECT\n" +
            "\tDATE_FORMAT( create_time, \"%Y-%m\" ) AS months,\n" +
            "\tcount( id ) AS total \n" +
            "FROM\n" +
            "\tt_topic\n" +
            "GROUP BY\n" +
            "\tmonths;")
    List<Map<String,Object>> queryTopicCountYear();
}
