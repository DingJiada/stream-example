package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.Examinee;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 考生表业务层接口
 * @author WX
 * @date 2020-08-04 17:14:09
 */
public interface IExamineeService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(Examinee record) throws Exception;

    Integer insertSelective(Examinee record) throws Exception;

    Examinee selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(Examinee record) throws Exception;

    Integer updateByPrimaryKey(Examinee record) throws Exception;

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-08-04 16:58:17
     */
    List<Examinee> queryListByPage(Examinee record);

    /**
     * 批量更新 根据ID
     * @param map 参数+ExamineeIds列表
     * @author WX
     * @date 2020-08-05 10:26:57
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;

    /**
     * 查询考生列表，带头像，前端平台-云监考
     * @param map
     * @author WX
     * @date 2020-08-16 16:03:25
     */
    List<Examinee> foregroundExamineeList(Map<String, Object> map);


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-22 17:01:16
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-22 17:08:21
     */
    List<Examinee> BatchSelect(Map<String, Object> map);


    /**
     * 新增考生信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-05 09:11:16
     */
    Integer save(Examinee record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 新增考生信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-05 09:14:43
     */
    Integer saveExaminee(Examinee record, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID更新
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-05 10:11:28
     */
    Integer update(Examinee record, String permId, HttpServletRequest req, String... operType) throws Exception;

    /**
     * 修改考生信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-05 10:11:28
     */
    Integer updateExaminee(Examinee record, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID删除考生信息
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-05 10:20:43
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据IDs批量删除考生信息
     * @param examineeIds
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-05 10:20:43
     */
    Integer batchDelete(String examineeIds, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据多参数批量删除考生信息
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-22 17:24:09
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, HttpServletRequest req, boolean strictMode) throws Exception;

}
