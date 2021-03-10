package com.shouzhi.pojo.vo;

/**
 * 学校设备操作Vo
 * @author WX
 * @date 2021-01-04 16:32:47
 */
public class SchDeviceOperateVo {
    /**
     * 操作key，用于控制云台时的密钥
     */
    private String operateKey;
    /**
     * 设备名称，用于反填至页面
     */
    private String schDeviceName;
    /**
     * 播放地址
     */
    private String playAddr;

    public SchDeviceOperateVo() {
    }

    public SchDeviceOperateVo(String schDeviceName, String playAddr) {
        this(null, schDeviceName, playAddr);
    }

    public SchDeviceOperateVo(String operateKey, String schDeviceName, String playAddr) {
        this.operateKey = operateKey;
        this.schDeviceName = schDeviceName;
        this.playAddr = playAddr;
    }

    public String getOperateKey() {
        return operateKey;
    }

    public void setOperateKey(String operateKey) {
        this.operateKey = operateKey;
    }

    public String getSchDeviceName() {
        return schDeviceName;
    }

    public void setSchDeviceName(String schDeviceName) {
        this.schDeviceName = schDeviceName;
    }

    public String getPlayAddr() {
        return playAddr;
    }

    public void setPlayAddr(String playAddr) {
        this.playAddr = playAddr;
    }
}
