package com.ssk.hfb.config;

import com.ssk.hfb.common.utils.RsaUtils;
import com.ssk.hfb.common.utils.SystemUtlis;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;
import java.util.List;

/**
 * @Feature: jwt属性
 */
@Data
@ConfigurationProperties(prefix = "hfb.jwt")
public class JwtProperties {
    /**
     * 公钥
     */
    private PublicKey publicKey;
    /**
     * 公钥地址
     */
    private String pubKeyPath;
    private String pubKeyPathLinux;
    private List<String> path;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    /**
     * @PostConstruct :在构造方法执行之后执行该方法
     */
    @PostConstruct
    public void init(){
        try {
            // 获取公钥
            boolean systemLinux = SystemUtlis.isSystemLinux();
            if (systemLinux){
                this.publicKey = RsaUtils.getPublicKey(pubKeyPathLinux);
            }else {
                this.publicKey = RsaUtils.getPublicKey(pubKeyPath);

            }
        } catch (Exception e) {
            logger.error("获取公钥失败！", e);
            throw new RuntimeException();
        }
    }
}
