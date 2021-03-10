package com.shouzhi.mq;

import com.shouzhi.basic.utils.HlsVideoUtil;
import com.shouzhi.basic.utils.Mp4VideoUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author WX
 * @date 2020-02-10 15:03:15
 */
@Service("videoProcessService")
@Transactional(rollbackFor = Exception.class)  //该类下所有的方法都受事务控制
public class VideoProcessService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired  //@Qualifier("customProperties")
    Properties customProperties;

    /**
     * flv转mp4
      * @param flvName 文件全路径名称，如:/recorded/16.11.42.789.flv
     * @return
     */
    public String flvToMp4(String flvName) {
        logger.info("准备进行Flv->转码->Mp4，flvName={}", flvName);
        if(StringUtils.isBlank(flvName)){
            logger.info("flvName为空，Flv->转码->Mp4失败！");
            return null;
        }
        String ffmpegPath = customProperties.getProperty("cstm.ffmpeg.path");

        // 检查文件是否存在
        File fileToDo = new File(flvName);
        if(!fileToDo.exists()){
            logger.info("{}文件不存在！",fileToDo.getAbsolutePath());
            return null;
        }
        // 校验文件后缀，不是flv不进行处理
        if(!fileToDo.isFile()|| !fileToDo.getName().endsWith(".flv")){
            logger.info("{}不是flv文件！",fileToDo.getAbsolutePath());
            return null;
        }
        // 开始转换MP4
        String mp4Name = fileToDo.getName().substring(0,fileToDo.getName().lastIndexOf("."))+".mp4";
        String mp4folder_path = fileToDo.getParent()+"/";
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpegPath,flvName,mp4Name,mp4folder_path);
        String success = mp4VideoUtil.flvToMp4();
        if(!"success".equals(success)){
            logger.info("flvToMp4转换失败！{}",success);
            return null;
        }
        // 移除FLV
        FileUtils.deleteQuietly(fileToDo);
        return (mp4folder_path+mp4Name).replaceAll("\\\\", "/");
    }

    /**
     * mp4生成M3u8
     * @param mp4Name 文件全路径名称，如:/recorded/16.11.42.789.mp4
     * @param m3u8Path 文件全路径名称，如:/recorded/16.11.42.789.m3u8
     * @param mp4BackPath 文件全路径名称，如:/recorded/back/16.11.42.789.mp4
     */
    public Map<String, String> generateM3u8(String mp4Name, String m3u8Path, String mp4BackPath) {
        logger.info("准备进行Mp4->生成->M3u8、封面图、备份Mp4文件，mp4Name={}，m3u8Path={}，mp4BackPath={}", mp4Name,m3u8Path,mp4BackPath);
        try {
            if(StringUtils.isBlank(mp4Name)){
                logger.info("mp4Name为空，生成->M3u8失败！");
                return null;
            }
            // 检查文件是否存在
            File fileToDo = new File(mp4Name);
            if(!fileToDo.exists()){
                logger.info("{}文件不存在！",fileToDo.getAbsolutePath());
                return null;
            }
            // 校验文件后缀，不是mp4不进行处理
            if(!fileToDo.isFile()|| !fileToDo.getName().endsWith(".mp4")){
                logger.info("{}不是mp4文件！",fileToDo.getAbsolutePath());
                return null;
            }

            // 切TS
            //String m3u8_name = "test1.m3u8";
            //     String m3u8folder_path = "D:/BaiduNetdiskDownload/Movies/test1/";
            File m3u8File = new File(m3u8Path);
            String m3u8_name = m3u8File.getName();
            String m3u8_rel_path = m3u8File.getParent()+"/";
            String ffmpegPath = customProperties.getProperty("cstm.ffmpeg.path");
            HlsVideoUtil videoUtil = new HlsVideoUtil(ffmpegPath,fileToDo.getAbsolutePath(),m3u8_name,m3u8_rel_path);
            String tsResult = videoUtil.generateM3u8();
            m3u8File=null;
            if(tsResult == null || !tsResult.equals("success")){
                logger.info("生成m3u8文件失败！{}",tsResult);
                return null;
            }

            // 切封面
            String thumbnailPath = m3u8_rel_path + "thumbnail.png";
            videoUtil.get_video_thumbnail(fileToDo.getAbsolutePath(),"00:00:03","0.001",thumbnailPath);

            // 获取视频时长
            String video_time = videoUtil.get_video_time(fileToDo.getAbsolutePath());
            if(video_time!=null&&video_time.length()>=9&&video_time.contains(".")){
                video_time = video_time.substring(0, video_time.lastIndexOf("."));
            }

            // 移动源文件至备份文件夹
            FileUtils.moveFile(fileToDo, new File(mp4BackPath));

            // 返回 m3u8视频地址、封面地址、视频转换状态、
            Map<String, String> videoInfo = new HashMap<>();
            videoInfo.put("videoTime", video_time);
            videoInfo.put("thumbnailPath", thumbnailPath);
            videoInfo.put("m3u8Path", m3u8_rel_path+m3u8_name);
            return videoInfo;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public long fileSize(String filePath){
        if(StringUtils.isBlank(filePath)){
            logger.info("文件名称为空，获取文件大小失败！");
            return 0;
        }
        File file = new File(filePath);
        if(!file.exists()){
            logger.info("{}文件不存在，获取文件大小失败！",file.getAbsolutePath());
            return 0;
        }
        return FileUtils.sizeOf(file);
    }

}
