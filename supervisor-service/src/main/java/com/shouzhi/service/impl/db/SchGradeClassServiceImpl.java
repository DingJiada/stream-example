package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SchGradeClassMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchGradeClass;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.ISchGradeClassService;
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
 * 学校年级班级表业务层接口实现类
 * @author WX
 * @date 2020-12-01 16:07:50
 */
@Service("schGradeClassService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SchGradeClassServiceImpl implements ISchGradeClassService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchGradeClassMapper schGradeClassMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return schGradeClassMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SchGradeClass record) throws Exception {
        return schGradeClassMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SchGradeClass record) throws Exception {
        return schGradeClassMapper.insertSelective(record);
    }

    @Override
    public SchGradeClass selectByPrimaryKey(String id) {
        return schGradeClassMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SchGradeClass record) throws Exception {
        return schGradeClassMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SchGradeClass record) throws Exception {
        return schGradeClassMapper.updateByPrimaryKey(record);
    }

    /**
     * 查询总数
     * @author WX
     * @date 2020-12-01 15:51:06
     */
    @Override
    public Integer selectCount() {
        return schGradeClassMapper.selectCount();
    }

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-01 15:59:18
     */
    @Override
    public List<SchGradeClass> queryListByPage(Map<String, Object> map) {
        return schGradeClassMapper.queryListByPage(map);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-01 16:06:31
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return schGradeClassMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-01 16:13:30
     */
    @Override
    public List<SchGradeClass> BatchSelect(Map<String, Object> map) {
        return schGradeClassMapper.BatchSelect(map);
    }

    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2020-12-01 16:17:21
     */
    @Override
    public Integer save(SchGradeClass record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入学校年级班级失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_GRADE_CLASS, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID更新
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2020-12-01 16:21:36
     */
    @Override
    public Integer update(SchGradeClass record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchGradeClass originalRecord = this.selectByPrimaryKey(record.getId());
        Assert.notNull(originalRecord,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新学校年级班级失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_GRADE_CLASS, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(originalRecord), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-01 16:26:08
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchGradeClass record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_GRADE_CLASS, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

    /**
     * 根据IDs批量删除
     * @param schGradeClassIds 学校年级班级ID，对应学校年级班级信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId              权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-01 16:31:25
     */
    @Override
    public Integer batchDelete(String schGradeClassIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(schGradeClassIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SchGradeClass> schGradeClasses = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(schGradeClasses) && schGradeClasses.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 1.批量删除
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_GRADE_CLASS, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, schGradeClasses, null);
        Assert.isTrue(count== schGradeClasses.size() && b,"DB_SQL_DELETE_ERROR");
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
     * @date 2020-12-01 16:33:19
     */
    @Override
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception {
        return null;
    }
}
