package com.shouzhi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取resource下的.properties文件，将文件中的内容封装到map中，注入到bean中方便依赖注入
 * @author WX
 * @date 2019-10-17 18:50:50
 */
@Configuration  //让SpringBoot启动时自动加载该类
public class PropertiesClassLoader {

    @Bean//(name = "customProperties")
    public Properties customProperties() {
        Properties properties = new Properties();
        try {
            //classpath:
            InputStream in = PropertiesClassLoader.class.getClassLoader().getResourceAsStream("custom.properties");
            properties.load(in);
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        return properties;
    }
}
