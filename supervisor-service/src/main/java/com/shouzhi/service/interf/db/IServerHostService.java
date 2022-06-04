package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.SchSemester;
import com.shouzhi.pojo.db.ServerHost;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 服务主机表业务层接口
 * @author WX
 * @date 2020-11-25 09:41:00
 */
public interface IServerHostService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(ServerHost record) throws Exception;

    Integer insertSelective(ServerHost record) throws Exception;

    ServerHost selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(ServerHost record) throws Exception;

    Integer updateByPrimaryKey(ServerHost record) throws Exception;

    /**
     * 根据参数查询
     * @param record
     * @author WX
     * @date 2020-11-25 09:49:03
     */
    ServerHost selectOneByParam(ServerHost record);


    /**
     * 查询总数
     * @author WX
     * @date 2021-02-03 17:24:16
     */
    Integer selectCount();

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2021-02-03 17:31:07
     */
    List<ServerHost> queryListByPage(Map<String, Object> map);

    /**
     * 根据ID更新
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2021-02-03 17:34:26
     */
    Integer update(ServerHost record, String permId, HttpServletRequest req, String... operType) throws Exception;

}
