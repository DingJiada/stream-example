package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.ExamVideo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 考试(考生)视频表业务层接口
 * @author WX
 * @date 2020-08-06 17:37:28
 */
public interface IExamVideoService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(ExamVideo record) throws Exception;

    Integer insertSelective(ExamVideo record) throws Exception;

    ExamVideo selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(ExamVideo record) throws Exception;

    Integer updateByPrimaryKey(ExamVideo record) throws Exception;

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-08-06 17:30:16
     */
    List<ExamVideo> queryListByPage(ExamVideo record);

    /**
     * 根据参数查询信息
     * @param record
     * @author WX
     * @date 2020-08-11 09:27:00
     */
    ExamVideo selectOneByParam(ExamVideo record);

    /**
     * 批量更新 根据ID
     * @param map 参数+ExamVideoIds列表
     * @author WX
     * @date 2020-08-06 18:27:35
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-22 15:56:20
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-22 16:00:32
     */
    List<ExamVideo> BatchSelect(Map<String, Object> map);

    /**
     * 新增考试(考生)视频
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-06 18:03:48
     */
    Integer save(ExamVideo record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 新增考试(考生)视频
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-06 18:03:48
     */
    Integer saveExamVideo(ExamVideo record, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID更新
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-06 18:14:37
     */
    Integer update(ExamVideo record, String permId, HttpServletRequest req, String... operType) throws Exception;

    /**
     * 修改考试(考生)视频
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-06 18:14:37
     */
    Integer updateExamVideo(ExamVideo record, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID删除考试(考生)视频
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-06 18:18:49
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据IDs批量删除考试(考生)视频
     * @param examVideoIds
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-06 18:23:10
     */
    Integer batchDelete(String examVideoIds, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据多参数批量删除考试(考生)视频
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-22 16:40:21
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, HttpServletRequest req, boolean strictMode) throws Exception;


    /**
     * 考试(考生)异常行为打点，为该考生对应的录制视频记录更新异常行为字段
     * @param examinationHallId 考试(考场)id
     * @param examineeId 考生id
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-15 19:17:26
     */
    Integer addExamAbnormal(String examinationHallId, String examineeId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 更新考试(考生)视频观看数量
     * @param rowId
     * @author WX
     * @date 2020-08-21 15:42:36
     */
    Long updateWatchCount(String rowId, HttpServletRequest req) throws Exception;

}
