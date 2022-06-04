package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SysStaticParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统静态参数设置表持久层接口
 * @author WX
 * @date 2020-11-04 15:40:06
 */
@Repository("sysStaticParamMapper")
public interface SysStaticParamMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysStaticParam record) throws Exception;

    Integer insertSelective(SysStaticParam record) throws Exception;

    SysStaticParam selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysStaticParam record) throws Exception;

    Integer updateByPrimaryKey(SysStaticParam record) throws Exception;

    /**
     * 根据参数查询系统静态参数设置列表
     * @param record
     * @author WX
     * @date 2020-11-04 16:02:10
     */
    List<SysStaticParam> queryListByPage(SysStaticParam record);

}