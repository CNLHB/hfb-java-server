package com.ssk.hfb.utils;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 *
 * @Description：IP工具类
 * @author zrt
 * @date 2018年9月22日 上午10:38:13
 */
@Slf4j
public class AddressUtils {
    /**
     * @Description：获取客户端的IP
     * @author zrt
     * @date 2018年9月22日 上午10:39:44
     */
    public static String getIpAddress(HttpServletRequest request) {
        //注意本地测试时，浏览器请求不要用localhost，要用本机IP访问项目地址，不然这里取不到ip
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
        if (log.isInfoEnabled()) {
//            log.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
//        log.info(publicIp);
        return ip;
    }


    /**
     * @Description：获取客户端外网ip
     * @Author：zrt
     * @Date：2019/6/13 11:23
     **/
    public static String getPublicIp() {
        try {
            String path = "http://ip.chinaz.com/";// 要获得html页面内容的地址

            URL url = new URL(path);// 创建url对象

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 打开连接

            conn.setRequestProperty("contentType", "GBK"); // 设置url中文参数编码

            conn.setConnectTimeout(5 * 1000);// 请求的时间

            conn.setRequestMethod("GET");// 请求方式

            InputStream inStream = conn.getInputStream();
            // readLesoSysXML(inStream);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    inStream, "GBK"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            // 读取获取到内容的最后一行,写入
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            String str = buffer.toString();
            String ipString1 = str.substring(str.indexOf("["));
            // 获取你的IP是中间的[182.149.82.50]内容
            String ipsString2 = ipString1.substring(ipString1.indexOf("[") + 1,
                    ipString1.lastIndexOf("]"));
            //获取当前IP地址所在地址
      /* String ipsString3=ipString1.substring(ipString1.indexOf(": "),ipString1.lastIndexOf("</center>"));
         System.err.println(ipsString3);*/

            // 返回公网IP值
            return ipsString2;

        } catch (Exception e) {
            System.out.println("获取公网IP连接超时");
            return "连接超时";
        }
    }

    public static String getAdress(String ipAddr) {
        //119.125.107.206
        String url = "http://api.map.baidu.com/location/ip?ak="+"ak地址"+"&ip=" + ipAddr + "&coor=bd09ll";//HTTP协议 ";
//        String url = "http://api.map.baidu.com/location/ip?ak=cXOksuw9T7E6iVYdith7qPlrbW2o3fKc&ip=119.125.107.206&coor=bd09ll";//HTTP协议 ";
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes);
            JSONObject demoJson = JSONObject.fromObject(message);
            if (demoJson.get("status").equals("2")||demoJson.get("status").equals(2)){
                log.error("ip地址是内网地址");
                return "";
            }
            String adress = JSONObject.fromObject(demoJson.get("content")).get("address").toString();
            is.close();
            return adress;

        } catch (Exception e) {
            log.error("null object");
//            e.printStackTrace();
        }
        return "";
    }
}
