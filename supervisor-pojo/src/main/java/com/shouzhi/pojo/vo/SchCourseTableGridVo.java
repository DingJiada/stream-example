package com.shouzhi.pojo.vo;

import java.util.List;

/**
 * 课表网格视图VO
 * @author WX
 * @date 2020-12-14 14:11:24
 */
public class SchCourseTableGridVo {

    /**
     * 水平表头，一维数组，如：['周一', '周二', '周三', '周四', '周五']
     */
    private List<String> hHeader;

    /**
     * 垂直表头，二维数组，如：
     * [
     *  ['1', 1],
     *  ['2', 1]
     * ]
     * （该字段暂未定准，以后可能会有变更）
     */
    private List<List<Object>> vHeader;

    /**
     * 课表数据，二维数组，如：
     * [
     *  ['语文','','英语','物理','语文','数学','英语','物理','物理','数学','英语','物理'],
     *  ['语文','数学','英语','物理','语文','','英语','物理','语文','数学','英语','物理'],
     * ]
     */
    private List<List<String>> courseList;

    public SchCourseTableGridVo() {
    }

    public SchCourseTableGridVo(List<String> hHeader, List<List<Object>> vHeader, List<List<String>> courseList) {
        this.hHeader = hHeader;
        this.vHeader = vHeader;
        this.courseList = courseList;
    }

    public List<String> gethHeader() {
        return hHeader;
    }

    public void sethHeader(List<String> hHeader) {
        this.hHeader = hHeader;
    }

    public List<List<Object>> getvHeader() {
        return vHeader;
    }

    public void setvHeader(List<List<Object>> vHeader) {
        this.vHeader = vHeader;
    }

    public List<List<String>> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<List<String>> courseList) {
        this.courseList = courseList;
    }
}
