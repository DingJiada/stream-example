package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchCourseTableBase;
import com.shouzhi.pojo.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 学校基础课程表表业务层接口
 * @author WX
 * @date 2020-12-03 14:25:39
 */
public interface ISchCourseTableBaseService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchCourseTableBase record) throws Exception;

    Integer insertSelective(SchCourseTableBase record) throws Exception;

    SchCourseTableBase selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchCourseTableBase record) throws Exception;

    Integer updateByPrimaryKey(SchCourseTableBase record) throws Exception;

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2020-12-29 15:38:06
     */
    SchCourseTableBase selectOneByParam(Map<String, Object> map);


    /**
     * 查询总数
     * @author WX
     * @date 2020-12-03 14:10:06
     */
    Integer selectCount();

    /**
     * 根据参数查询列表，仅用于前台在线巡课调用，其余地方不可调用！
     * @param map
     * @author WX
     * @date 2020-12-29 17:20:19
     */
    List<SchCourseTableBase> foregroundListByPage(Map<String, Object> map);

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-03 14:14:23
     */
    List<SchCourseTableBase> queryListByPage(Map<String, Object> map);

    /**
     * 批量更新根据ID
     * @param map 参数列表
     * @author WX
     * @date 2021-03-12 17:59:35
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;


    /**
     * 批量更新根据ID，不同列，注意SQL的长度限制，可根据情况分批次执行，或也可以更新mysql的设置来扩展
     * @param list 数据列表
     * @author WX
     * @date 2021-03-15 17:18:09
     */
    Integer BatchUpdateDiffColumn(List<SchCourseTableBase> list) throws Exception;


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-03 14:19:37
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-03 14:23:16
     */
    List<SchCourseTableBase> BatchSelect(Map<String, Object> map);

    /**
     * 根据参数查询列表  无连接表 NoJoinTable
     * @param map
     * @author Dingjd
     * @date 2021/3/18 15:36
     **/
    List<SchCourseTableBase> queryListByPageNJT(Map<String, Object> map) throws Exception;

    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2020-12-03 14:29:50
     */
    Integer save(SchCourseTableBase record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据ID更新
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-03 14:34:53
     */
    Integer update(SchCourseTableBase record, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-03 14:38:36
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据IDs批量删除
     * @param schCourseTableBaseIds 学校基础课程表ID，对应学校基础课程表信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-03 14:43:08
     */
    Integer batchDelete(String schCourseTableBaseIds, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param isCascade 是否级联操作
     * @param cascadeId 级联标志
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-12-03 14:47:58
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception;

    /**
     * 查询课表-表格视图数据
     * @param map
     * @author WX
     * @date 2020-12-07 10:40:43
     */
    SchCourseTableGridVo findTableGrid(Map<String, Object> map);

    /**
     * 查询学校基础课程表-课表直播源
     * @param record
     * @author WX
     * @date 2021-03-16 09:33:16
     */
    List<SchCourseTableBaseLiveSourceVo> courseLiveSource(List<SchCourseTableBase> record);


    /**
     * 查询巡课详情
     * @param map
     * @author WX
     * @date 2020-12-29 16:03:35
     */
    SchCourseTableBase findPatrolCourseDetail(Map<String, Object> map);

    /**
     * 查询个人中心-全部课程
     * @param map
     * @author Dingjd
     * @date 2021/3/23 10:36
     **/
    List<PcAllCoursesVO> querySelfAllCourse(Map<String, Object> map, HttpServletRequest req) throws Exception;

    /**
     * 前端(当天)课程树
     * @param req
     * @author WX
     * @date 2021-01-06 15:50:35
     */
    List<TreeNodeVo> fgCourseTree(HttpServletRequest req);

    /**
     * 检测week
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param weeks 周数
     * @param isSaveDetectData 是否保存数据
     * @author Dingjd
     * @date 2021/3/18 15:37
     **/
    DetectWeekVo detectWeek(String permId, String weeks, boolean isSaveDetectData, HttpServletRequest req) throws Exception;

}
