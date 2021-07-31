package com.ssk.hfb.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name="t_topic_active")
public class TopicActive {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private Integer tId;
    private Integer uId;
    private Integer tActive;
    private Integer tUid;
}
