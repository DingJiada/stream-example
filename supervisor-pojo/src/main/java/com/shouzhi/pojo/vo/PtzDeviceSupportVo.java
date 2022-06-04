package com.shouzhi.pojo.vo;

/**
 * Ptz设备支持VO
 * @author WX
 * @date 2020-12-21 14:08:15
 */
public class PtzDeviceSupportVo {

    /**
     * 绝对移动支持，boolean类型，支持：true、不支持：false
     */
    private boolean absoluteMoveSupported;

    /**
     * 连续移动支持，boolean类型，支持：true、不支持：false
     */
    private boolean continuousMoveSupported;

    /**
     * 相对移动支持，boolean类型，支持：true、不支持：false
     */
    private boolean relativeMoveSupported;

    public PtzDeviceSupportVo() {
    }

    public PtzDeviceSupportVo(boolean absoluteMoveSupported, boolean continuousMoveSupported, boolean relativeMoveSupported) {
        this.absoluteMoveSupported = absoluteMoveSupported;
        this.continuousMoveSupported = continuousMoveSupported;
        this.relativeMoveSupported = relativeMoveSupported;
    }

    public boolean isAbsoluteMoveSupported() {
        return absoluteMoveSupported;
    }

    public void setAbsoluteMoveSupported(boolean absoluteMoveSupported) {
        this.absoluteMoveSupported = absoluteMoveSupported;
    }

    public boolean isContinuousMoveSupported() {
        return continuousMoveSupported;
    }

    public void setContinuousMoveSupported(boolean continuousMoveSupported) {
        this.continuousMoveSupported = continuousMoveSupported;
    }

    public boolean isRelativeMoveSupported() {
        return relativeMoveSupported;
    }

    public void setRelativeMoveSupported(boolean relativeMoveSupported) {
        this.relativeMoveSupported = relativeMoveSupported;
    }
}
