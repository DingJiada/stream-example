package com.shouzhi.pojo.echarts;

import java.util.List;

/**
 * 柱状图
 * @author WX
 * @date 2021-01-25 16:36:56
 */
public class BarChart {

    /**
     * 图例数据
     */
    private List<String> legendData;

    /**
     * X轴数据
     */
    private List<String> xAxisData;

    /**
     * Y轴数据
     */
    private List<Integer> yAxisData;

    public BarChart() {
    }

    public BarChart(List<String> legendData, List<String> xAxisData, List<Integer> yAxisData) {
        this.legendData = legendData;
        this.xAxisData = xAxisData;
        this.yAxisData = yAxisData;
    }

    public List<String> getLegendData() {
        return legendData;
    }

    public void setLegendData(List<String> legendData) {
        this.legendData = legendData;
    }

    public List<String> getxAxisData() {
        return xAxisData;
    }

    public void setxAxisData(List<String> xAxisData) {
        this.xAxisData = xAxisData;
    }

    public List<Integer> getyAxisData() {
        return yAxisData;
    }

    public void setyAxisData(List<Integer> yAxisData) {
        this.yAxisData = yAxisData;
    }
}
