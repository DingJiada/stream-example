package com.shouzhi.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * 个人中心，全部课程VO
 * @author Dingjd
 * @date 2021/3/23 9:06
 */
public class PcAllCoursesVO implements Serializable {

    private static final long serialVersionUID = 1L;
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
     * 上课地点
     **/
    private String schSpaceName;

    /**
     * 课程名称
     **/
    private String courseName;

    /**
     * 上课班级
     **/
    private String schClassNames;

    /**
     * 是否加入直播课表（0：未加入，1：已加入）
     * @ignore
     */
    @JsonIgnore
    private String isJoinLive;

    /**
     * 周数+星期几所对应的日期，如：第18周的星期二对应的日期为yyyy-MM-dd
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date dateForWeeks;

    public PcAllCoursesVO() {}

    public PcAllCoursesVO(Date startTime, Date endTime, String schSpaceName, String courseName, String schClassNames, String isJoinLive, Date dateForWeeks) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.schSpaceName = schSpaceName;
        this.courseName = courseName;
        this.schClassNames = schClassNames;
        this.isJoinLive = isJoinLive;
        this.dateForWeeks = dateForWeeks;
    }

    public String getSchSpaceName() {
        return schSpaceName;
    }

    public void setSchSpaceName(String schSpaceName) {
        this.schSpaceName = schSpaceName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSchClassNames() {
        return schClassNames;
    }

    public void setSchClassNames(String schClassNames) {
        this.schClassNames = schClassNames;
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

    public String getIsJoinLive() {
        return isJoinLive;
    }

    public void setIsJoinLive(String isJoinLive) {
        this.isJoinLive = isJoinLive;
    }

    public Date getDateForWeeks() {
        return dateForWeeks;
    }

    public void setDateForWeeks(Date dateForWeeks) {
        this.dateForWeeks = dateForWeeks;
    }

    @Override
    public String toString() {
        return "PcAllCoursesVO{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", schSpaceName='" + schSpaceName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", schClassNames='" + schClassNames + '\'' +
                ", isJoinLive='" + isJoinLive + '\'' +
                ", dateForWeeks=" + dateForWeeks +
                '}';
    }
}
