package com.shouzhi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shouzhi.basic.utils.UrlUtil;
import com.shouzhi.mq.VideoProcessService;
import com.shouzhi.service.common.RedisTemplateService;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupervisorWebMediaTaskApplicationTests {

    @Autowired  //@Qualifier("customProperties")
            Properties customProperties;
    @Autowired
    VideoProcessService videoProcessService;

    @Test
    public void contextLoads() throws IOException {
        // "app":"live",
        // "stream":"t101",
        // "param":"?serviceType=yjk&examHallCode=E0001&studentCode=XS00231&exp=1592535978338&sign=OTllMTI4MDdhYTIxMjVjNWQ3ODQ3YzUwNjQwMjIzYjk=",
        // "cwd":"/home/admintest/java/srs.oschina/trunk",
        // "file":"./objs/nginx/html/live/E0001_XS00231/2020/08/07/16.11.42.789.flv"


        // 不管有没有对应的业务类型，都将视频移至 recorded/video_flv/dvr/ 文件夹下
        String dvrPath = customProperties.getProperty("cstm.file.dvr.path");
        String file = "./upload/chengdu.flv";
        File srcFile = new File("E:/java/StreamingMedia"+file);
        String dvrRelPath = file.substring(file.indexOf("upload"));
        File destFile = new File(dvrPath+dvrRelPath);
        FileUtils.moveFile(srcFile, destFile);

        String backPath = customProperties.getProperty("cstm.file.back.path");
        String hlsPath = customProperties.getProperty("cstm.file.hls.path");
        String yjkPath = customProperties.getProperty("cstm.service.type.yjk.path");


        //参数解析
        String serviceType = "yjk";
        if("yjk".equals(serviceType)){
            String toMp4 = videoProcessService.flvToMp4(destFile.getAbsolutePath());
            String m3u8Path = hlsPath+yjkPath+dvrRelPath.substring(0, dvrRelPath.lastIndexOf(".flv"))+".m3u8";
            String mp4BackPath = toMp4.replaceFirst(dvrPath, backPath+yjkPath);
            Map<String, String> map = videoProcessService.generateM3u8(toMp4, m3u8Path, mp4BackPath);
        }

    }

}
