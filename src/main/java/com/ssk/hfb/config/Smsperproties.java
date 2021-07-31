package com.ssk.hfb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hfb.sms")
public class Smsperproties {
    private String secretId;//密钥对 secretId 和 secretKey
    private String secretKey;
    private String sign;
    private String appid;
}
