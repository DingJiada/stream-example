package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SchDevice;
import com.shouzhi.pojo.db.SchSpace;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 学校设备信息表持久层接口
 * @author WX
 * @date 2020-11-11 13:58:09
 */
@Repository("schDeviceMapper")
public interface SchDeviceMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchDevice record) throws Exception;

    Integer insertSelective(SchDevice record) throws Exception;

    SchDevice selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchDevice record) throws Exception;

    Integer updateByPrimaryKey(SchDevice record) throws Exception;

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-11-11 14:00:10
     */
    List<SchDevice> queryListByPage(Map<String, Object> map);


    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-03-08 13:47:19
     */
    Integer batchInsert(List<SchDevice> list) throws Exception;


    /**
     * 批量更新根据ID
     * @param map 参数+schDeviceIds列表
     * @author WX
     * @date 2020-11-11 14:01:26
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-20 14:11:06
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-20 14:23:17
     */
    List<SchDevice> BatchSelect(Map<String, Object> map);
}