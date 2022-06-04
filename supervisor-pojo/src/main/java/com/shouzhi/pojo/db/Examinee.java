package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_examinee
 * @author 
 */
public class Examinee implements Serializable {
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
     * 学号
     */
    private String studentCode;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学生考试状态，默认'0'。0：未参加，1：已参加（此状态由客户端去控制）
     * @ignore
     */
    private String studentStatus;

    /**
     * 登陆时间
     * @ignore
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date loginTime;

    /**
     * 退出时间
     * @ignore
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date exitTime;

    /**
     * 直播地址推流，流媒体推流地址应为“固定前缀+hall_code+'_'+student_code”
     * @ignore
     */
    private String liveAddressPush;

    /**
     * 直播地址拉流，流媒体推流地址应为“固定前缀+hall_code+'_'+student_code”
     * @ignore
     */
    private String liveAddressPull;

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
     * 查询条件
     * @ignore
     */
    @JsonIgnore
    private String studentCodeOrName;

    /**
     * 用户头像，用于前端平台监考在线列表
     * @ignore
     */
    private String headImgUrl;

    /**
     * 考试期间是否有异常，0：无异常 1：有异常(前端自己使用，用于异常后打点，冒黄灯。后端无任何依赖)
     * @ignore
     */
    private String examAbnormal = "0";


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

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public String getLiveAddressPush() {
        return liveAddressPush;
    }

    public void setLiveAddressPush(String liveAddressPush) {
        this.liveAddressPush = liveAddressPush;
    }

    public String getLiveAddressPull() {
        return liveAddressPull;
    }

    public void setLiveAddressPull(String liveAddressPull) {
        this.liveAddressPull = liveAddressPull;
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

    public String getStudentCodeOrName() {
        return studentCodeOrName;
    }

    public void setStudentCodeOrName(String studentCodeOrName) {
        this.studentCodeOrName = studentCodeOrName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getExamAbnormal() {
        return examAbnormal;
    }

    public void setExamAbnormal(String examAbnormal) {
        this.examAbnormal = examAbnormal;
    }
}