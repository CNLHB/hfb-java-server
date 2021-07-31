package com.ssk.hfb.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum TemplateEnum {
    PASSWORD_RESET("577948",1),
    PHONE_REGISTER("577742",2),
    PHONE_LOGIN("577949",1),
    PHONE_RESET("577571",1),
    ;
    private String templateID;
    private Integer time;
}
//577742手机注册验证  577949手机登录  577948密码重置