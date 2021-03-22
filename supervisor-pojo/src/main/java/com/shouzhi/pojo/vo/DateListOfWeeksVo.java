package com.shouzhi.pojo.vo;

import java.util.List;
import java.util.Map;

/**
 * 周数对应的日期列表
 * 个人中心-我的课程-全部课程-顶部查询条件
 * @author WX
 * @date 2021-03-22 18:35:20
 */
public class DateListOfWeeksVo {

    /**
     * 日期字符串列表
     */
    private List<String> dateStrList;

    /**
     * 周日期集合，日期字符串列表内每个日期字符串对应的星期(周几)
     */
    private Map<Integer, String> weekDate;


    private static final long serialVersionUID = 1L;

    public DateListOfWeeksVo() {
    }

    public DateListOfWeeksVo(List<String> dateStrList, Map<Integer, String> weekDate) {
        this.dateStrList = dateStrList;
        this.weekDate = weekDate;
    }

    public List<String> getDateStrList() {
        return dateStrList;
    }

    public void setDateStrList(List<String> dateStrList) {
        this.dateStrList = dateStrList;
    }

    public Map<Integer, String> getWeekDate() {
        return weekDate;
    }

    public void setWeekDate(Map<Integer, String> weekDate) {
        this.weekDate = weekDate;
    }
}
