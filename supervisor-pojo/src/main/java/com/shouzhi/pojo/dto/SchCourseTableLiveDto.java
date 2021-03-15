package com.shouzhi.pojo.dto;

import java.io.Serializable;

/**
 * 直播课表DTO，仅用于数据接收，不用做数据响应
 * @author WX
 * @date 2021-03-12 16:45:04
 */
public class SchCourseTableLiveDto implements Serializable {

    /**
     * 学校基础课程表ID，外键
     */
    private String schCourseTableBaseId;

    // /**
    //  * 课堂简介
    //  */
    // private String courseDesc;

    /**
     * 周数，如：1、2、5、18等
     */
    private Integer weeks;

    /**
     * 周几，星期几，如：1、2、7等
     */
    private Integer week;

    /**
     * 是否录制，默认否（0：否，1：是）
     */
    private String isRecord;

    private static final long serialVersionUID = 1L;

    public String getSchCourseTableBaseId() {
        return schCourseTableBaseId;
    }

    public void setSchCourseTableBaseId(String schCourseTableBaseId) {
        this.schCourseTableBaseId = schCourseTableBaseId;
    }

    public Integer getWeeks() {
        return weeks;
    }

    public void setWeeks(Integer weeks) {
        this.weeks = weeks;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(String isRecord) {
        this.isRecord = isRecord;
    }
}
