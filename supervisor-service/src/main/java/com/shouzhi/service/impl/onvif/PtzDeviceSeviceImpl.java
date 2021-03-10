package com.shouzhi.service.impl.onvif;

import com.shouzhi.basic.utils.CredentialsUtil;
import com.shouzhi.pojo.db.SchDevice;
import com.shouzhi.pojo.onvif.OnvifDeviceContext;
import com.shouzhi.pojo.onvif.PtzPosition;
import com.shouzhi.pojo.vo.PtzDeviceSupportVo;
import com.shouzhi.service.constants.SecretKeyConst;
import com.shouzhi.service.interf.db.ISchDeviceService;
import com.shouzhi.service.interf.onvif.IPtzDeviceSevice;
import de.onvif.soap.OnvifDevice;
import org.apache.commons.lang3.StringUtils;
import org.onvif.ver10.schema.PTZVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * PTZ业务层接口实现类
 * @author WX
 * @date 2020-10-28 10:48:08
 */
@Service("ptzDeviceSevice")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class PtzDeviceSeviceImpl implements IPtzDeviceSevice {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OnvifDeviceContextFactory onvifDeviceContextFactory;

    @Autowired
    private ISchDeviceService schDeviceService;

    /**
     * 获取当前云台的位置
     * @param operateKey 操作key(设备id)，用于控制云台时的密钥
     * @author WX
     * @date 2020-10-28 10:49:26
     */
    @Override
    public PtzPosition getPosition(String operateKey) throws Exception {
        SchDevice sd = schDeviceService.selectByPrimaryKey(operateKey);
        Assert.notNull(sd,"SCH_DEVICE_NOT_FOUND_ERROR");
        Assert.isTrue("1".equals(sd.getIsSupportControl()),"ONVIF_PTZ_NO_SUPPORT_ERROR");
        OnvifDeviceContext onvifDeviceContext = onvifDeviceContextFactory.getInstance(CredentialsUtil.MD5Pwd(sd.getIpAddr(), SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS), sd.getIpAddr(), sd.getOnvifUser(), sd.getOnvifPwd(), false);
        PTZVector position = onvifDeviceContext.getOnvifDevice().getPtz().getPosition(onvifDeviceContext.getProfiles().get(0).getToken());
        float x = BigDecimal.valueOf(position.getPanTilt().getX()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        float y = BigDecimal.valueOf(position.getPanTilt().getY()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        float zoom = BigDecimal.valueOf(position.getZoom().getX()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return new PtzPosition(x, y, zoom);
    }

    /**
     * 绝对移动
     * @param operateKey 操作key(设备id)，用于控制云台时的密钥
     * @param x
     * @param y
     * @param zoom
     * @author WX
     * @date 2020-10-28 10:49:26
     */
    @Override
    public boolean absoluteMove(String operateKey, float x, float y, float zoom) throws Exception {
        SchDevice sd = schDeviceService.selectByPrimaryKey(operateKey);
        Assert.notNull(sd,"SCH_DEVICE_NOT_FOUND_ERROR");
        Assert.isTrue("1".equals(sd.getIsSupportControl()),"ONVIF_PTZ_NO_SUPPORT_ERROR");
        Assert.isTrue("1".equals(sd.getIsSupportAbsoluteMove()),"ONVIF_PTZ_ABSOLUTE_MOVE_NO_SUPPORT_ERROR");
        OnvifDeviceContext onvifDeviceContext = onvifDeviceContextFactory.getInstance(CredentialsUtil.MD5Pwd(sd.getIpAddr(), SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS), sd.getIpAddr(), sd.getOnvifUser(), sd.getOnvifPwd(), false);
        return onvifDeviceContext.getOnvifDevice()
                .getPtz().absoluteMove(onvifDeviceContext.getProfiles().get(0).getToken(), x, y, zoom);
    }

    /**
     * 是否支持云台
     * @param hostIp 设备ip
     * @param onvifUser onvif用户名
     * @param onvifPwd onvif密码
     * @author WX
     * @date 2020-10-28 16:28:34
     */
    @Override
    public PtzDeviceSupportVo isSupport(String hostIp, String onvifUser, String onvifPwd) throws Exception {
        //  key=ip,ip是唯一的，在新增设备时id并还未生成所以无法拿到,ip可以做到id的作用
        OnvifDeviceContext onvifDeviceContext = onvifDeviceContextFactory.getInstance(CredentialsUtil.MD5Pwd(hostIp, SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS), hostIp, onvifUser, onvifPwd, false);
        OnvifDevice onvifDevice = onvifDeviceContext.getOnvifDevice();
        Assert.isTrue(StringUtils.isNotBlank(onvifDevice.getPtzUri()),"ONVIF_PTZ_NO_SUPPORT_ERROR");
        String token = onvifDeviceContext.getProfiles().get(0).getToken();
        Assert.isTrue(onvifDevice.getPtz().isPtzOperationsSupported(token),"ONVIF_PTZ_NO_SUPPORT_ERROR");
        // 绝对移动 持续移动 相对移动
        boolean absoluteMoveSupported = onvifDevice.getPtz().isAbsoluteMoveSupported(token);
        boolean continuousMoveSupported = onvifDevice.getPtz().isContinuosMoveSupported(token);
        boolean relativeMoveSupported = onvifDevice.getPtz().isRelativeMoveSupported(token);
        // 如果key不能解决容易被变更的问题，那就不要缓存到内存中，要把 onvifDevice 销毁掉[已解决,key=ip]
        // onvifDevice = null;
        // System.gc();
        return new PtzDeviceSupportVo(absoluteMoveSupported, continuousMoveSupported, relativeMoveSupported);
    }


    /**
     * 移除OnvifDevice实例，根据OperateKey(设备id)
     * @param operateKey
     * @author WX
     * @date 2021-01-05 11:40:26
     */
    @Override
    public void delOnvifDeviceInstanceByOperateKey(String operateKey) {
        SchDevice sd = schDeviceService.selectByPrimaryKey(operateKey);
        if(sd==null) return;
        this.delOnvifDeviceInstanceByHostIp(sd.getIpAddr());
    }

    /**
     * 移除OnvifDevice实例，根据HostIp
     * @param hostIp
     * @author WX
     * @date 2021-01-05 11:40:26
     */
    @Override
    public void delOnvifDeviceInstanceByHostIp(String hostIp) {
        if(StringUtils.isBlank(hostIp)) return;
        onvifDeviceContextFactory.delInstance(CredentialsUtil.MD5Pwd(hostIp, SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS));
    }

}
