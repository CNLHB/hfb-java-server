package com.ssk.hfb;

import com.ssk.hfb.config.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import sun.reflect.generics.tree.Tree;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.ssk.hfb.mapper")
public class HfbServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HfbServerApplication.class, args);
    }


}
