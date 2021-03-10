package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_log_login
 * @author 
 */
public class LogLogin implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date loginTime;

    /**
     * 登录状态，1成功，0失败
     */
    private String loginState;

    /**
     * 状态码，具体的状态描述。200：成功，101：密码错误，102：验证码错误，103：验证码失效，104：失败重试次数限制错误，等等(更倾向于失败描述，成功就是成功没什么描述)
     */
    private String loginCode;

    /**
     * Browser UA
     */
    @JsonIgnore
    private String loginUa;

    /**
     * 操作系统名称，如：Windows 10
     */
    private String osName;

    /**
     * 操作系统组名称，如：Windows
     */
    private String osGroupName;

    /**
     * 操作系统设备类型名称，如：Computer
     */
    private String osDeviceTypeName;

    /**
     * 操作系统制造商名称，如：Microsoft Corporation
     */
    private String osManufacturerName;

    /**
     * 操作系统版本，如：Win64
     */
    private String osVersion;

    /**
     * 浏览器名称，如：Chrome 8
     */
    private String browserName;

    /**
     * 浏览器组名称，如：Chrome
     */
    private String browserGroupName;

    /**
     * 浏览器类型名称，如：Browser
     */
    private String browserTypeName;

    /**
     * 浏览器制造商名称，如：Google Inc.
     */
    private String browserManufacturerName;

    /**
     * 浏览器渲染引擎，如：WebKit
     */
    private String browserRenderingEngine;

    /**
     * 浏览器版本，如：83.0.4103.61
     */
    private String browserVersion;

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
     * 删除时间
     * @ignore
     */
    @JsonIgnore
    private Date deleteTime;

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     * @ignore
     */
    private String userName;

    /**
     * 姓名
     * @ignore
     */
    private String personName;

    /**
     * 角色名称
     * @ignore
     */
    private String roleName;

    public LogLogin() {
    }

    public LogLogin(String createId, String createBy, String roleName) {
        this.createId = createId;
        this.createBy = createBy;
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginState() {
        return loginState;
    }

    public void setLoginState(String loginState) {
        this.loginState = loginState;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getLoginUa() {
        return loginUa;
    }

    public void setLoginUa(String loginUa) {
        this.loginUa = loginUa;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsGroupName() {
        return osGroupName;
    }

    public void setOsGroupName(String osGroupName) {
        this.osGroupName = osGroupName;
    }

    public String getOsDeviceTypeName() {
        return osDeviceTypeName;
    }

    public void setOsDeviceTypeName(String osDeviceTypeName) {
        this.osDeviceTypeName = osDeviceTypeName;
    }

    public String getOsManufacturerName() {
        return osManufacturerName;
    }

    public void setOsManufacturerName(String osManufacturerName) {
        this.osManufacturerName = osManufacturerName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserGroupName() {
        return browserGroupName;
    }

    public void setBrowserGroupName(String browserGroupName) {
        this.browserGroupName = browserGroupName;
    }

    public String getBrowserTypeName() {
        return browserTypeName;
    }

    public void setBrowserTypeName(String browserTypeName) {
        this.browserTypeName = browserTypeName;
    }

    public String getBrowserManufacturerName() {
        return browserManufacturerName;
    }

    public void setBrowserManufacturerName(String browserManufacturerName) {
        this.browserManufacturerName = browserManufacturerName;
    }

    public String getBrowserRenderingEngine() {
        return browserRenderingEngine;
    }

    public void setBrowserRenderingEngine(String browserRenderingEngine) {
        this.browserRenderingEngine = browserRenderingEngine;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
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

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}