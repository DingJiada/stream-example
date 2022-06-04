package com.shouzhi.service.constants;

/**
 * 直播流业务类型相关常量
 * 凡是带录制的流地址，在生成时都应该按照 业务地址_业务类型_库表记录id 规则进行生成
 * @author WX
 * @date 2020-08-07 17:44:28
 */
public final class LiveStreamTypeConst {
    /**
     * 私有化构造方法
     */
    private LiveStreamTypeConst(){}

    /**
     * 云监考-考生，含此类型的流地址被录制的视频应该与{@link DBConst#TABLE_NAME_WR_EXAM_VIDEO}有关
     */
    public static final String YJK_KS = "yjk_ks";

    /**
     * 本地巡考-教室
     */
    public static final String BDXK_JS = "bdxk_js";

}
