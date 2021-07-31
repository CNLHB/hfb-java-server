package com.ssk.hfb.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_topic_detail")
public class TopicDetail {
    @Id
    private Integer tId;
    private String detail;
    private String image;
    private Integer hits;
    private Integer likeNum;
    private Integer treadNum;
    private Integer commentNum;

}
