package com.ssk.hfb.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_access")
public class Access {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private String day;
    private Date createTime;

}
