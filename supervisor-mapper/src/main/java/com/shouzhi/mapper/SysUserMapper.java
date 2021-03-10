package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 系统用户表持久层接口
 * @author WX
 * @date 2020-06-15 09:41:23
 */
@Repository("sysUserMapper")
public interface SysUserMapper {
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
}