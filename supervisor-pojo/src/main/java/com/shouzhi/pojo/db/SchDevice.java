package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_sch_device
 * @author 
 */
public class SchDevice implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 学校空间信息id
     */
    private String schSpaceId;

    /**
     * 设备序列号
     */
    private String serialNumber;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备位置描述，以讲台方向为北，NW：西北角，N：正北，NE：东北角，W：正西，C：教室正中心，E：正东，SW：西南角、S：正南、SE：东南角，CNW：西北中，CNE：东北中，CSE：东南中，CSW：西南中，PF：讲台，OT：其它
     */
    private String deviceLocation;

    /**
     * 设备类型，1：摄像机，2：讲台计算机，overlay：合成流
     */
    private String deviceType;

    /**
     * 接入协议，1：海康，2：大华，3：ONVIF
     */
    private String accessProtocol;

    /**
     * 设备ip地址
     */
    private String ipAddr;

    /**
     * 访问用户名
     */
    private String accessUser;

    /**
     * 访问密码
     */
    private String accessPwd;

    /**
     * rtsp主码流地址
     */
    private String rtspUrlMain;

    /**
     * rtsp子码流地址
     */
    private String rtspUrlSub;

    /**
     * rtmp流地址
     */
    private String rtmpGetUrl;

    /**
     * flv流地址
     */
    private String flvGetUrl;

    /**
     * HLS流地址
     */
    private String hlsGetUrl;

    /**
     * 设备连接状态，默认离线，0：离线(红)，1：在线(绿)
     */
    private String connectStatus;

    /**
     * 设备本身是否支持云台控制，0：不支持，1：支持
     */
    private String isSupportControl;

    /**
     * 是否支持绝对移动(前提为‘是否支持云台控制=1’)，0：不支持，1：支持
     */
    private String isSupportAbsoluteMove;

    /**
     * 是否支持连续移动(前提为‘是否支持云台控制=1’)，0：不支持，1：支持
     */
    private String isSupportContinuousMove;

    /**
     * 是否启用云台控制，默认未启用，0：未启用，1：启用
     */
    private String isEnableControl;

    /**
     * 是否激活设备，默认激活，相对范围是平台系统而不是设备本身，激活后在平台系统业务操作范围中可见，反之不可见。0：未激活，1：激活
     */
    private String isActive;

    /**
     * onvif用户名
     */
    private String onvifUser;

    /**
     * onvif密码
     */
    private String onvifPwd;

    /**
     * 创建人id，用于连表不用于显示
     * @ignore
     */
    @JsonIgnore
    private String createId;

    /**
     * 创建人账号，不建议填名字，账号更改的可能性很小且又比ID稍微形象化
     * @ignore
     */
    @JsonIgnore
    private String createBy;

    /**
     * 创建方式（0：程序代码创建，1：导入创建）
     * @ignore
     */
    @JsonIgnore
    private String createWay;

    /**
     * 创建时间
     * @ignore
     */
    @JsonIgnore
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除（0：未删除，1：删除）
     * @ignore
     */
    @JsonIgnore
    private String isDelete;

    /**
     * 删除人id，用于连表不用于显示
     * @ignore
     */
    @JsonIgnore
    private String deleteId;

    /**
     * 删除人
     * @ignore
     */
    @JsonIgnore
    private String deleteBy;

    /**
     * 删除时间
     * @ignore
     */
    @JsonIgnore
    private Date deleteTime;

    private static final long serialVersionUID = 1L;

    /**
     * 学校空间信息名称
     * @ignore
     */
    private String schSpaceName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchSpaceId() {
        return schSpaceId;
    }

    public void setSchSpaceId(String schSpaceId) {
        this.schSpaceId = schSpaceId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(String deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAccessProtocol() {
        return accessProtocol;
    }

    public void setAccessProtocol(String accessProtocol) {
        this.accessProtocol = accessProtocol;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getAccessUser() {
        return accessUser;
    }

    public void setAccessUser(String accessUser) {
        this.accessUser = accessUser;
    }

    public String getAccessPwd() {
        return accessPwd;
    }

    public void setAccessPwd(String accessPwd) {
        this.accessPwd = accessPwd;
    }

    public String getRtspUrlMain() {
        return rtspUrlMain;
    }

    public void setRtspUrlMain(String rtspUrlMain) {
        this.rtspUrlMain = rtspUrlMain;
    }

    public String getRtspUrlSub() {
        return rtspUrlSub;
    }

    public void setRtspUrlSub(String rtspUrlSub) {
        this.rtspUrlSub = rtspUrlSub;
    }

    public String getRtmpGetUrl() {
        return rtmpGetUrl;
    }

    public void setRtmpGetUrl(String rtmpGetUrl) {
        this.rtmpGetUrl = rtmpGetUrl;
    }

    public String getFlvGetUrl() {
        return flvGetUrl;
    }

    public void setFlvGetUrl(String flvGetUrl) {
        this.flvGetUrl = flvGetUrl;
    }

    public String getHlsGetUrl() {
        return hlsGetUrl;
    }

    public void setHlsGetUrl(String hlsGetUrl) {
        this.hlsGetUrl = hlsGetUrl;
    }

    public String getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(String connectStatus) {
        this.connectStatus = connectStatus;
    }

    public String getIsSupportControl() {
        return isSupportControl;
    }

    public void setIsSupportControl(String isSupportControl) {
        this.isSupportControl = isSupportControl;
    }

    public String getIsSupportAbsoluteMove() {
        return isSupportAbsoluteMove;
    }

    public void setIsSupportAbsoluteMove(String isSupportAbsoluteMove) {
        this.isSupportAbsoluteMove = isSupportAbsoluteMove;
    }

    public String getIsSupportContinuousMove() {
        return isSupportContinuousMove;
    }

    public void setIsSupportContinuousMove(String isSupportContinuousMove) {
        this.isSupportContinuousMove = isSupportContinuousMove;
    }

    public String getIsEnableControl() {
        return isEnableControl;
    }

    public void setIsEnableControl(String isEnableControl) {
        this.isEnableControl = isEnableControl;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getOnvifUser() {
        return onvifUser;
    }

    public void setOnvifUser(String onvifUser) {
        this.onvifUser = onvifUser;
    }

    public String getOnvifPwd() {
        return onvifPwd;
    }

    public void setOnvifPwd(String onvifPwd) {
        this.onvifPwd = onvifPwd;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateWay() {
        return createWay;
    }

    public void setCreateWay(String createWay) {
        this.createWay = createWay;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getSchSpaceName() {
        return schSpaceName;
    }

    public void setSchSpaceName(String schSpaceName) {
        this.schSpaceName = schSpaceName;
    }


    @Override
    public String toString() {
        return "SchDevice{" +
                "id='" + id + '\'' +
                ", schSpaceId='" + schSpaceId + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceLocation='" + deviceLocation + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", accessProtocol='" + accessProtocol + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", accessUser='" + accessUser + '\'' +
                ", accessPwd='" + accessPwd + '\'' +
                ", rtspUrlMain='" + rtspUrlMain + '\'' +
                ", rtspUrlSub='" + rtspUrlSub + '\'' +
                ", rtmpGetUrl='" + rtmpGetUrl + '\'' +
                ", flvGetUrl='" + flvGetUrl + '\'' +
                ", hlsGetUrl='" + hlsGetUrl + '\'' +
                ", connectStatus='" + connectStatus + '\'' +
                ", isSupportControl='" + isSupportControl + '\'' +
                ", isSupportAbsoluteMove='" + isSupportAbsoluteMove + '\'' +
                ", isSupportContinuousMove='" + isSupportContinuousMove + '\'' +
                ", isEnableControl='" + isEnableControl + '\'' +
                ", isActive='" + isActive + '\'' +
                ", onvifUser='" + onvifUser + '\'' +
                ", onvifPwd='" + onvifPwd + '\'' +
                ", createId='" + createId + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createWay='" + createWay + '\'' +
                ", createTime=" + createTime +
                ", remark='" + remark + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", deleteId='" + deleteId + '\'' +
                ", deleteBy='" + deleteBy + '\'' +
                ", deleteTime=" + deleteTime +
                ", schSpaceName='" + schSpaceName + '\'' +
                '}';
    }
}