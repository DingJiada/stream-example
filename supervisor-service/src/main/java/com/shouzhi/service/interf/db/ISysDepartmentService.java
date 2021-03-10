package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SysDepartment;
import com.shouzhi.pojo.vo.TreeNodeVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统部门表业务层接口
 * @author WX
 * @date 2020-11-30 12:28:41
 */
public interface ISysDepartmentService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysDepartment record) throws Exception;

    Integer insertSelective(SysDepartment record) throws Exception;

    SysDepartment selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysDepartment record) throws Exception;

    Integer updateByPrimaryKey(SysDepartment record) throws Exception;

    /**
     * 查询总数
     * @author WX
     * @date 2020-11-30 11:43:42
     */
    Integer selectCount();

    /**
     * 根据参数查询系统权限列表
     * @param map
     * @author WX
     * @date 2020-11-30 11:46:37
     */
    List<SysDepartment> queryListByPage(Map<String, Object> map);


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-30 14:03:09
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-30 14:06:56
     */
    List<SysDepartment> BatchSelect(Map<String, Object> map);


    /**
     * 查询菜单树
     * @param ascriptionType
     * @param req
     * @author WX
     * @date 2020-11-30 12:44:13
     */
    List<TreeNodeVo> findTree(String ascriptionType, HttpServletRequest req);


    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2020-11-30 14:16:43
     */
    Integer save(SysDepartment record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据ID更新
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-30 14:23:27
     */
    Integer update(SysDepartment record, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-30 14:34:49
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据IDs批量删除
     * @param sysDepartmentIds 系统部门ID，对应系统部门信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-30 14:49:39
     */
    Integer batchDelete(String sysDepartmentIds, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param isCascade 是否级联操作
     * @param cascadeId 级联标志
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-30 14:37:22
     */
    List<SysDepartment> batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception;

}
