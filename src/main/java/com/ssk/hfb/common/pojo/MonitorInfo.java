package com.ssk.hfb.common.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_log")
public class MonitorInfo {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long uId;
    private String kind;
    private String type;
    private String errorType;
    private String filename;
    private String userAdress;
    private String userIp;
    private String tagName;
    private String selector;
    private String position;
    private String message;
    private String stack;
    private String pathname;
    private String response;
    private String params;
    private String status;
    private String userAgent;
    private String url;
    private String title;
    private String timestamp;
    private String largestContentfulPaint;
    private String firstContentfulPaint;
    private String firstMeaningfulPaint;
    private String firstPaint;
    private String inputDelay;
    private String duration;
    private String startTime;
    private String connectTime;
    private String ttfbTime;
    private String dnsTime;
    private String responseTime;
    private String parseDomTime;
    private String domContentLoadedTime;//dom_dontent_loaded_time
    private String timeToInteractive;
    private String loadTime;
    private Date createTime;

}
