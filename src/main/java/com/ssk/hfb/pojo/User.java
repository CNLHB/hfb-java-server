package com.ssk.hfb.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Table(name="t_user")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String userName;
    private String email;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String identity;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    private String phoneNumber;
    private String birthday;
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String openId;
    private String authorUrl;
    private String description;
    private Integer gender;
    private String address;
    private String occupation;
    private Boolean status;
    @Transient
    private Boolean isguanzhu;
    @Transient
    @JsonIgnore
    private String code;
    @JsonIgnore
    private Date createTime;
    @JsonIgnore
    private Date updateTime;
    @JsonIgnore
    private Date deleteTime;
}
