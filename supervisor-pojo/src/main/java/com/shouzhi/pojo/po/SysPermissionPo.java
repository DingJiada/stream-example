package com.shouzhi.pojo.po;

import java.io.Serializable;

/**
 * 菜单权限映射
 * @author WX
 * @date 2020-08-25 14:44:49
 */
public class SysPermissionPo implements Serializable {

    // 一级菜单
    private String level1Id;

    private String level1Name;

    private String level1Type;

    private String level1ParentId;

    // 二级菜单
    private String level2Id;

    private String level2Name;

    private String level2Type;

    private String level2ParentId;

    // 按钮级别
    private String btnId;

    private String btnName;

    private String btnType;

    private String btnParentId;

    private static final long serialVersionUID = 1L;

    public String getLevel1Id() {
        return level1Id;
    }

    public void setLevel1Id(String level1Id) {
        this.level1Id = level1Id;
    }

    public String getLevel1Name() {
        return level1Name;
    }

    public void setLevel1Name(String level1Name) {
        this.level1Name = level1Name;
    }

    public String getLevel1Type() {
        return level1Type;
    }

    public void setLevel1Type(String level1Type) {
        this.level1Type = level1Type;
    }

    public String getLevel1ParentId() {
        return level1ParentId;
    }

    public void setLevel1ParentId(String level1ParentId) {
        this.level1ParentId = level1ParentId;
    }

    public String getLevel2Id() {
        return level2Id;
    }

    public void setLevel2Id(String level2Id) {
        this.level2Id = level2Id;
    }

    public String getLevel2Name() {
        return level2Name;
    }

    public void setLevel2Name(String level2Name) {
        this.level2Name = level2Name;
    }

    public String getLevel2Type() {
        return level2Type;
    }

    public void setLevel2Type(String level2Type) {
        this.level2Type = level2Type;
    }

    public String getLevel2ParentId() {
        return level2ParentId;
    }

    public void setLevel2ParentId(String level2ParentId) {
        this.level2ParentId = level2ParentId;
    }

    public String getBtnId() {
        return btnId;
    }

    public void setBtnId(String btnId) {
        this.btnId = btnId;
    }

    public String getBtnName() {
        return btnName;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public String getBtnType() {
        return btnType;
    }

    public void setBtnType(String btnType) {
        this.btnType = btnType;
    }

    public String getBtnParentId() {
        return btnParentId;
    }

    public void setBtnParentId(String btnParentId) {
        this.btnParentId = btnParentId;
    }
}
