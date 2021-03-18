package com.shouzhi.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 学校基础课程表直播源视图VO
 * @author 
 */
public class SchCourseTableBaseLiveSourceVo implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;


    /**
     * 课程名称
     */
    private String courseName;


    /**
     * 上课人数，如：45
     */
    private Integer peopleNum;


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
    private Integer week;

    /**
     * 周几，星期几，view视图字段，如：周一、周二、周日等
     */
    private String weekV;

    /**
     * 周数，如：1、2、5、18等 (选用，因为下边已经提供组装好的List，见liveWeekList)
     */
    private String weeks;

    /**
     * 周数，view视图字段，如：1-2、5-18等
     */
    private String weeksV;


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
     * 学校年级班级名称
     * @ignore
     */
    private String schClassNames;


    /**
     * 直播周数，是个数组列表，如：[1,2,5,18]
     */
    private List<String> liveWeekList;

    /**
     * 录制周数，是个数组列表，如：[1,2,5,18]
     */
    private List<String> recWeekList;


    /**
     * 直播周数容器，前端-后台管理-直播管理-制定计划-自定义直播计划需要此字段，后端无任何使用
     * @ignore
     */
    private List<String> liveWeeks;

    /**
     * 录制周数容器，前端-后台管理-直播管理-制定计划-自定义直播计划需要此字段，后端无任何使用
     */
    private List<String> recWeeks;


    public SchCourseTableBaseLiveSourceVo() {
    }

    public SchCourseTableBaseLiveSourceVo(String id, String courseName, Integer peopleNum, String sectionNumV, Date startTime, Date endTime, Integer week, String weekV, String weeks, String weeksV, String sysPersonName, String sysDepName, String schSpaceName, String schClassNames, List<String> liveWeekList, List<String> recWeekList) {
        this.id = id;
        this.courseName = courseName;
        this.peopleNum = peopleNum;
        this.sectionNumV = sectionNumV;
        this.startTime = startTime;
        this.endTime = endTime;
        this.week = week;
        this.weekV = weekV;
        this.weeks = weeks;
        this.weeksV = weeksV;
        this.sysPersonName = sysPersonName;
        this.sysDepName = sysDepName;
        this.schSpaceName = schSpaceName;
        this.schClassNames = schClassNames;
        this.liveWeekList = liveWeekList;
        this.recWeekList = recWeekList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getSchClassNames() {
        return schClassNames;
    }

    public void setSchClassNames(String schClassNames) {
        this.schClassNames = schClassNames;
    }


    public List<String> getLiveWeekList() {
        return liveWeekList;
    }

    public void setLiveWeekList(List<String> liveWeekList) {
        this.liveWeekList = liveWeekList;
    }

    public List<String> getRecWeekList() {
        return recWeekList;
    }

    public void setRecWeekList(List<String> recWeekList) {
        this.recWeekList = recWeekList;
    }

    public List<String> getLiveWeeks() {
        return liveWeeks;
    }

    public void setLiveWeeks(List<String> liveWeeks) {
        this.liveWeeks = liveWeeks;
    }

    public List<String> getRecWeeks() {
        return recWeeks;
    }

    public void setRecWeeks(List<String> recWeeks) {
        this.recWeeks = recWeeks;
    }
}