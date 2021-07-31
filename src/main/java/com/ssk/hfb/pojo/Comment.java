package com.ssk.hfb.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Table(name="t_comment")
public class Comment {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private Integer uId;
    private Integer parentId;
    private Boolean child;
    private Integer tId;
    private String content;
    private Date createTime;
    @Transient
    @JsonIgnore
    private Date deleteTime;
    @Transient
    private String username;
    @Transient
    private String userpic;
    @Transient
    private List<Comment> children;

}
