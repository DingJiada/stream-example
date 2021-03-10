package com.shouzhi.pojo.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 权限设置VO
 * @author WX
 * @date 2020-08-25 15:26:30
 */
public class SysPermissionSettingVo implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源类型，1：一级菜单，2：二级菜单，button：按钮
     */
    private String type;

    /**
     * 父结点id
     */
    private String parentId;

    /**
     * 是否选中，是为1，否为0
     */
    private String isSelected;

    /**
     * 子节点
     */
    private List<SysPermissionSettingVo> children;

    private static final long serialVersionUID = 1L;

    public SysPermissionSettingVo(String id, String name, String type, String parentId, String isSelected, List<SysPermissionSettingVo> children) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.parentId = parentId;
        this.isSelected = isSelected;
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public List<SysPermissionSettingVo> getChildren() {
        return children;
    }

    public void setChildren(List<SysPermissionSettingVo> children) {
        this.children = children;
    }
}
