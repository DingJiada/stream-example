package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchCourseCategory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 学校课程类别表业务层接口
 * @author WX
 * @date 2020-12-02 14:10:19
 */
public interface ISchCourseCategoryService {

    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchCourseCategory record) throws Exception;

    Integer insertSelective(SchCourseCategory record) throws Exception;

    SchCourseCategory selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchCourseCategory record) throws Exception;

    Integer updateByPrimaryKey(SchCourseCategory record) throws Exception;

    /**
     * 查询总数
     * @author WX
     * @date 2020-12-02 14:08:19
     */
    Integer selectCount();

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-02 14:15:39
     */
    List<SchCourseCategory> queryListByPage(Map<String, Object> map);

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-02 14:19:57
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-02 14:24:38
     */
    List<SchCourseCategory> BatchSelect(Map<String, Object> map);

    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2020-12-02 14:36:06
     */
    Integer save(SchCourseCategory record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据ID更新
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-02 14:40:18
     */
    Integer update(SchCourseCategory record, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-02 14:46:34
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据IDs批量删除
     * @param schCourseCategoryIds 学校课程类别ID，对应学校课程类别信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-02 14:51:39
     */
    Integer batchDelete(String schCourseCategoryIds, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param isCascade 是否级联操作
     * @param cascadeId 级联标志
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-12-02 14:55:23
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception;

    /**
     * 后台管理-基础设置-课程类别的导入
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile excel文件
     * @author Dingjd
     * @date 2021-03-12 09:55:36
     */
    Integer impCourseCategoryService(String permId, MultipartFile excelFile, HttpServletRequest req) throws Exception;

    /**
     * 批量插入
     * @param list
     * @author Dingjd
     * @date 2021-03-12 11:10:00
     */
    Integer batchInsert(List<SchCourseCategory> list) throws Exception;

    /**
     * 批量保存
     * @param list
     * @author Dingjd
     * @date 2021-03-12 11:10:00
     */
    Integer batchSave(List<SchCourseCategory> list, String permId, HttpServletRequest req) throws Exception;
}
