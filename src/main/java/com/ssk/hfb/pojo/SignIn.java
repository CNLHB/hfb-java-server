package com.ssk.hfb.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_sign_in")
public class SignIn {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private Integer uId;
    private String signIn;
    private String adress;
    private Date createTime;


}