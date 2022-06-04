package com.shouzhi.service.common;

import com.shouzhi.basic.constants.FileTypeConstants;
import com.shouzhi.basic.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 文件上传
 * @author WX
 * @date 2020-11-24 11:33:59
 */
@Service("fileUploadService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class FileUploadService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired  //@Qualifier("customProperties")
    private Properties customProperties;

    /**
     * 上传图片
     * @param imgFile 图片文件
     * @param typePath Properties文件内定义的路径，每个文件都有自己的归属类型，比如头像、背景图
     * @param customPath 自定义路径，比如以ID命名文件夹等，可选
     * @return basePath/typePath/[customPath]/文件名称.后缀
     */
    public String uploadImg(MultipartFile imgFile, String typePath, String customPath) throws IOException,IllegalArgumentException {
        // 校验文件合法
        if(imgFile==null||imgFile.isEmpty()){
            throw new IllegalArgumentException("FILE_UPLOAD_EMPTY_ERROR");
        }
        String fileName = imgFile.getOriginalFilename();
        if(!fileName.endsWith(FileTypeConstants.JPG)&&!fileName.endsWith(FileTypeConstants.PNG)
                &&!fileName.endsWith(FileTypeConstants.JPEG)&&!fileName.endsWith(FileTypeConstants.BMP)
                &&!fileName.endsWith(FileTypeConstants.GIF)&&!fileName.endsWith(FileTypeConstants.ICO)){
            throw new IllegalArgumentException("FILE_UPLOAD_TYPE_ERROR");
        }

        String basePath = customProperties.getProperty("file.uploaded.static_resources");
        // 获取文件后缀
        String fileSuffix = imgFile.getOriginalFilename().substring(imgFile.getOriginalFilename().lastIndexOf("."));
        // 获取文件md5值
        String fileMd5Name = FileUtil.getFileMd5(imgFile.getInputStream()) + fileSuffix;
        // customPath最后不带/则追加/，带/则不变
        if(StringUtils.isNotBlank(customPath)&&customPath.length()>=1)
            customPath = "/".equals(customPath.substring(customPath.length()-1))?customPath:customPath+"/";
        // 拼接文件存储相对路径
        String relativePath = StringUtils.isNotBlank(customPath)?typePath+customPath+fileMd5Name:typePath+fileMd5Name;
        // 校验是否有重复文件，有则不再上传，并把该重复路径取出，无则转存文件
        File duplicateFile = FileUtil.duplicateFile4Md5Name(basePath + typePath, fileMd5Name);
        if(duplicateFile!=null){
            relativePath = duplicateFile.getCanonicalPath().replaceAll("\\\\","\\/").replaceFirst(basePath, "/");
        }else {
            // 上传
            String absolutePath = basePath+relativePath;
            File outputFile = new File(absolutePath);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            imgFile.transferTo(outputFile);
        }
        return relativePath;
    }

    /**
     * 上传图片
     * @param imgFiles 图片文件数组
     * @param typePath Properties文件内定义的路径，每个文件都有自己的归属类型，比如头像、背景图
     * @param customPath 自定义路径，比如以ID命名文件夹等，可选
     * @return basePath/typePath/[customPath]/文件名称.后缀
     */
    public List<String> uploadImg(MultipartFile[] imgFiles, String typePath, String customPath) throws IOException,IllegalArgumentException {
        if(imgFiles.length<=0) throw new IllegalArgumentException("FILE_UPLOAD_EMPTY_ERROR");
        List<String> paths = new ArrayList<>();
        for (MultipartFile imgFile : imgFiles) {
            paths.add(this.uploadImg(imgFile, typePath, customPath));
        }
        return paths;
    }


}
