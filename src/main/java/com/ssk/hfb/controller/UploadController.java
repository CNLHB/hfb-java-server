package com.ssk.hfb.controller;


import com.ssk.hfb.service.QiNiuUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    QiNiuUploadService qiNiu;

    @PostMapping("cloud")
    public String qiniuUploadFile(@RequestParam("file") MultipartFile file){
        return qiNiu.qiNiuUpload(file);
    }
    @PostMapping("clouds")
    public List<String> qiniuUploadFiles(@RequestParam("files") MultipartFile[] files,@RequestParam("file") MultipartFile[] file){

        List<String> list = qiNiu.qiNiuUploads(files);
        System.out.println(list);
        return list;
    }
    @DeleteMapping("file/{id}")
    public Boolean deleteFile(@PathVariable("id") String id){
        return qiNiu.deleteFile(id);
    }
}
