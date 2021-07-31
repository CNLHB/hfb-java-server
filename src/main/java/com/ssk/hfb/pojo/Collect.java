package com.ssk.hfb.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
@Data
@Table(name = "t_collect")
public class Collect {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private Long tId;
    private Integer cId;
    private Integer uId;
    private String username;
    private String userpic;
    private String title;
    private Date createTime;
    private Date deleteTime;
}
