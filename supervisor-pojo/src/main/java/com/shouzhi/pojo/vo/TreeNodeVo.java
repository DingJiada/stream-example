package com.shouzhi.pojo.vo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 树节点VO,每个前端UI框架需要的字段名称不一样,可以根据项目做适当修改,但整体结构是固定不会变的
 * 比如:element ui 树节点的名称字段是label,而easy ui 树节点的名称字段是text
 * @author WX
 * @date 2020-10-29 15:47:59
 */
public class TreeNodeVo implements Serializable {

    /**
     * 节点id
     */
    private String id;

    /**
     * 节点value,与id的值相等,只不过为了适应不同组件
     */
    private String value;

    /**
     * 父节点ID
     */
    private String pid;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 节点状态，open：打开,closed：关闭，默认关闭
     */
    private String state = "closed";

    /**
     * ☑是否选中，默认关闭
     */
    private Boolean checked = false;

    /**
     * ☑是否禁用选中，默认不禁用
     */
    private Boolean disabled = false;

    /**
     * 小图标样式
     */
    private String iconCls;

    /**
     * 节点层级，如1，2，3
     */
    private Integer level = 1;

    /**
     * 是否叶子节点，0：否 1：是
     */
    private Integer isLeaf = 1;

    /**
     * 非定性扩充属性
     */
    private JSONObject attributes;

    /**
     * 孩子节点
     */
    private List<TreeNodeVo> children = new ArrayList<>();

    private static final long serialVersionUID = 1L;

    public TreeNodeVo() {
    }

    public TreeNodeVo(String id, String value, String pid, String label) {
        this(id, value, pid, label, "", null);
    }

    public TreeNodeVo(String id, String value, String pid, String label, String iconCls) {
        this(id, value, pid, label, iconCls, null);
    }

    public TreeNodeVo(String id, String value, String pid, String label, JSONObject attributes) {
        this(id, value, pid, label, null, attributes);
    }

    public TreeNodeVo(String id, String value, String pid, String label, String iconCls, JSONObject attributes) {
        this.id = id;
        this.value = value;
        this.pid = pid;
        this.label = label;
        this.iconCls = iconCls;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public JSONObject getAttributes() {
        return attributes;
    }

    public void setAttributes(JSONObject attributes) {
        this.attributes = attributes;
    }

    public List<TreeNodeVo> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNodeVo> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "TreeNodeVo{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", pid='" + pid + '\'' +
                ", label='" + label + '\'' +
                ", state='" + state + '\'' +
                ", checked=" + checked +
                ", disabled=" + disabled +
                ", iconCls='" + iconCls + '\'' +
                ", level=" + level +
                ", isLeaf=" + isLeaf +
                ", attributes=" + attributes +
                ", children=" + children +
                '}';
    }

    /**
     * 将原始数据originalList构建为树形结构
     * @param originalList 原始数据列表，为了逻辑简单清晰这里接收的是List<TreeNodeVo>，而非泛型
     *                     调用前需将从数据库查询到的List<T>转换为List<TreeNodeVo>或直接从Mapper中查询后返回List<TreeNodeVo>
     * @param pid   根节点id，String类型，若为其它类型并不适用
     * @return List<TreeNodeVo>，考虑到有些列表可能没有直接根节点，而是很多个同级节点
     * @author WX
     * @date 2020-11-09 15:16:35
     */
    public static List<TreeNodeVo> buildTree(List<TreeNodeVo> originalList, String pid){
        // 获取根节点，即找出父节点为空(或为null或为0)的对象
        List<TreeNodeVo> rootNodeList = new ArrayList<>();
        List<TreeNodeVo> foundNodeList = new ArrayList<>();
        for (TreeNodeVo treeNode : originalList) {
            if(treeNode.getPid().equals(pid)){
                rootNodeList.add(treeNode);
                foundNodeList.add(treeNode);
            }
        }
        if(!foundNodeList.isEmpty()){
            // 将根节点从原始list移除，减少下次处理数据
            originalList.removeAll(foundNodeList);
            // 递归封装树
            recursiveTree(rootNodeList, originalList);
        }
        foundNodeList = null;

        // System.err.println(LocalDateTime.now()+" --->构建完成，GC开始计时");
        // GC会增加耗时，根据情况决定是否启用
        // System.gc();
        // System.err.println(LocalDateTime.now()+" --->构建完成，GC结束计时");

        return rootNodeList;
    }

    /**
     * 递归树
     * @param parentNodeList 父节点列表
     * @param originalList 原始数据列表
     * @author WX
     * @date 2020-11-09 15:32:19
     */
    private static void recursiveTree(List<TreeNodeVo> parentNodeList, List<TreeNodeVo> originalList){
        for (TreeNodeVo treeNode : parentNodeList) {
            // 找到当前treeNode父节点的子节点列表
            findChildren(treeNode, originalList);
            if (treeNode.getChildren().isEmpty()) {
                continue;
            }
            // 开始下次递归
            recursiveTree(treeNode.getChildren(), originalList);
        }
    }

    /**
     * 查找单个父节点的孩子节点列表
     * @param parentNode 父节点，单个
     * @param originalList 原始数据列表
     * @author WX
     * @date 2020-11-09 15:46:09
     */
    private static void findChildren(TreeNodeVo parentNode, List<TreeNodeVo> originalList){
        // 找到当前父节点下的子节点列表
        List<TreeNodeVo> childNodeList = new ArrayList<>();
        for (TreeNodeVo treeNode : originalList) {
            if(treeNode.getPid().equals(parentNode.getId())){
                childNodeList.add(treeNode);
                treeNode.setLevel(parentNode.getLevel()+1);
                // 将找到的子节点列表写入到当前父节点下（给子节点列表字段赋值）
                parentNode.getChildren().add(treeNode);
            }
        }
        // 若存在，将当前父节点的子节点从原始list移除，减少下次处理数据
        if (!childNodeList.isEmpty()) {
            parentNode.setIsLeaf(0);
            originalList.removeAll(childNodeList);
        }
        childNodeList = null;
    }
}
