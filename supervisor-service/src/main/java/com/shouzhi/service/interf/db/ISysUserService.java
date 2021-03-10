package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.SysUser;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统用户表业务层接口
 * @author WX
 * @date 2020-06-15 09:48:21
 */
public interface ISysUserService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysUser record) throws Exception;

    Integer insertSelective(SysUser record) throws Exception;

    SysUser selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysUser record) throws Exception;

    Integer updateByPrimaryKey(SysUser record) throws Exception;

    /**
     * 根据参数查询系统用户列表
     * @param record
     * @author WX
     * @date 2020-06-15 09:41:23
     */
    List<SysUser> queryListByPage(SysUser record);

    /**
     * 根据参数查询用户信息
     * @param record
     * @author WX
     * @date 2020-06-15 15:53:23
     */
    SysUser selectOneByParam(SysUser record);

    /**
     * 根据用户名查询用户信息，shiro登录专用，里边涉及到级联查询，其余场景请勿使用
     * @param record
     * @author WX
     * @date 2020-06-15 15:53:23
     */
    SysUser selectByLogin(SysUser record);

    /**
     * 根据角色表的参数字段查询用户信息，比如：根据角色ID、根据角色类型、根据角色code等，不涉及groupBy
     * @param map
     * @author WX
     * @date 2020-08-05 15:56:43
     */
    List<SysUser> selectByRoleParam(Map<String, Object> map);


    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-02-05 10:46:36
     */
    Integer batchInsert(List<SysUser> list);


    /**
     * 批量更新已注册状态根据ID
     * @param map 参数+SysUserIds列表
     * @author WX
     * @date 2020-07-30 17:54:19
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 15:41:23
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 15:47:07
     */
    List<SysUser> BatchSelect(Map<String, Object> map);


    /**
     * 新增系统用户
     * @param record
     * @param permId
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    Integer save(SysUser record, String permId, HttpServletRequest req) throws Exception;


    /**
     * 批量新增系统用户
     * @param list
     * @param permId
     * @author WX
     * @date 2021-02-05 13:50:06
     */
    Integer batchSave(List<SysUser> list, String permId, HttpServletRequest req) throws Exception;


    /**
     * 新增系统用户和角色
     * @param personNum
     * @param personName
     * @param sysRoleIds
     * @param selectedRegister
     * @param permId
     * @author WX
     * @date 2020-07-29 16:34:02
     */
    Integer saveSysUserAndRole(String personNum, String personName, String sysRoleIds, String selectedRegister, String permId, HttpServletRequest req) throws Exception;

    /**
     * 批量保存
     * @param list 系统用户list
     * @param sysRoleIds 角色ids
     * @param selectedRegister 是否自动注册账户，是为1，否为0
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2021-02-05 10:53:29
     */
    Integer batchSaveSysUsersAndRole(List<SysUser> list, String sysRoleIds, String selectedRegister, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID更新系统用户
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    Integer update(SysUser record, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除系统用户
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据IDs批量删除系统用户
     * @param sysUserIds 用户ID，对应用户信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-30 20:08:16
     */
    Integer batchDelete(String sysUserIds, String permId, HttpServletRequest req) throws Exception;


    /**
     * 导入系统用户
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile excel文件
     * @param sysRoleIds 系统角色ID，对应角色列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param selectedRegister 是否自动注册账户，是为1，否为0
     * @author WX
     * @date 2021-02-04 16:42:23
     */
    Integer impSysUser(String permId, MultipartFile excelFile, String sysRoleIds, String selectedRegister, HttpServletRequest req) throws Exception;


    /**
     * 根据SysUserId插入BasicAuth账号
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-29 14:55:07
     */
    Integer toRegister(String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据SysUserId插入BasicAuth账号
     * @param sysUserIds 用户ID，对应用户信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-30 18:01:46
     */
    Integer toBatchRegister(String sysUserIds, String permId, HttpServletRequest req) throws Exception;







    /**
     * 根据ID更新用户头像
     * @param imgFile
     * @param rowId
     * @param permId
     * @author WX
     * @date 2020-07-20 12:30:03
     */
    Integer uploadHeadImgById(MultipartFile imgFile, String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID查询系统用户基本资料，包括角色，部门，连表信息
     * @param rowId
     * @author WX
     * @date 2020-07-21 17:45:20
     */
    SysUser findbasicInfoById(String rowId, HttpServletRequest req);

}
