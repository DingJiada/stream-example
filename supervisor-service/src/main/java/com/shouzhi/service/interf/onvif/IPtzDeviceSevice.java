package com.shouzhi.service.interf.onvif;

import com.shouzhi.pojo.onvif.PtzPosition;
import com.shouzhi.pojo.vo.PtzDeviceSupportVo;

/**
 * PTZ业务层接口
 * @author WX
 * @date 2020-10-28 10:01:50
 */
public interface IPtzDeviceSevice {

    /**
     * 获取当前云台的位置
     * @param operateKey 操作key(设备id)，用于控制云台时的密钥
     * @author WX
     * @date 2020-10-28 10:49:26
     */
    PtzPosition getPosition(String operateKey) throws Exception;

    /**
     * 绝对移动
     * @param operateKey 操作key(设备id)，用于控制云台时的密钥
     * @param x
     * @param y
     * @param zoom
     * @author WX
     * @date 2020-10-28 10:49:26
     */
    boolean absoluteMove(String operateKey, float x, float y, float zoom) throws Exception;

    /**
     * 是否支持云台
     * @param hostIp 设备ip
     * @param onvifUser onvif用户名
     * @param onvifPwd onvif密码
     * @author WX
     * @date 2020-10-28 16:28:34
     */
    PtzDeviceSupportVo isSupport(String hostIp, String onvifUser, String onvifPwd) throws Exception;

    /**
     * 移除OnvifDevice实例，根据OperateKey(设备id)
     * @param operateKey
     * @author WX
     * @date 2021-01-05 11:40:26
     */
    public void delOnvifDeviceInstanceByOperateKey(String operateKey);

    /**
     * 移除OnvifDevice实例，根据HostIp
     * @param hostIp
     * @author WX
     * @date 2021-01-05 11:40:26
     */
    public void delOnvifDeviceInstanceByHostIp(String hostIp);
}
