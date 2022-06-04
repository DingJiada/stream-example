package com.shouzhi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shouzhi.mapper") //@MapperScan 用于扫描MyBatis的Mapper接口，并且根据接口生成代理对象
public class SupervisorWebMediaTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupervisorWebMediaTaskApplication.class, args);
    }

}
