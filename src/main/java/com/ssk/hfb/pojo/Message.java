package com.ssk.hfb.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_message")
public class Message {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonProperty(value = "cId")
    private Integer cId;
    private Integer fromId;
    private Integer toId;
    private Integer belong;
    private String message;
    private Boolean status;
    private Date sendTime;
    @JsonIgnore
    private Date deleteTime;

}
