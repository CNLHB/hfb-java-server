package com.ssk.hfb.pojo;

import lombok.Data;

@Data
public class WinXinEntity {
    private String access_token;
    private String ticket;
    private String noncestr;
    private String timestamp;
    private String str;
    private String signature;
}
