package com.ssk.hfb.mapper;

import com.ssk.hfb.common.mapper.BaseMapper;
import com.ssk.hfb.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT COUNT(*) FROM t_user where DATE_SUB(CURDATE(), INTERVAL #{num} DAY) <= date(create_time)")
    int queryCountByTime(@Param("num") int num);

    @Select(" select count(*) as total from t_user")
    int queryUserCount();
    @Select(" SELECT IFNULL(COUNT(t.create_time), 0) AS total,DATE_FORMAT(c.datelist, '%Y-%m-%d') AS current_day\n" +
            "FROM t_user t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d')\n" +
            "WHERE c.datelist < CURDATE()\n" +
            "AND c.datelist >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)\n" +
            "GROUP BY c.datelist")
    List<Map<String,Object>> queryUserCountWeek();
    @Select(" SELECT IFNULL(COUNT(t.create_time), 0) AS total,DATE_FORMAT(c.datelist, '%Y-%m-%d') AS current_day\n" +
            "FROM t_user t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d')\n" +
            "WHERE c.datelist < CURDATE()\n" +
            "AND c.datelist >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)\n" +
            "GROUP BY c.datelist")
    List<Map<String,Object>> queryUserCountMonth();

    @Select("SELECT\n" +
            "\tDATE_FORMAT( create_time, \"%Y-%m\" ) AS months,\n" +
            "\tcount( id ) AS total \n" +
            "FROM\n" +
            "\tt_user\n" +
            "GROUP BY\n" +
            "\tmonths;")
    List<Map<String,Object>> queryUserCountYear();
}
