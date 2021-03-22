package com.shouzhi.pojo.vo;

import com.shouzhi.pojo.db.SchCourseTableBase;
import java.util.List;

/**
 * @author Dingjd
 * @date 2021/3/19 14:18
 */
public class DetectWeekResultVo {

    private Integer result;

    private List<SchCourseTableBase> schCourseTableLiveList;

    public DetectWeekResultVo(){}

    public DetectWeekResultVo(Integer result, List<SchCourseTableBase> schCourseTableLiveList) {
        this.result = result;
        this.schCourseTableLiveList = schCourseTableLiveList;
    }

    public Integer getResult() {
        return result;
    }

    public List<SchCourseTableBase> getSchCourseTableLiveList() {
        return schCourseTableLiveList;
    }
}
