package com.shouzhi.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.shouzhi.basic.utils.UrlUtil;
import com.shouzhi.config.RabbitmqConfig;
import com.shouzhi.pojo.db.ExamVideo;
import com.shouzhi.service.common.RedisTemplateService;
import com.shouzhi.service.interf.db.IExamVideoService;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.util.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.Properties;

/**
 * rabbitmq接收DVR钩子处理中心
 * @author WX
 * @date 2020-08-10 13:59:05
 */
@Component
public class DvrHooksHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired  //@Qualifier("customProperties")
    Properties customProperties;
    @Autowired
    RedisTemplateService redisTemplateService;
    @Autowired
    VideoProcessService videoProcessService;
    @Autowired
    private IExamVideoService examVideoService;


    //使用@RabbitListener注解监听队列
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_MEDIA_SERVER_DVR},containerFactory = "customContainerFactory")
    public void queue_media_server_dvr(String msg, Message message, Channel channel){
        logger.info("QUEUE_MEDIA_SERVER_DVR-接收到消息：{}", msg);
        try {
            // "app":"live",
            // "stream":"t101",
            // "param":"?serviceType=yjk&examHallCode=E0001&studentCode=XS00231&exp=1592535978338&sign=OTllMTI4MDdhYTIxMjVjNWQ3ODQ3YzUwNjQwMjIzYjk=",
            // "cwd":"/home/admintest/java/srs.oschina/trunk",
            // "file":"./objs/nginx/html/live/E0001_XS00231/2020/08/07/16.11.42.789.flv"
            // "serviceParams":"serviceType=kczb&examHallCode=E0001&studentCode=XS00231"

            //1、解析消息内容，
            JSONObject jsonObject = JSON.parseObject(msg);
            // 不管有没有对应的业务类型，都将视频移至 recorded/video_flv/dvr/ 文件夹下
            String dvrPath = customProperties.getProperty("cstm.file.dvr.path");
            String file = jsonObject.getString("file").substring(1);
            File srcFile = new File(jsonObject.getString("cwd")+file);
            String dvrRelPath = file.substring(file.indexOf(jsonObject.getString("app")));
            File destFile = new File(dvrPath+dvrRelPath);
            FileUtils.moveFile(srcFile, destFile);
            logger.info("文件转移成功，原路径={}，新路径={}", srcFile.getAbsolutePath(), destFile.getAbsolutePath());

            String backPath = customProperties.getProperty("cstm.file.back.path");
            String hlsPath = customProperties.getProperty("cstm.file.hls.path");
            String yjkPath = customProperties.getProperty("cstm.service.type.yjk.path");
            String zxxkPath = customProperties.getProperty("cstm.service.type.zxxk.path");
            String nginxVideoPath = customProperties.getProperty("cstm.server.nginx.video.path");

            //业务参数解析
            MultiMap multiMap = UrlUtil.queryStringMap(jsonObject.getString("serviceParams"));
            String serviceType = multiMap.getString("serviceType");
            // 这里是根据制定的规则解析，做相应的业务操作，不然谁也不知道你的这个录制视频是要归属于哪个业务，哪个表，那条记录
            if("yjk".equals(serviceType)){
                logger.info("进入云监考-DVR-考生视频逻辑处理模块");
                String examHallCode = multiMap.getString("examHallCode");
                String studentCode = multiMap.getString("studentCode");
                // 根据参数查询对应的数据是否存在，存在拿到id，
                ExamVideo examVideo = new ExamVideo();
                examVideo.setExaminationHallCode(examHallCode);
                examVideo.setStudentCode(studentCode);
                examVideo = examVideoService.selectOneByParam(examVideo);
                if(examVideo==null){
                    logger.info("云监考-DVR-未找到examinationHallCode={},studentCode={}对应的ExamVideo记录,无法进行录制视频的对标", examHallCode,studentCode);
                    return;
                }
                logger.info("已找到DVR视频对应数据库记录所在目标，id={},examinationHallId={},examinationHallCode={},examineeId={},studentCode={}", examVideo.getId(),examVideo.getExaminationHallId(),examVideo.getExaminationHallCode(),examVideo.getExamineeId(),examVideo.getStudentCode());
                String examVideoId = examVideo.getId();
                examVideo = new ExamVideo();
                examVideo.setId(examVideoId);
                examVideo.setVideoStatus("3");
                examVideoService.updateByPrimaryKeySelective(examVideo);
                // 对刚刚移动的文件 进行转码、切片、切封面、
                String toMp4 = videoProcessService.flvToMp4(destFile.getAbsolutePath());
                String m3u8Path = hlsPath+yjkPath+dvrRelPath.substring(0, dvrRelPath.lastIndexOf(".flv"))+".m3u8";
                String mp4BackPath = toMp4.replaceFirst(dvrPath, backPath+yjkPath);
                Map<String, String> map = videoProcessService.generateM3u8(toMp4, m3u8Path, mp4BackPath);
                long fileSize = videoProcessService.fileSize(mp4BackPath);

                // 根据 上边查询到的id 更新 相关字段
                examVideo.setOriginalFile(mp4BackPath);
                examVideo.setVideoSize(fileSize);
                examVideo.setVideoDuration(map.get("videoTime"));
                examVideo.setVideoCoverImg(nginxVideoPath+map.get("thumbnailPath").replaceFirst(hlsPath,""));
                examVideo.setVideoPath(nginxVideoPath+map.get("m3u8Path").replaceFirst(hlsPath,""));
                examVideo.setVideoStatus("4");
                examVideoService.updateByPrimaryKeySelective(examVideo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("QUEUE_MEDIA_SERVER_DVR-异常捕获：{}", e.getMessage());
        }

    }

}
