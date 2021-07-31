package com.ssk.hfb.mapper;

import com.ssk.hfb.common.mapper.BaseMapper;
import com.ssk.hfb.common.pojo.MonitorInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MonitorInfoMapper extends BaseMapper<MonitorInfo> {
    @Select("select COUNT(*) as total,url\n" +
            "from t_log\n" +
            "where url not  like '%?%' and url not like '%#%'  and url not like '%localhost%'\n" +
            "GROUP BY url")
    List<Map<String,Object>> queryUrlCountList();
    @Select("select COUNT(*) as total,url\n" +
            "from t_log\n" +
            "where type='error' and url not  like '%?%' and url not like '%#%'  and url not like '%localhost%'\n" +
            "GROUP BY url")
    List<Map<String,Object>> queryErrorUrlCountList();
    @Select("SELECT count( * ) as total,error_type\n" +
            "FROM t_log\n" +
            "WHERE type=\"error\"\n" +
            "AND LEFT(create_time,#{ttype})=#{tday}" +
            "AND url like  CONCAT('%',CONCAT(#{turl},'%')) GROUP BY error_type")
    List<Map<String,Object>> queryErrorCountList(@Param("ttype") int ttype,@Param("tday") String tday,@Param("turl") String turl);
    @Select("select id,error_type,filename,url,message,tag_name,user_adress,create_time\n" +
            "from t_log\n" +
            "where type='error' \n" +
            "and url not  like '%?%' \n" +
            "and url not like '%#%'  \n" +
            "AND url like CONCAT('%',CONCAT(#{turl},'%')) AND LEFT(create_time,#{ttype})=#{tday}")
    List<Map<String,Object>> queryErrorUrlList(@Param("ttype") int ttype,@Param("tday") String tday,@Param("turl") String turl);
    @Select("SELECT IFNULL(COUNT(t.create_time), 0) AS total,\n" +
            "IFNULL(error_type, 'jsError') as error_type,\n" +
            "DATE_FORMAT(c.datelist, '%Y-%m-%d') AS time\n" +
            "FROM t_log t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d')AND t.type = 'error' AND url=#{turl}\n" +
            "WHERE c.datelist < CURDATE() AND c.datelist >= DATE_SUB(CURDATE(), INTERVAL #{tday} DAY) \n" +
            "GROUP BY c.datelist,error_type")
    List<Map<String,Object>> queryByWeekError(@Param("tday") int tday,@Param("turl") String turl);
    @Select("SELECT count(*) AS total,\n" +
            "Right(DATE_FORMAT(create_time, '%Y-%m-%d %h'),2) as time,\n" +
            "error_type\n" +
            "FROM t_log t\n" +
            "WHERE t.type = 'error'\n" +
            "AND DATE_FORMAT(create_time, '%Y-%m-%d')= #{tday}\n" +
            "AND url=#{turl}\n" +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d %h'),error_type")
    List<Map<String,Object>> queryByDayError(@Param("tday") String tday,@Param("turl") String turl);
    @Select("SELECT IFNULL(COUNT(t.create_time), 0) AS total,\n" +
            "t.type as type,\n" +
            "IFNULL(avg(t.load_time), 0) as load_time,\n" +
            "IFNULL(avg(t.ttfb_time), 0) as ttfb_time,\n" +
            "IFNULL(avg(t.connect_time), 0) as connect_time,\n" +
            "IFNULL(avg(t.response_time), 0)  as response_time,\n" +
            "IFNULL(avg(t.parse_dom_time), 0)  as parse_dom_time,\n" +
            "IFNULL(avg(t.dom_content_loaded_time), 0)  as dom_content_loaded_time,\n" +
            "IFNULL(avg(t.time_to_interactive), 0) as time_to_interactive,\n" +
            "IFNULL(avg(t.dns_time), 0) as dns_time,\n" +
            "DATE_FORMAT(c.datelist, '%Y-%m-%d') AS current_day\n" +
            "FROM t_log t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d')AND t.type = 'timing' AND t.url=#{turl}\n" +
            "WHERE c.datelist < CURDATE() AND c.datelist >= DATE_SUB(CURDATE(), INTERVAL #{tday} DAY) \n" +
            "GROUP BY c.datelist")
    List<Map<String,Object>> queryPageByWeekPerforMance(@Param("tday") int tday,@Param("turl") String turl);
    @Select("SELECT IFNULL(COUNT(t.create_time), 0) AS total,\n" +
            "error_type\n" +
            "FROM t_log t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d')\n" +
            "AND t.type = 'error'\n" +
            "AND url=#{turl}\n" +
            "WHERE c.datelist < CURDATE() AND c.datelist >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) \n" +
            "GROUP BY t.error_type\n" +
            "HAVING t.error_type is not NULL")
    List<Map<String,Object>> queryWeekError(@Param("turl") String turl);

    @Select("select t.id,error_type,filename,url,message,tag_name,user_adress,create_time\n" +
            "FROM t_log t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d')\n" +
            "AND t.type = 'error'\n" +
            "AND url=#{turl}\n" +
            "WHERE c.datelist < CURDATE() AND c.datelist >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)  and t.type='error'\n")
    List<Map<String,Object>> queryWeekErrorUrlList(@Param("turl") String turl);
    @Select("SELECT IFNULL(COUNT(t.create_time), 0) AS total,\n" +
            "IFNULL(error_type, 'jsError') as error_type,\n" +
            "DATE_FORMAT(c.datelist, '%Y-%m') AS time\n" +
            "FROM t_log t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d')\n" +
            "AND t.type = 'error' AND t.url=#{turl}\n" +
            "WHERE DATE_FORMAT(c.datelist, '%Y')=#{tyear}\n" +
            "GROUP BY DATE_FORMAT(c.datelist, '%Y-%m'),error_type")
    List<Map<String,Object>> queryYearErrorCount(@Param("tyear") String tyear,@Param("turl") String turl);


    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d %H:00:00') AS time, \n" +
            "COUNT(*) AS total,\n" +
            "IFNULL(avg(ttfb_time), 0) as ttfb_time,\n" +
            "IFNULL(avg(connect_time), 0) as connect_time,\n" +
            "IFNULL(avg(response_time), 0)  as response_time,\n" +
            "IFNULL(avg(parse_dom_time), 0)  as parse_dom_time,\n" +
            "IFNULL(avg(dom_content_loaded_time), 0)  as dom_content_loaded_time,\n" +
            "IFNULL(avg(time_to_interactive), 0) as time_to_interactive,\n" +
            "IFNULL(avg(dns_time), 0) as dns_time,\n" +
            "IFNULL(avg(load_time), 0) as load_time\n" +
            "FROM t_log\n" +
            "WHERE DATE_FORMAT(create_time, '%Y-%m-%d')=#{tday} AND type = 'timing' AND url=#{turl}\n" + //like '%?%'
            "GROUP BY time")
    List<Map<String,Object>> queryDayHourAvgPagePerforMance(@Param("tday") String tday,@Param("turl") String turl);
    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') AS time, COUNT(*) AS total,\n" +
            "round(IFNULL(avg(ttfb_time), 0)) as ttfb_time,\n" +
            "round(IFNULL(avg(connect_time), 0))as connect_time,\n" +
            "round(IFNULL(avg(response_time), 0) ) as response_time,\n" +
            "round(IFNULL(avg(parse_dom_time), 0) ) as parse_dom_time,\n" +
            "round(IFNULL(avg(dom_content_loaded_time), 0))  as dom_content_loaded_time,\n" +
            "round(IFNULL(avg(time_to_interactive), 0)) as time_to_interactive,\n" +
            "round(IFNULL(avg(dns_time), 0)) as dns_time,\n" +
            "round(IFNULL(avg(load_time), 0)) as load_time\n" +
            "FROM t_log \n" +
            "WHERE DATE_FORMAT(create_time, '%Y-%m-%d')=#{tday} AND type = 'timing' AND url=#{turl}\n" +
            "GROUP BY time")
    List<Map<String,Object>> queryDayPagePerforMance(@Param("tday") String tday,@Param("turl") String turl);
    @Select("SELECT DATE_FORMAT(t.create_time, '%Y-%m') AS time, \n" +
            "round(IFNULL(avg(ttfb_time), 0)) as ttfb_time,\n" +
            "round(IFNULL(avg(connect_time), 0))as connect_time,\n" +
            "round(IFNULL(avg(response_time), 0) ) as response_time,\n" +
            "round(IFNULL(avg(parse_dom_time), 0) ) as parse_dom_time,\n" +
            "round(IFNULL(avg(dom_content_loaded_time), 0))  as dom_content_loaded_time,\n" +
            "round(IFNULL(avg(time_to_interactive), 0)) as time_to_interactive,\n" +
            "round(IFNULL(avg(dns_time), 0)) as dns_time,\n" +
            "round(IFNULL(avg(load_time), 0)) as load_time " +
            "FROM t_log t\n" +
            "RIGHT JOIN calendar c ON DATE_FORMAT(c.datelist, '%Y-%m-%d') = DATE_FORMAT(t.create_time, '%Y-%m-%d') \n" +
            "WHERE c.datelist < CURDATE()\n" +
            "AND c.datelist >= DATE_SUB(CURDATE(), INTERVAL #{tday} DAY)\n" +
            "AND type = 'timing' \n" +
            "AND url=#{turl} \n" +
            "GROUP BY DATE_FORMAT(t.create_time, '%Y')")
    List<Map<String,Object>> queryWeekOrMonthPerforMance(@Param("tday") int tday,@Param("turl") String turl);
}
