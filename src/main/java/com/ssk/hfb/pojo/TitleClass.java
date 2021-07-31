package com.ssk.hfb.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "t_title_class")
public class TitleClass {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private Long tId;
    private Integer cId;

}
