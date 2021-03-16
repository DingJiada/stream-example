package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_sch_course_table_live
 * @author 
 */
public class SchCourseTableLive implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 学校基础课程表ID，外键
     */
    private String schCourseTableBaseId;

    /**
     * 课堂简介
     */
    private String courseDesc;

    /**
     * 周数，如：1、2、5、18等
     */
    private Integer weeks;

    /**
     * 周数+星期几所对应的日期，如：第18周的星期二对应的日期为yyyy-MM-dd
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date dateForWeeks;

    /**
     * 是否录制，默认否（0：否，1：是）
     */
    private String isRecord;

    /**
     * 计划来源，1_1：基础课表自动生成、1_1：基础课表自定义创建、2_1：其它
     */
    private String planForm;

    /**
     * 计划创建人，填名字，如：张三
     */
    private String planCreator;

    /**
     * 是否取消，默认否（0：未取消，1：已取消）
     */
    private String isCancel;

    /**
     * 创建人id，用于连表不用于显示
     * @ignore
     */
    @JsonIgnore
    private String createId;

    /**
     * 创建人账号，不建议填名字，账号更改的可能性很小且又比ID稍微形象化
     * @ignore
     */
    @JsonIgnore
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
    @JsonIgnore
    private Date createTime;

    /**
     * 备注
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

    // 非数据库表字段
    /**
     * 课程名称
     * @ignore
     */
    private String courseName;

    /**
     * 课程分类，1：公共必修课、2：公共选修课、3：专业必修课、4：专业选修课、5：其它
     * @ignore
     */
    private String courseType;

    /**
     * 上课人数，如：45
     * @ignore
     */
    private Integer peopleNum;

    /**
     * 节次数，view视图字段，如：1-2、6-8等
     * @ignore
     */
    private String sectionNumV;

    /**
     * 开始时间
     * @ignore
     */
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    /**
     * 结束时间
     * @ignore
     */
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * 周几，星期几，view视图字段，如：周一、周二、周日等
     * @ignore
     */
    private String weekV;



    /**
     * 系统用户名称
     * @ignore
     */
    private String sysPersonName;

    /**
     * 部门组织名称
     * @ignore
     */
    private String sysDepName;

    /**
     * 学校空间信息名称
     * @ignore
     */
    private String schSpaceName;

    /**
     * 学校课程类别名称
     * @ignore
     */
    private String schCategoryName;

    /**
     * 学校年级班级名称
     * @ignore
     */
    private String schClassNames;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchCourseTableBaseId() {
        return schCourseTableBaseId;
    }

    public void setSchCourseTableBaseId(String schCourseTableBaseId) {
        this.schCourseTableBaseId = schCourseTableBaseId;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public Integer getWeeks() {
        return weeks;
    }

    public void setWeeks(Integer weeks) {
        this.weeks = weeks;
    }

    public Date getDateForWeeks() {
        return dateForWeeks;
    }

    public void setDateForWeeks(Date dateForWeeks) {
        this.dateForWeeks = dateForWeeks;
    }

    public String getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(String isRecord) {
        this.isRecord = isRecord;
    }

    public String getPlanForm() {
        return planForm;
    }

    public void setPlanForm(String planForm) {
        this.planForm = planForm;
    }

    public String getPlanCreator() {
        return planCreator;
    }

    public void setPlanCreator(String planCreator) {
        this.planCreator = planCreator;
    }

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getSectionNumV() {
        return sectionNumV;
    }

    public void setSectionNumV(String sectionNumV) {
        this.sectionNumV = sectionNumV;
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

    public String getWeekV() {
        return weekV;
    }

    public void setWeekV(String weekV) {
        this.weekV = weekV;
    }

    public String getSysPersonName() {
        return sysPersonName;
    }

    public void setSysPersonName(String sysPersonName) {
        this.sysPersonName = sysPersonName;
    }

    public String getSysDepName() {
        return sysDepName;
    }

    public void setSysDepName(String sysDepName) {
        this.sysDepName = sysDepName;
    }

    public String getSchSpaceName() {
        return schSpaceName;
    }

    public void setSchSpaceName(String schSpaceName) {
        this.schSpaceName = schSpaceName;
    }

    public String getSchCategoryName() {
        return schCategoryName;
    }

    public void setSchCategoryName(String schCategoryName) {
        this.schCategoryName = schCategoryName;
    }

    public String getSchClassNames() {
        return schClassNames;
    }

    public void setSchClassNames(String schClassNames) {
        this.schClassNames = schClassNames;
    }
}