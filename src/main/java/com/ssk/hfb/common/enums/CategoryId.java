package com.ssk.hfb.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum CategoryId {
    PASSWORD_RESET(1),
    PHONE_REGISTER(2),
    PHONE_LOGIN(3),
    PHONE_RESET(4),
    ;
    private Integer cId;

}
