package com.ssk.hfb.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_topic_title")
public class TopicTitle {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private Integer cId;
    private Integer uId;
    private String titlePic;
    private String title;
    private Integer total;
    private Boolean display;
    private String description;
    private Date createTime;
    @JsonIgnore
    private Date deleteTime;
}
