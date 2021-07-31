package com.ssk.hfb.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssk.hfb.common.pojo.InfoNum;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Table(name="t_topic")
public class Topic {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private Integer uId;
    private Integer cId;
    private String title;
    private String urlType;
    private String images;
    private Date createTime;
    @Transient
    private String userpic;
    @Transient
    private Integer uGender;
    @Transient
    private String username;
    @Transient
    private Boolean collect;
    @Transient
    private Boolean isguanzhu;
    @Transient
    private Integer commentNum;
    @Transient
    private InfoNum infoNum;
//    @JsonIgnore
    private Boolean display;
    @JsonIgnore
    private Date updateTime;
    @JsonIgnore
    private Date deleteTime;
    @Transient
    private List<Integer> ids;
    @Transient
    private List<TopicTitle> lable;
}
