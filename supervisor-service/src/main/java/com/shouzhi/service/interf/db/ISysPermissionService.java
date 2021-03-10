package com.shouzhi.service.interf.db;

import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SysPermission;
import com.shouzhi.pojo.vo.MenuAndPermVo;
import com.shouzhi.pojo.vo.SysPermissionVo;
import com.shouzhi.pojo.vo.TreeNodeVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统权限表业务层接口
 * @author WX
 * @date 2020-06-10 09:50:10
 */
public interface ISysPermissionService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysPermission record) throws Exception;

    Integer insertSelective(SysPermission record) throws Exception;

    SysPermission selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysPermission record) throws Exception;

    Integer updateByPrimaryKey(SysPermission record) throws Exception;

    /**
     * 查询总数
     * @author WX
     * @date 2020-11-16 15:53:09
     */
    Integer selectCount();

    /**
     * 根据参数查询系统资源权限列表
     * @param map
     * @author WX
     * @date 2020-06-10 09:40:13
     */
    List<SysPermission> queryListByPage(Map<String, Object> map);

    /**
     * 批量更新根据ID
     * @param map 参数+SysPermissionIds列表
     * @author WX
     * @date 2020-11-03 10:13:26
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 10:21:19
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 10:26:06
     */
    List<SysPermission> BatchSelect(Map<String, Object> map);


    // 拷贝注意：这里应该抛异常的，为了保留代码样本此处未抛异常，拷贝代码不要拷贝此行！
    CommonResult save(SysPermission record, MultipartFile imgFile, String permId, HttpServletRequest req);

    /**
     * 根据ID更新系统资源权限
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-11 16:01:25
     */
    Integer update(SysPermission record, MultipartFile imgFile, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除系统资源权限
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-11 17:26:27
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据IDs批量删除系统资源权限
     * @param sysPermissionIds 系统资源权限ID，对应系统资源权限信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-03 10:03:23
     */
    Integer batchDelete(String sysPermissionIds, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-26 14:16:39
     */
    List<SysPermission> batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception;


    /**
     * 获取用户菜单列表和权限列表
     * @param req
     * @author WX
     * @date 2021-01-19 15:10:27
     */
    MenuAndPermVo getMenuAndPerms(HttpServletRequest req) throws Exception;


    /**
     * 获取用户菜单列表
     * @param sysPermissions
     * @author WX
     * @date 2020-06-15 16:39:51
     */
    List<SysPermissionVo> getPermissions(List<SysPermission> sysPermissions) throws Exception;


    /**
     * 获取用户权限列表
     * @param sysPermissions
     * @author WX
     * @date 2021-01-19 15:10:27
     */
    List<String> getPermCodes(List<SysPermission> sysPermissions) throws Exception;


    /**
     * 查询菜单树
     * @param ascriptionType
     * @param req
     * @author WX
     * @date 2020-10-30 10:10:23
     */
    List<TreeNodeVo> findTree(String ascriptionType, HttpServletRequest req);
}
