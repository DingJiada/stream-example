package com.shouzhi.pojo.echarts;

import java.util.List;

/**
 * 饼状图
 * @author WX
 * @date 2021-01-25 16:39:00
 */
public class PieChart {

    /**
     * 图例数据
     */
    private List<String> legendData;

    /**
     * 饼图数据
     */
    private List<PieChartData> pieData;

    public PieChart() {
    }

    public PieChart(List<String> legendData, List<PieChartData> pieData) {
        this.legendData = legendData;
        this.pieData = pieData;
    }

    public List<String> getLegendData() {
        return legendData;
    }

    public void setLegendData(List<String> legendData) {
        this.legendData = legendData;
    }

    public List<PieChartData> getPieData() {
        return pieData;
    }

    public void setPieData(List<PieChartData> pieData) {
        this.pieData = pieData;
    }
}
