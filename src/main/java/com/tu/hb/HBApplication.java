package com.tu.hb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.tu.hb.mapper")
@EnableScheduling
public class HBApplication {

    public static void main(String[] args) {
        SpringApplication.run(HBApplication.class, args);
    }

}
