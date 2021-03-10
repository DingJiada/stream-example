package com.shouzhi.pojo.onvif;

import de.onvif.soap.OnvifDevice;
import org.onvif.ver10.schema.Profile;

import java.io.Serializable;
import java.util.List;

/**
 * OnvifDevice上下文
 * @author WX
 * @date 2020-10-28 14:02:08
 */
public class OnvifDeviceContext implements Serializable {

    /**
     * 建立连接成功后的OnvifDevice
     */
    private OnvifDevice onvifDevice;

    /**
     * 操作PTZ需要的profileToken很频繁，所以需要放在上下文中
     */
    private List<Profile> profiles;

    /**
     * And more...
     */

    private static final long serialVersionUID = 1L;

    public OnvifDeviceContext(OnvifDevice onvifDevice, List<Profile> profiles) {
        this.onvifDevice = onvifDevice;
        this.profiles = profiles;
    }

    public OnvifDevice getOnvifDevice() {
        return onvifDevice;
    }

    public void setOnvifDevice(OnvifDevice onvifDevice) {
        this.onvifDevice = onvifDevice;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }
}
