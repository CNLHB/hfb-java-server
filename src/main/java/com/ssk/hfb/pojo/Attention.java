package com.ssk.hfb.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Data
@Table(name="t_attention")
public class Attention {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private Integer fromId;
    private Integer toId;
    @Transient
    private String username;
    @Transient
    private String userpic;
    @Transient
    private List<Message> messages;

}
