package com.shouzhi.service.interf.other;

import com.shouzhi.pojo.echarts.BarChart;
import com.shouzhi.pojo.echarts.PieChart;
import com.shouzhi.pojo.vo.HomePageCurrentDateTimeVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台首页业务层接口
 * @author WX
 * @date 2021-01-20 16:33:17
 */
public interface IHomePageService {

    /**
     * 当前日期时间
     * @param req
     * @author WX
     * @date 2021-01-20 16:43:25
     */
    HomePageCurrentDateTimeVo currentDateTime(HttpServletRequest req) throws Exception;

    /**
     * 访问人数统计模块
     * @param dateType 日期类型，1：今日、2：本周、3：本月、4：本年
     * @param req
     * @author WX
     * @date 2021-01-22 16:10:06
     */
    BarChart visitorsNum(String dateType, HttpServletRequest req) throws Exception;

    /**
     * 访问来源-操作系统统计模块
     * @param dateType 日期类型，1：今日、2：本周、3：本月、4：本年
     * @param sourceType 来源类型，1：操作系统、2：浏览器
     * @param req
     * @author WX
     * @date 2021-01-25 16:59:07
     */
    PieChart visitorSource(String dateType, String sourceType, HttpServletRequest req) throws Exception;

}
