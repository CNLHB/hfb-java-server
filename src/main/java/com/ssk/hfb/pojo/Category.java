package com.ssk.hfb.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_category")
public class Category {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String name;
    private Date createTime;
    private Boolean status;
    @JsonIgnore
    private Date deleteTime;
}
