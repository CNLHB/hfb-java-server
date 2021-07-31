package com.ssk.hfb.common.vo;

import com.ssk.hfb.pojo.Comment;
import com.ssk.hfb.pojo.Topic;
import com.ssk.hfb.pojo.TopicTitle;
import com.ssk.hfb.pojo.User;
import lombok.Data;

import java.util.Date;
@Data
public class UserComment {
    private Integer id;
    private String username;
    private String userpic;
    private String topicpic;
    private Date createTime;
    private String comment;
    private String topicTitle;
    private String title;
    public UserComment(User user, Topic topic, Comment comment, TopicTitle title){
        this.id = topic.getId();
        this.username = user.getUserName();
        this.userpic =user.getAuthorUrl();
        this.topicpic = topic.getImages();
        this.createTime = comment.getCreateTime();
        this.comment = comment.getContent();
        this.topicTitle = topic.getTitle();
        this.title = title.getTitle();

    }
}
