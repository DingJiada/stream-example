package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_examination_hall
 * @author 
 */
public class ExaminationHall implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 考场编号
     */
    private String hallCode;

    /**
     * 该考场考试科目名称
     */
    private String examSubjectsName;

    /**
     * 考生人数
     */
    private Integer examineeCount;

    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * 考试状态，默认'0'。-1：已结束，0：未开始，1：进行中
     * @ignore
     */
    private String examStatus;

    /**
     * 该考场所属组织
     */
    private String classId;

    /**
     * 学生登录考试时间，是个数字，比如10，代表在考试开始前10分钟之后才允许考生登录
     */
    private Integer studentLoginTime;

    /**
     * 监考员登录监考时间，是个数字，比如10，代表在考试开始前10分钟之后才允许监考员登录监考
     */
    private Integer invigilatorsLoginTime;

    /**
     * 显示专辑，0：不显示 1：显示（每条考场数据对应一个视频集合，即专辑。该考场对应的视频专辑默认是显示，若人为手动删除专辑则show_album置为'0'）
     * @ignore
     */
    private String showAlbum;

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


    /**
     * 专辑名称串，拼接规则"日期_考场编号_科目名称_班级名称"，班级名称为空则最后一个位置为空
     * @ignore
     */
    private String albumName;

    /**
     * 专辑图片url
     * @ignore
     */
    private String albumImgUrl = "albumImg/defaultAlbumImg.png";

    /**
     * 监考老师名称串，多个使用英文逗号分隔
     * @ignore
     */
    private String invigilatorNames;

    /**
     * 监考老师Ids串，多个使用英文逗号分隔
     */
    private String invigilatorIds;

    /**
     * 前端平台-云监考-考场列表需要此字段，后端无任何使用
     * @ignore
     */
    private Integer djsTime = 0;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHallCode() {
        return hallCode;
    }

    public void setHallCode(String hallCode) {
        this.hallCode = hallCode;
    }

    public String getExamSubjectsName() {
        return examSubjectsName;
    }

    public void setExamSubjectsName(String examSubjectsName) {
        this.examSubjectsName = examSubjectsName;
    }

    public Integer getExamineeCount() {
        return examineeCount;
    }

    public void setExamineeCount(Integer examineeCount) {
        this.examineeCount = examineeCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Integer getStudentLoginTime() {
        return studentLoginTime;
    }

    public void setStudentLoginTime(Integer studentLoginTime) {
        this.studentLoginTime = studentLoginTime;
    }

    public Integer getInvigilatorsLoginTime() {
        return invigilatorsLoginTime;
    }

    public void setInvigilatorsLoginTime(Integer invigilatorsLoginTime) {
        this.invigilatorsLoginTime = invigilatorsLoginTime;
    }

    public String getShowAlbum() {
        return showAlbum;
    }

    public void setShowAlbum(String showAlbum) {
        this.showAlbum = showAlbum;
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

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumImgUrl() {
        return albumImgUrl;
    }

    public void setAlbumImgUrl(String albumImgUrl) {
        this.albumImgUrl = albumImgUrl;
    }

    public String getInvigilatorNames() {
        return invigilatorNames;
    }

    public void setInvigilatorNames(String invigilatorNames) {
        this.invigilatorNames = invigilatorNames;
    }

    public String getInvigilatorIds() {
        return invigilatorIds;
    }

    public void setInvigilatorIds(String invigilatorIds) {
        this.invigilatorIds = invigilatorIds;
    }

    public Integer getDjsTime() {
        return djsTime;
    }

    public void setDjsTime(Integer djsTime) {
        this.djsTime = djsTime;
    }
}