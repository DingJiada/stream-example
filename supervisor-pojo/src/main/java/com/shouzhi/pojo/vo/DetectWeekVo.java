package com.shouzhi.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shouzhi.pojo.db.SchCourseTableBase;

import java.io.Serializable;
import java.util.List;

/**
 * 检测周数VO-直播课表-加入直播计划前的检测使用
 * @author Dingjd
 * @date 2021/3/19 14:18
 */
public class DetectWeekVo implements Serializable {

    /**
     * 检测结果，-1：该周数存在自定义计划、-2：该周数已全部自动加入计划、1：允许
     */
    private Integer detectResult;

    /**
     * 检测过程中查询的与该周数关联的表数据，不返回前端，仅用于service层复用
     * @ignore
     */
    @JsonIgnore
    private List<SchCourseTableBase> schCourseTableLiveList;

    private static final long serialVersionUID = 1L;

    public DetectWeekVo(){}

    public DetectWeekVo(Integer detectResult, List<SchCourseTableBase> schCourseTableLiveList) {
        this.detectResult = detectResult;
        this.schCourseTableLiveList = schCourseTableLiveList;
    }

    public Integer getDetectResult() {
        return detectResult;
    }

    public List<SchCourseTableBase> getSchCourseTableLiveList() {
        return schCourseTableLiveList;
    }
}
