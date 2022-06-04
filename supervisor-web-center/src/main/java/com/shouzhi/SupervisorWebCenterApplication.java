package com.shouzhi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * springboot 启动器
 * @author WX
 * @date 2019-10-09 10:36:32
 */

@SpringBootApplication
@MapperScan("com.shouzhi.mapper") //@MapperScan 用于扫描MyBatis的Mapper接口，并且根据接口生成代理对象
@EnableTransactionManagement
public class SupervisorWebCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupervisorWebCenterApplication.class, args);
    }

}
