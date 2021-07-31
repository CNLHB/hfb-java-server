package com.ssk.hfb.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_login_log")
public class LoginLog {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String account;
    private String name;
    private String ip;
    private String adress;
    private Date createTime;

}
