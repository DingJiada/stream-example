package com.shouzhi.pojo.echarts;

/**
 * 饼状图内的数据
 * @author WX
 * @date 2021-01-25 16:42:40
 */
public class PieChartData {

    /**
     * 值
     */
    private Integer value;

    /**
     * 名称
     */
    private String name;

    public PieChartData() {
    }

    public PieChartData(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
