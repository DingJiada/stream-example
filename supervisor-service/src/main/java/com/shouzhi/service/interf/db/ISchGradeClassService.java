package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchGradeClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 学校年级班级表业务层接口
 * @author WX
 * @date 2020-12-01 16:06:08
 */
public interface ISchGradeClassService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchGradeClass record) throws Exception;

    Integer insertSelective(SchGradeClass record) throws Exception;

    SchGradeClass selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchGradeClass record) throws Exception;

    Integer updateByPrimaryKey(SchGradeClass record) throws Exception;

    /**
     * 查询总数
     * @author WX
     * @date 2020-12-01 15:51:06
     */
    Integer selectCount();

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-01 15:59:18
     */
    List<SchGradeClass> queryListByPage(Map<String, Object> map);

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-01 16:06:31
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-01 16:13:30
     */
    List<SchGradeClass> BatchSelect(Map<String, Object> map);


    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2020-12-01 16:17:21
     */
    Integer save(SchGradeClass record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据ID更新
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-01 16:21:36
     */
    Integer update(SchGradeClass record, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-01 16:26:08
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据IDs批量删除
     * @param schGradeClassIds 学校年级班级ID，对应学校年级班级信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-01 16:31:25
     */
    Integer batchDelete(String schGradeClassIds, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param isCascade 是否级联操作
     * @param cascadeId 级联标志
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-12-01 16:33:19
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception;

}
