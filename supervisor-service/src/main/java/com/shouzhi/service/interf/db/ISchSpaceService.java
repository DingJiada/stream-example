package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchSpace;
import com.shouzhi.pojo.vo.TreeNodeVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 学校空间表业务层接口
 * @author WX
 * @date 2020-11-05 17:35:50
 */
public interface ISchSpaceService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchSpace record) throws Exception;

    Integer insertSelective(SchSpace record) throws Exception;

    SchSpace selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchSpace record) throws Exception;

    Integer updateByPrimaryKey(SchSpace record) throws Exception;

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2021-01-20 17:51:36
     */
    SchSpace selectOneByParam(Map<String, Object> map);

    /**
     * 查询总数
     * @author WX
     * @date 2020-11-16 15:53:09
     */
    Integer selectCount();

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-11-05 17:18:21
     */
    List<SchSpace> queryListByPage(SchSpace record);

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-03-02 15:32:06
     */
    Integer batchInsert(List<SchSpace> list) throws Exception;

    /**
     * 批量更新根据ID
     * @param map 参数+schSpaceIds列表
     * @author WX
     * @date 2020-11-06 11:20:09
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-21 18:14:23
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-21 18:18:07
     */
    List<SchSpace> BatchSelect(Map<String, Object> map);


    /**
     * 新增学校空间信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-11-06 10:59:12
     */
    Integer save(SchSpace record, String permId, HttpServletRequest req) throws Exception;


    /**
     * 批量新增学校空间信息
     * @param list
     * @param permId
     * @author WX
     * @date 2021-03-02 15:32:06
     */
    Integer batchSave(List<SchSpace> list, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID更新学校空间信息
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-06 11:02:06
     */
    Integer update(SchSpace record, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除学校空间信息
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-06 11:07:22
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据IDs批量删除学校空间信息
     * @param schSpaceIds 学校空间信息ID，对应学校空间信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-06 11:18:06
     */
    Integer batchDelete(String schSpaceIds, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-26 17:04:02
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception;


    /**
     * 查询学校空间信息树
     * @param req
     * @author WX
     * @date 2020-11-06 16:23:16
     */
    List<TreeNodeVo> findTree(HttpServletRequest req);


    /**
     * 导入部分学校空间信息
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile excel文件
     * @param parentId 父结点id
     * @param delOldData 是否移除旧数据，是为1，否为0
     * @author WX
     * @date 2021-03-02 09:33:19
     */
    Integer impPartSchSpace(String permId, MultipartFile excelFile, String parentId, String delOldData, HttpServletRequest req) throws Exception;

}
