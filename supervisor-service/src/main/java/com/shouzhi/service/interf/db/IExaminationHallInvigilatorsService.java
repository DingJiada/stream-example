package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.ExaminationHallInvigilators;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 考场考试-监考老师关联表业务层接口
 * @author WX
 * @date 2020-08-03 14:10:18
 */
public interface IExaminationHallInvigilatorsService {

    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(ExaminationHallInvigilators record) throws Exception;

    Integer insertSelective(ExaminationHallInvigilators record) throws Exception;

    ExaminationHallInvigilators selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(ExaminationHallInvigilators record) throws Exception;

    Integer updateByPrimaryKey(ExaminationHallInvigilators record) throws Exception;

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-08-03 13:57:19
     */
    Integer batchInsert(List<ExaminationHallInvigilators> list);


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 17:13:18
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 17:18:27
     */
    List<ExaminationHallInvigilators> BatchSelect(Map<String, Object> map);


    /**
     * 批量插入根据单个ExaminationHallId，含插入操作日志
     * @param examinationHallId
     * @param sysUserIds
     * @param permId
     * @author WX
     * @date 2020-08-03 15:40:43
     */
    Integer batchSaveByExaminationHallId(String examinationHallId, String sysUserIds, String permId, HttpServletRequest req) throws Exception;


    /**
     * 批量插入根据多个ExaminationHallId，含插入操作日志
     * @param examinationHallIds
     * @param sysUserIds
     * @param permId
     * @author WX
     * @date 2020-08-03 15:40:43
     */
    // Integer batchSaveByExaminationHallIds(String examinationHallIds, String sysUserIds, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-23 17:16:06
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, HttpServletRequest req, boolean strictMode) throws Exception;

}
