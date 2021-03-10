package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.onvif.PtzPosition;
import com.shouzhi.pojo.vo.PtzDeviceSupportVo;
import com.shouzhi.service.interf.onvif.IPtzDeviceSevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;
import java.net.ConnectException;

/**
 * 摄像机云台控制接口
 * @author WX
 * @date 2020-10-28 10:55:36
 */
@RestController
@RequestMapping("/api/v1/ptzDevice")
public class PtzDeviceController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IPtzDeviceSevice ptzDeviceSevice;

    /**
     * 前台-获取摄像机云台当前位置
     * @apiNote 前台-获取摄像机云台当前位置，作用于调用控制云台绝对移动接口时的入参。为了减少与摄像机之间的通讯开销，此位置参数应当由前端做“页面级缓存”。即:
     *          1.进入带有摄像机画面的页面时，应首先请求此接口并缓存摄像机云台当前位置，接下来的移动接口入参应该是在此缓存参数的基础上<b>加</b>0.10或<b>减</b>0.10，而不是每次移动都重新请求此接口！
     *          2.当所在页面被关闭后重新打开或被刷新时应清除之前缓存的位置参数，并重新请求此接口且缓存覆盖摄像机云台当前位置
     * @param operateKey 操作key，用于控制云台时的密钥
     * @author WX
     * @date 2020-10-28 11:21:27
     */
    @PostMapping("/findPosition")
    public CommonResult<PtzPosition> findPosition(@RequestParam("operateKey") String operateKey, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PtzPosition> result = new CommonResult<>();
        try {
            PtzPosition position = ptzDeviceSevice.getPosition(operateKey);
            result.setStatus(1).setMsg("查询成功").setResultBody(position);
        } catch (Exception e) {
            ptzDeviceSevice.delOnvifDeviceInstanceByOperateKey(operateKey);
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 前台-绝对移动(缩放)云台
     * @apiNote 前台-绝对移动(缩放)云台，配合<b><获取摄像机云台当前位置接口></b>一起使用。注意：前端请求该接口时需做<b>缓冲等待</b>效果。
     *          解释：服务器在操作摄像机时会先取缓存中的实例，若缓存中没有则会建立与该摄像机的连接实例，建立连接非常耗时。
     *          所以当用户恰巧碰到第一次操作该摄像机时或服务器中间被重启过时本次请求则会很慢，<b>缓冲等待</b>效果会起到提示作用
     * @param x x轴，横向移动，移动参数范围-1.00~1.00，如：-0.90、-0.30、0.00、0.40、0.60等
     * @param y y轴，纵向移动，移动参数范围-1.00~1.00，如：-0.90、-0.30、0.00、0.40、0.60等
     * @param zoom 缩放，放大缩小，缩放参数范围0.00~1.00，如：0.00、0.40、0.60等
     * @param operateKey 操作key，用于控制云台时的密钥
     * @author WX
     * @date  2020-10-28 15:06:30
     */
    @PostMapping("/absoluteMove/{x}/{y}/{zoom}/{operateKey}")
    public CommonResult<Boolean> absoluteMove(@PathVariable("x") float x,@PathVariable("y") float y,@PathVariable("zoom") float zoom,
                                              @PathVariable("operateKey") String operateKey, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<Boolean> result = new CommonResult<>();
        try {
            boolean b = ptzDeviceSevice.absoluteMove(operateKey, x, y, zoom);
            result.setStatus(1).setMsg("操作成功").setResultBody(b);
        } catch (Exception e) {
            ptzDeviceSevice.delOnvifDeviceInstanceByOperateKey(operateKey);
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    // TODO 连续移动接口
    // TODO 连续移动的停止移动接口

    /**
     * 后台管理-检测设备是否支持云台控制
     * @apiNote 后台管理-检测设备是否支持云台控制，注意：前端请求该接口<b>必须设置请求的超时时间</b>，超时时间为<b>10S</b>。
     * @param hostIp 设备ip
     * @param onvifUser onvif用户名
     * @param onvifPwd onvif密码
     * @author WX
     * @date  2020-10-28 16:57:21
     */
    @PostMapping("/isSupport")
    public CommonResult<PtzDeviceSupportVo> isSupport(@RequestParam("hostIp") String hostIp, @RequestParam("onvifUser") String onvifUser,
                                           @RequestParam("onvifPwd") String onvifPwd, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<PtzDeviceSupportVo> result = new CommonResult<>();
        try {
            PtzDeviceSupportVo support = ptzDeviceSevice.isSupport(hostIp, onvifUser, onvifPwd);
            result.setStatus(1).setMsg("检测完成").setResultBody(support);
        }catch (ConnectException | SOAPException e1) {
            ptzDeviceSevice.delOnvifDeviceInstanceByHostIp(hostIp);
            this.fillIllegalArgResult(result, new IllegalArgumentException("ONVIF_INITIAL_DEVICES_CONNECT_ERROR"), true, true, logger);
        } catch (Exception e) {
            ptzDeviceSevice.delOnvifDeviceInstanceByHostIp(hostIp);
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

}
