package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SysRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统角色表持久层接口
 * @author WX
 * @date 2020-06-12 09:52:40
 */
@Repository("sysRoleMapper")
public interface SysRoleMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysRole record) throws Exception;

    Integer insertSelective(SysRole record) throws Exception;

    SysRole selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysRole record) throws Exception;

    Integer updateByPrimaryKey(SysRole record) throws Exception;

    /**
     * 根据参数查询系统角色列表
     * @param record
     * @author WX
     * @date 2020-06-12 09:52:40
     */
    List<SysRole> queryListByPage(SysRole record);

}