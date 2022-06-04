package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.ExaminationHall;
import com.shouzhi.pojo.db.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 考场考试表业务层接口
 * @author WX
 * @date 2020-08-03 12:43:55
 */
public interface IExaminationHallService {

    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(ExaminationHall record) throws Exception;

    Integer insertSelective(ExaminationHall record) throws Exception;

    ExaminationHall selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(ExaminationHall record) throws Exception;

    Integer updateByPrimaryKey(ExaminationHall record) throws Exception;

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-08-03 12:49:23
     */
    List<ExaminationHall> queryListByPage(ExaminationHall record);

    /**
     * 根据用户表的参数字段查询考试信息，比如：根据用户ID、根据用户姓名等，不涉及groupBy
     * @param map
     * @author WX
     * @date 2020-08-05 18:43:18
     */
    List<ExaminationHall> selectByUserParam(Map<String, Object> map);


    /**
     * 批量更新 根据ID
     * @param map 参数+ExaminationHallIds列表
     * @author WX
     * @date 2020-08-04 14:08:42
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-22 17:46:09
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-22 17:51:39
     */
    List<ExaminationHall> BatchSelect(Map<String, Object> map);


    /**
     * 新增考试
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-03 15:17:43
     */
    Integer save(ExaminationHall record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 新增考试+监考老师信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-03 15:26:08
     */
    Integer saveExamAndInvigilators(ExaminationHall record, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID更新考试信息
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    Integer update(ExaminationHall record, String permId, HttpServletRequest req, String... operType) throws Exception;

    /**
     * 修改考试+监考老师信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-04 09:04:16
     */
    Integer updateExamAndInvigilators(ExaminationHall record, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID删除考试信息
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-04 13:43:24
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据IDs批量删除考试信息
     * @param examinationHallIds
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-04 13:59:16
     */
    Integer batchDelete(String examinationHallIds, String permId, HttpServletRequest req) throws Exception;











    /**
     * 删除专辑
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-07 10:08:49
     */
    Integer deleteAlbum(String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 批量删除专辑
     * @param examinationHallIds
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-07 10:09:28
     */
    Integer batchDeleteAlbum(String examinationHallIds, String permId, HttpServletRequest req) throws Exception;

}
