package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SchCourseCategoryMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchCourseCategory;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.ISchCourseCategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学校课程类别表业务层接口实现类
 * @author WX
 * @date 2020-12-02 14:14:38
 */
@Service("schCourseCategoryService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SchCourseCategoryServiceImpl implements ISchCourseCategoryService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchCourseCategoryMapper schCourseCategoryMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return schCourseCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SchCourseCategory record) throws Exception {
        return schCourseCategoryMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SchCourseCategory record) throws Exception {
        return schCourseCategoryMapper.insertSelective(record);
    }

    @Override
    public SchCourseCategory selectByPrimaryKey(String id) {
        return schCourseCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SchCourseCategory record) throws Exception {
        return schCourseCategoryMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SchCourseCategory record) throws Exception {
        return schCourseCategoryMapper.updateByPrimaryKey(record);
    }

    /**
     * 查询总数
     * @author WX
     * @date 2020-12-02 14:08:19
     */
    @Override
    public Integer selectCount() {
        return schCourseCategoryMapper.selectCount();
    }

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-02 14:15:39
     */
    @Override
    public List<SchCourseCategory> queryListByPage(Map<String, Object> map) {
        return schCourseCategoryMapper.queryListByPage(map);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-02 14:19:57
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return schCourseCategoryMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-02 14:24:38
     */
    @Override
    public List<SchCourseCategory> BatchSelect(Map<String, Object> map) {
        return schCourseCategoryMapper.BatchSelect(map);
    }

    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2020-12-02 14:36:06
     */
    @Override
    public Integer save(SchCourseCategory record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入学校课程类别失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_CATEGORY, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID更新
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2020-12-02 14:40:18
     */
    @Override
    public Integer update(SchCourseCategory record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchCourseCategory originalRecord = this.selectByPrimaryKey(record.getId());
        Assert.notNull(originalRecord,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新学校课程类别失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_CATEGORY, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(originalRecord), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-02 14:46:34
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchCourseCategory record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_CATEGORY, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

    /**
     * 根据IDs批量删除
     * @param schCourseCategoryIds 学校课程类别ID，对应学校课程类别信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId               权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-02 14:51:39
     */
    @Override
    public Integer batchDelete(String schCourseCategoryIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(schCourseCategoryIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SchCourseCategory> schCourseCategories = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(schCourseCategories) && schCourseCategories.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 1.批量删除
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_CATEGORY, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, schCourseCategories, null);
        Assert.isTrue(count== schCourseCategories.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

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
    @Override
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception {
        return null;
    }
}
