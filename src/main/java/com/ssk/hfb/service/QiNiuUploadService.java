package com.ssk.hfb.service;

import com.google.gson.Gson;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class QiNiuUploadService {
    @Autowired
    UploadProperties prop;
    private String URL = ""; //地址前缀
    private String ACCESS_KEY = "";//ACCESS_KEY
    private String SECRET_KEY = "";//SECRET_KEY
    private String BUCKET = "vboy";  //空间名称
    //构造一个带指定 Region 对象的配置类
    Configuration cfg = new Configuration(Region.region2());
    public String qiNiuUpload(MultipartFile file){
        // 1、文件信息校验
        // 1)校验文件类型
        String type = file.getContentType();
        if (!prop.getQiniu().contains(type)) {
            log.info("上传失败，文件类型不匹配：{}", type);
            throw new CommonAdviceException(ExceptioneEnum.INVALID_FILE_TYPE);
        }

        // 2.1、获取文件后缀名
        String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        //...其他参数参考类注释Region.region2(), Region.huanan()
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = System.currentTimeMillis() + file.getOriginalFilename();
        try {
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String upToken = auth.uploadToken(BUCKET);
            Response response = uploadManager.put(file.getInputStream(),key,upToken,null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.hash);
            System.out.println(putRet);

            return URL + putRet.key;
        } catch (IOException e) {
            log.error("文件上传失败：" + e.getMessage());
            return "文件上传失败";

        }
    }
    public List<String> qiNiuUploads(MultipartFile[] files) {
        // 1、文件信息校验
        // 1)校验文件类型
        List<String> imgs = new ArrayList<>();
        for (MultipartFile file : files) {
            System.out.println(file.getSize());
            String type = file.getContentType();
            if (!prop.getQiniu().contains(type)) {
                log.info("上传失败，文件类型不匹配：{}", type);
                throw new CommonAdviceException(ExceptioneEnum.INVALID_FILE_TYPE);
            }
            // 2.1、获取文件后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            //...其他参数参考类注释Region.region2(), Region.huanan()
            UploadManager uploadManager = new UploadManager(cfg);
            //...生成上传凭证，然后准备上传
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = System.currentTimeMillis() + file.getOriginalFilename();
            try {
                Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
                String upToken = auth.uploadToken(BUCKET);
                Response response = uploadManager.put(file.getInputStream(), key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.hash);
                System.out.println(putRet);
                imgs.add(URL + putRet.key);
            } catch (IOException e) {
                log.error("文件上传失败：" + e.getMessage());

            }
        }
        return imgs;
    }



    public Boolean deleteFile(String key){
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(BUCKET, key);
            return true;
        } catch ( QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
            return false;
        }

    }

    public Boolean deleteBatchFile(String[] keys) {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //单次批量请求的文件数量不得超过1000
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(BUCKET, keys);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keys.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keys[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                }
            }
            return true;
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
            return false;
        }

}

}
