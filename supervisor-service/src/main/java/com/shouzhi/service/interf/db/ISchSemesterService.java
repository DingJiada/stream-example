package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchSemester;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 学校学期表业务层接口
 * @author WX
 * @date 2020-12-04 11:19:20
 */
public interface ISchSemesterService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchSemester record) throws Exception;

    Integer insertSelective(SchSemester record) throws Exception;

    SchSemester selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchSemester record) throws Exception;

    Integer updateByPrimaryKey(SchSemester record) throws Exception;

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2020-12-07 09:45:06
     */
    SchSemester selectOneByParam(Map<String, Object> map);


    /**
     * 查询总数
     * @author WX
     * @date 2020-12-04 11:19:06
     */
    Integer selectCount();

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-04 11:23:17
     */
    List<SchSemester> queryListByPage(Map<String, Object> map);

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-04 11:28:50
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-04 11:32:09
     */
    List<SchSemester> BatchSelect(Map<String, Object> map);


    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2020-12-04 11:36:10
     */
    Integer save(SchSemester record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据ID更新
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-04 11:40:53
     */
    Integer update(SchSemester record, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID 设置为当前学校学期
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2021-02-22 15:05:09
     */
    Integer setCurrentSem(String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID删除
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-04 11:43:31
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据IDs批量删除
     * @param schSemesterIds 学校学期ID，对应学校学期信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-04 11:46:29
     */
    Integer batchDelete(String schSemesterIds, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param isCascade 是否级联操作
     * @param cascadeId 级联标志
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-12-04 11:49:16
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception;

    /**
     * 查询某个学期周数列表
     * @param map
     * @author WX
     * @date 2020-12-04 17:50:36
     */
    List<Integer> findWeekNums(Map<String, Object> map, HttpServletRequest req);

    /**
     * 根据某个学期获取当前为第几周，即当前周
     * @param map
     * @author WX
     * @date 2021-01-06 15:56:07
     */
    String currentWeekBySem(Map<String, Object> map);

    /**
     * 根据当前最新学期获取当前为第几周，即当前周
     * @author WX
     * @date 2021-01-06 16:05:18
     */
    String currentWeekByCurrentSem();
}
