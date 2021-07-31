package com.ssk.hfb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "hfb.upload")
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;
    private List<String> allowFileTypes;
    private List<String> qiniu;

}
