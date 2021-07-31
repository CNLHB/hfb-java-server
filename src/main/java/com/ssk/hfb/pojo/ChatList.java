package com.ssk.hfb.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Table(name="t_chat_list")
public class ChatList {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private Boolean status;
    private Date createTime;
    private Date afterTime;
    @Transient
    private String username;
    @Transient
    private String userpic;
    @Transient
    private List<Message> messages;

}
