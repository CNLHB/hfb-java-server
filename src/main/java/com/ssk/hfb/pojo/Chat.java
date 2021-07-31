package com.ssk.hfb.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "t_message")
public class Chat {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String message;
    private Integer fromId;
    private Integer toId;
    private Boolean status;
    private Data sendTime;

}
