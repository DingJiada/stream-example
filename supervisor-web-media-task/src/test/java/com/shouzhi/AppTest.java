package com.shouzhi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author WX
 * @date 2019-12-17 14:52:30
 */
public class AppTest {

    @Test
    public void testOne() throws IOException {
        // "app":"live",
        // "stream":"t101",
        // "param":"?serviceType=yjk&examHallCode=E0001&studentCode=XS00231&exp=1592535978338&sign=OTllMTI4MDdhYTIxMjVjNWQ3ODQ3YzUwNjQwMjIzYjk=",
        // "cwd":"/home/admintest/java/srs.oschina/trunk",
        // "file":"./objs/nginx/html/live/E0001_XS00231/2020/08/07/16.11.42.789.flv"

        //1、解析消息内容，
        // JSONObject jsonObject = JSON.parseObject(msg);

        // 不管有没有对应的业务类型，都将视频移至 recorded/video_flv/dvr/ 文件夹下
        String dvrPath = "E:/java/StreamingMedia/upload/video_flv/dvr/";
        String file = "./srcfile/chengdu.flv";
        File srcFile = new File("E:/java/StreamingMedia"+file);
        String app = file.substring(file.indexOf("srcfile"));
        File destFile = new File(dvrPath+app);
        // FileUtils.moveFile(srcFile, destFile);

        System.out.println(dvrPath+file.substring(1));
    }

}
