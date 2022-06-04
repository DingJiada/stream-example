package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.SchDevice;
import com.shouzhi.pojo.db.SchSpace;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 学校设备信息表业务层接口
 * @author WX
 * @date 2020-11-11 14:03:50
 */
public interface ISchDeviceService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchDevice record) throws Exception;

    Integer insertSelective(SchDevice record) throws Exception;

    SchDevice selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchDevice record) throws Exception;

    Integer updateByPrimaryKey(SchDevice record) throws Exception;

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-11-11 14:00:10
     */
    List<SchDevice> queryListByPage(Map<String, Object> map);


    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-03-08 13:47:19
     */
    Integer batchInsert(List<SchDevice> list) throws Exception;


    /**
     * 批量更新根据ID
     * @param map 参数+schDeviceIds列表
     * @author WX
     * @date 2020-11-11 14:01:26
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-20 14:11:06
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;


    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-20 14:23:17
     */
    List<SchDevice> BatchSelect(Map<String, Object> map);


    /**
     * 新增学校设备信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-11-11 14:08:06
     */
    Integer save(SchDevice record, String permId, HttpServletRequest req) throws Exception;


    /**
     * 批量新增学校设备信息
     * @param list
     * @param permId
     * @author WX
     * @date 2021-03-08 13:51:19
     */
    Integer batchSave(List<SchDevice> list, String permId, HttpServletRequest req) throws Exception;


    /**
     * 批量新增学校设备合成流，每间教室对应一个合成流设备
     * 目前用在空间批量导入时的批量生成设备合成流
     * @param records
     * @param permId
     * @author WX
     * @date 2021-03-09 15:31:35
     */
    Integer batchSaveOverlayStream(List<SchSpace> records, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID更新学校设备信息
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-11 14:10:32
     */
    Integer update(SchDevice record, Boolean allColumn, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除学校设备信息
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-11 14:13:16
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据IDs批量删除学校设备信息
     * @param schDeviceIds 学校设备信息ID，对应学校设备信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-11 14:16:20
     */
    Integer batchDelete(String schDeviceIds, String permId, HttpServletRequest req) throws Exception;


    /**
     * 导入学校设备信息
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile excel文件
     * @author WX
     * @date 2021-03-05 09:24:36
     */
    Integer impSchDevice(String permId, MultipartFile excelFile, HttpServletRequest req) throws Exception;


    /**
     * 根据学校空间信息ID批量删除设备信息
     * @param schSpaceIdOrIds 单个schSpaceId或List的schSpaceIds
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-11 16:52:18
     */
    Integer batchDeleteBySchSpaceId(Object schSpaceIdOrIds, String permId, HttpServletRequest req, boolean strictMode) throws Exception;


    /**
     * 根据学校空间信息ID更新流地址
     * @param newSchSpace 新学校空间信息
     * @param oldSchSpace 旧学校空间信息
     * @author WX
     * @date 2020-11-19 10:51:06
     */
    Integer updateStreamUrlBySchSpaceId(SchSpace newSchSpace, SchSpace oldSchSpace) throws Exception;

    /**
     * 查询设备是否在线
     * @param schDeviceId
     * @author WX
     * @date 2020-11-12 10:28:16
     */
    SchDevice checkOnline(String schDeviceId) throws Exception;

    /**
     * 批量查询设备是否在线
     * @param schDeviceIds 学校设备信息ID，对应学校设备信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-12-14 16:40:27
     */
    List<SchDevice> batchCheckOnline(String schDeviceIds) throws Exception;
}
