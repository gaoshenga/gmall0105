package com.dcits;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.dcits.mapper")

public class GmallUserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallUserServerApplication.class, args);
    }

}
