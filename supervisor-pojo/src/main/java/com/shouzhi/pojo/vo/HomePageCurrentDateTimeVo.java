package com.shouzhi.pojo.vo;

/**
 * 首页-当前日期时间模块Vo
 * @author WX
 * @date 2021-01-20 17:07:53
 */
public class HomePageCurrentDateTimeVo {

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 当前教学周
     */
    private String currentWeek;

    /**
     * 周几，中文
     */
    private String weekCn;

    /**
     * 阳(公)历日期
     */
    private String gDate;

    /**
     * 阴(农)历日期
     */
    private String lDate;

    /**
     * 用户姓名
     */
    private String personName;

    /**
     * 系统平台名称
     */
    private String sysPlatformName;

    /**
     * 上次登录时间
     */
    private String lastLoginTime;

    public HomePageCurrentDateTimeVo() {
    }

    public HomePageCurrentDateTimeVo(String schoolName, String currentWeek, String weekCn, String gDate, String lDate, String personName, String sysPlatformName, String lastLoginTime) {
        this.schoolName = schoolName;
        this.currentWeek = currentWeek;
        this.weekCn = weekCn;
        this.gDate = gDate;
        this.lDate = lDate;
        this.personName = personName;
        this.sysPlatformName = sysPlatformName;
        this.lastLoginTime = lastLoginTime;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(String currentWeek) {
        this.currentWeek = currentWeek;
    }

    public String getWeekCn() {
        return weekCn;
    }

    public void setWeekCn(String weekCn) {
        this.weekCn = weekCn;
    }

    public String getgDate() {
        return gDate;
    }

    public void setgDate(String gDate) {
        this.gDate = gDate;
    }

    public String getlDate() {
        return lDate;
    }

    public void setlDate(String lDate) {
        this.lDate = lDate;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getSysPlatformName() {
        return sysPlatformName;
    }

    public void setSysPlatformName(String sysPlatformName) {
        this.sysPlatformName = sysPlatformName;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
