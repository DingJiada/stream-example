package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shouzhi.pojo.vo.SchDeviceOperateVo;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_sch_course_table_base
 * @author 
 */
public class SchCourseTableBase implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 课程代码
     */
    private String courseCode;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程分类，1：公共必修课、2：公共选修课、3：专业必修课、4：专业选修课、5：其它
     */
    private String courseType;

    /**
     * 上课人数，如：45
     */
    private Integer peopleNum;

    /**
     * 节次数，如：1、2、8、12等
     */
    @JsonIgnore
    private String sectionNum;

    /**
     * 节次数，view视图字段，如：1-2、6-8等
     */
    private String sectionNumV;

    /**
     * 开始时间
     */
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * 周几，星期几，如：1、2、7等
     */
    @JsonIgnore
    private Integer week;

    /**
     * 周几，星期几，view视图字段，如：周一、周二、周日等
     */
    private String weekV;

    /**
     * 周数，如：1、2、5、18等
     */
    @JsonIgnore
    private String weeks;

    /**
     * 周数，view视图字段，如：1-2、5-18等
     */
    private String weeksV;

    /**
     * 是否加入直播课表（0：未加入，1：已加入）
     * @ignore
     */
    @JsonIgnore
    private String isJoinLive;

    /**
     * 加入直播课表计划的周数，如：1、2、5、18等
     * @ignore
     */
    @JsonIgnore
    private String joinLiveWeeks;

    /**
     * 是否已经全部加入直播课表（0：未全部加入，1：已全部加入）
     * @ignore
     */
    @JsonIgnore
    private String isJoinedLiveAll;

    /**
     * 系统用户ID
     */
    private String sysUserId;

    /**
     * 学校空间信息id
     */
    private String schSpaceId;

    /**
     * 学校课程类别ID
     */
    private String schCourseCategoryId;

    /**
     * 部门组织id
     */
    private String sysDepartmentId;

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

    /**
     * 学校设备1，含：操作key、设备名称、播放地址（仅前台使用，后台无用）
     * @ignore
     */
    private SchDeviceOperateVo schDevice1;

    /**
     * 学校设备2，含：操作key、设备名称、播放地址（仅前台使用，后台无用）
     * @ignore
     */
    private SchDeviceOperateVo schDevice2;

    /**
     * 学校设备VGA，含：设备名称、播放地址（仅前台使用，后台无用）
     * @ignore
     */
    private SchDeviceOperateVo schDeviceVGA;

    /**
     * 学校设备Flv地址画中画，仅播放地址（仅前台使用，后台无用）
     * @ignore
     */
    private String schDeviceFlvOverlay;

    /**
     * 当前周（仅前台使用，后台无用）
     * @ignore
     */
    private String currentWeek;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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

    public String getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
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

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getWeekV() {
        return weekV;
    }

    public void setWeekV(String weekV) {
        this.weekV = weekV;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getWeeksV() {
        return weeksV;
    }

    public void setWeeksV(String weeksV) {
        this.weeksV = weeksV;
    }

    public String getIsJoinLive() {
        return isJoinLive;
    }

    public void setIsJoinLive(String isJoinLive) {
        this.isJoinLive = isJoinLive;
    }

    public String getJoinLiveWeeks() {
        return joinLiveWeeks;
    }

    public void setJoinLiveWeeks(String joinLiveWeeks) {
        this.joinLiveWeeks = joinLiveWeeks;
    }

    public String getIsJoinedLiveAll() {
        return isJoinedLiveAll;
    }

    public void setIsJoinedLiveAll(String isJoinedLiveAll) {
        this.isJoinedLiveAll = isJoinedLiveAll;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getSchSpaceId() {
        return schSpaceId;
    }

    public void setSchSpaceId(String schSpaceId) {
        this.schSpaceId = schSpaceId;
    }

    public String getSchCourseCategoryId() {
        return schCourseCategoryId;
    }

    public void setSchCourseCategoryId(String schCourseCategoryId) {
        this.schCourseCategoryId = schCourseCategoryId;
    }

    public String getSysDepartmentId() {
        return sysDepartmentId;
    }

    public void setSysDepartmentId(String sysDepartmentId) {
        this.sysDepartmentId = sysDepartmentId;
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

    public SchDeviceOperateVo getSchDevice1() {
        return schDevice1;
    }

    public void setSchDevice1(SchDeviceOperateVo schDevice1) {
        this.schDevice1 = schDevice1;
    }

    public SchDeviceOperateVo getSchDevice2() {
        return schDevice2;
    }

    public void setSchDevice2(SchDeviceOperateVo schDevice2) {
        this.schDevice2 = schDevice2;
    }

    public SchDeviceOperateVo getSchDeviceVGA() {
        return schDeviceVGA;
    }

    public void setSchDeviceVGA(SchDeviceOperateVo schDeviceVGA) {
        this.schDeviceVGA = schDeviceVGA;
    }

    public String getSchDeviceFlvOverlay() {
        return schDeviceFlvOverlay;
    }

    public void setSchDeviceFlvOverlay(String schDeviceFlvOverlay) {
        this.schDeviceFlvOverlay = schDeviceFlvOverlay;
    }

    public String getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(String currentWeek) {
        this.currentWeek = currentWeek;
    }

    @Override
    public String toString() {
        return "SchCourseTableBase{" +
                "id='" + id + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseType='" + courseType + '\'' +
                ", peopleNum=" + peopleNum +
                ", sectionNum='" + sectionNum + '\'' +
                ", sectionNumV='" + sectionNumV + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", week=" + week +
                ", weekV=" + weekV +
                ", weeks='" + weeks + '\'' +
                ", weeksV='" + weeksV + '\'' +
                ", isJoinLive='" + isJoinLive + '\'' +
                ", joinLiveWeeks='" + joinLiveWeeks + '\'' +
                ", isJoinedLiveAll='" + isJoinedLiveAll + '\'' +
                ", sysUserId='" + sysUserId + '\'' +
                ", schSpaceId='" + schSpaceId + '\'' +
                ", schCourseCategoryId='" + schCourseCategoryId + '\'' +
                ", sysDepartmentId='" + sysDepartmentId + '\'' +
                ", createId='" + createId + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createWay='" + createWay + '\'' +
                ", createTime=" + createTime +
                ", remark='" + remark + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", deleteTime=" + deleteTime +
                '}';
    }
}