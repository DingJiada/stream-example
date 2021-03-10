package com.shouzhi.service.impl.onvif;

import com.shouzhi.pojo.onvif.OnvifDeviceContext;
import de.onvif.soap.OnvifDevice;
import org.apache.commons.lang3.StringUtils;
import org.onvif.ver10.schema.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.soap.SOAPException;
import java.net.ConnectException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OnvifDevice上下文工厂
 * @author WX
 * @date 2020-10-28 09:32:28
 */
@Service("onvifDeviceContextFactory")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class OnvifDeviceContextFactory {

    private static ConcurrentHashMap<String, OnvifDeviceContext> onvifDeviceContextMap = new ConcurrentHashMap<>();
    private byte[] onvifDeviceContextLock = new byte[0]; // 特殊的instance变量

    /**
     * 创建及初始化OnvifDevice
     * @param key
     * @param hostIp 设备ip
     * @param onvifUser onvif用户名
     * @param onvifPwd onvif密码
     * @param soapLogging
     * @author WX
     * @date 2020-10-28 09:39:17
     */
    public OnvifDeviceContext getInstance(String key, String hostIp, String onvifUser, String onvifPwd, boolean soapLogging) throws Exception {
        if(StringUtils.isBlank(onvifUser)||StringUtils.isBlank(onvifPwd)) throw new IllegalArgumentException("ONVIF_USER_OR_PWD_NULL_ERROR");
        OnvifDeviceContext onvifDeviceContext = onvifDeviceContextMap.get(key);
        if(onvifDeviceContext==null){
            synchronized (onvifDeviceContextLock){
                OnvifDevice cam = new OnvifDevice(hostIp, onvifUser, onvifPwd, soapLogging);
                List<Profile> profiles = cam.getDevices().getProfiles();
                if(profiles==null) throw new IllegalArgumentException("ONVIF_GET_PROFILES_FAIL_ERROR");
                onvifDeviceContext = new OnvifDeviceContext(cam, profiles);
                onvifDeviceContextMap.put(key, onvifDeviceContext);
            }
        }
        return onvifDeviceContext;
    }

    /**
     * 删除OnvifDevice实例缓存
     * @param key
     * @author WX
     * @date 2021-01-05 11:36:03
     */
    public void delInstance(String key) {
        onvifDeviceContextMap.remove(key);
    }
}
