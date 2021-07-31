package com.ssk.hfb.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_history")
public class BrowseHistory {
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
