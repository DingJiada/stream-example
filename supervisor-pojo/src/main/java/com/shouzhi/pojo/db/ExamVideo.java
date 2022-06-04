package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_exam_video
 * @author 
 */
public class ExamVideo implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 考场考试id
     */
    private String examinationHallId;

    /**
     * 考场编号
     */
    private String examinationHallCode;

    /**
     * 考生信息id
     */
    private String examineeId;

    /**
     * 考生学号
     */
    private String studentCode;

    /**
     * 原始名称，上传时的名称
     * @ignore
     */
    @JsonIgnore
    private String originalFile;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 视频存储路径
     * @ignore
     */
    private String videoPath;

    /**
     * 视频时长
     */
    private String videoDuration;

    /**
     * 视频封面图片
     */
    private String videoCoverImg;

    /**
     * 视频大小
     */
    private Long videoSize;

    /**
     * 视频状态，1：录制中 2：录制完成 3：切片中 4：切片完成
     */
    private String videoStatus;

    /**
     * 观看次数
     */
    private Long watchCount;

    /**
     * 考试期间是否有异常，0：无异常 1：有异常
     */
    private String examAbnormal;

    /**
     * 考试异常时间点，是个毫秒级时间戳，可换算成#分#秒，提示时需加'约'字，如：约07分21秒前后
     */
    private String abnormalMillis;

    /**
     * 考试异常时间点，是考试当天打点的时间，录制视频内一旦存在时间水印可准确查找
     */
    private Date abnormalTime;

    /**
     * 创建人id，用于连表不用于显示
     * @ignore
     */
    private String createId;

    /**
     * 创建人账号，不建议填名字，账号更改的可能性很小且又比ID稍微形象化
     * @ignore
     */
    private String createBy;

    /**
     * 创建方式（0：程序代码创建，1：导入创建）
     * @ignore
     */
    @JsonIgnore
    private String createWay;

    /**
     * 创建时间
     * @ignore
     */
    private Date createTime;

    /**
     * 备注
     * @ignore
     */
    private String remark;

    /**
     * 是否删除（0：未删除，1：删除）
     * @ignore
     */
    @JsonIgnore
    private String isDelete;

    /**
     * 删除时间
     * @ignore
     */
    @JsonIgnore
    private Date deleteTime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExaminationHallId() {
        return examinationHallId;
    }

    public void setExaminationHallId(String examinationHallId) {
        this.examinationHallId = examinationHallId;
    }

    public String getExaminationHallCode() {
        return examinationHallCode;
    }

    public void setExaminationHallCode(String examinationHallCode) {
        this.examinationHallCode = examinationHallCode;
    }

    public String getExamineeId() {
        return examineeId;
    }

    public void setExamineeId(String examineeId) {
        this.examineeId = examineeId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String originalFile) {
        this.originalFile = originalFile;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoCoverImg() {
        return videoCoverImg;
    }

    public void setVideoCoverImg(String videoCoverImg) {
        this.videoCoverImg = videoCoverImg;
    }

    public Long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(Long videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(String videoStatus) {
        this.videoStatus = videoStatus;
    }

    public Long getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(Long watchCount) {
        this.watchCount = watchCount;
    }

    public String getExamAbnormal() {
        return examAbnormal;
    }

    public void setExamAbnormal(String examAbnormal) {
        this.examAbnormal = examAbnormal;
    }

    public String getAbnormalMillis() {
        return abnormalMillis;
    }

    public void setAbnormalMillis(String abnormalMillis) {
        this.abnormalMillis = abnormalMillis;
    }

    public Date getAbnormalTime() {
        return abnormalTime;
    }

    public void setAbnormalTime(Date abnormalTime) {
        this.abnormalTime = abnormalTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateWay() {
        return createWay;
    }

    public void setCreateWay(String createWay) {
        this.createWay = createWay;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }


}