package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchCourseTableLive;
import com.shouzhi.pojo.dto.SchCourseTableLiveDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 学校直播课程表表业务层接口
 * @author WX
 * @date 2021-02-23 14:14:43
 */
public interface ISchCourseTableLiveService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchCourseTableLive record) throws Exception;

    Integer insertSelective(SchCourseTableLive record) throws Exception;

    SchCourseTableLive selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchCourseTableLive record) throws Exception;

    Integer updateByPrimaryKey(SchCourseTableLive record) throws Exception;

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2021-02-23 10:49:18
     */
    SchCourseTableLive selectOneByParam(Map<String, Object> map);


    /**
     * 查询总数
     * @author WX
     * @date 2021-02-23 10:51:26
     */
    Integer selectCount();

    /**
     * 根据参数查询列表，仅用于前台在线巡课调用，其余地方不可调用！
     * @param map
     * @author WX
     * @date 2021-02-23 10:48:09
     */
    // List<SchCourseTableLive> foregroundListByPage(Map<String, Object> map);


    /**
     * 根据参数查询列表  无连接表 NoJoinTable
     * @param map
     * @author WX
     * @date 2021-03-18 09:53:19
     **/
    List<SchCourseTableLive> queryListByPageNJT(Map<String, Object> map);


    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2021-02-23 10:54:29
     */
    List<SchCourseTableLive> queryListByPage(Map<String, Object> map);


    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-03-12 17:43:36
     */
    Integer batchInsert(List<SchCourseTableLive> list) throws Exception;


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2021-02-23 10:58:37
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2021-02-23 11:06:18
     */
    List<SchCourseTableLive> BatchSelect(Map<String, Object> map);


    /**
     * 批量更新
     * @param map
     * @author Dingjd
     * @date 2021/3/17 15:15
     **/
    Integer batchUpdate(Map<String, Object> map);


    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2021-02-23 14:18:03
     */
    Integer save(SchCourseTableLive record, String permId, HttpServletRequest req) throws Exception;


    /**
     * 批量新增
     * @param list
     * @param permId
     * @author WX
     * @date 2021-03-12 17:47:39
     */
    Integer batchSave(List<SchCourseTableLive> list, String permId, HttpServletRequest req) throws Exception;



    /**
     * 加入(发布)自定义直播计划
     * @param records
     * @param permId
     * @author WX
     * @date 2021-03-12 10:57:26
     */
    Integer joinCustomLivePlan(List<SchCourseTableLiveDto> records, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID更新
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2021-02-23 14:23:16
     */
    Integer update(SchCourseTableLive record, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2021-02-23 14:25:30
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据IDs批量删除
     * @param schCourseTableLiveIds 学校直播课程表ID，对应学校直播课程表信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2021-02-23 14:29:19
     */
    Integer batchDelete(String schCourseTableLiveIds, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param isCascade 是否级联操作
     * @param cascadeId 级联标志
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2021-02-23 14:34:27
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception;

    /**
     * 批量取消（批量恢复）计划
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param ids 筛选id 多个,隔开
     * @param isCancel 批量取消或恢复 0 或 1
     * @author Dingjd
     * @date 2021/3/17 14:55
     **/
    Integer batchChangePlanService(String permId, String ids, String isCancel, HttpServletRequest req) throws Exception;

    /**
     * 一键取消（一键恢复）计划
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param isCancel 批量取消或恢复 0 或 1
     * @author Dingjd
     * @date 2021/3/17 16:40
     **/
    Integer oneKeyChangePlanService(String permId, String isCancel, HttpServletRequest req) throws Exception;

    /**
     * 后台管理-直播管理-直播课表-制定直播计划-按教务课表自动生成计划-发布业务
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param isRecord 是否录制，默认否（0：否，1：是）
     * @param weeks 周数
     * @author Dingjd
     * @date 2021/3/18 16:48
     **/
    Integer publishLivePlanService(String permId, String isRecord, String weeks, HttpServletRequest req) throws Exception;
}
