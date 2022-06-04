package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shouzhi.basic.constants.DatePatterns;
import com.shouzhi.basic.utils.CharacterDetectUtil;
import com.shouzhi.basic.utils.ExcelRead;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SchSpaceMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchDevice;
import com.shouzhi.pojo.db.SchSpace;
import com.shouzhi.pojo.db.SysUser;
import com.shouzhi.pojo.vo.TreeNodeVo;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.common.DistinctWrapper;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.customexception.FileImportException;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.ISchDeviceService;
import com.shouzhi.service.interf.db.ISchSpaceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学校空间表业务层接口实现类
 * @author WX
 * @date 2020-11-05 17:38:00
 */
@Service("schSpaceService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SchSpaceServiceImpl implements ISchSpaceService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchSpaceMapper schSpaceMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;

    @Autowired
    ISchDeviceService schDeviceService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return schSpaceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SchSpace record) throws Exception {
        return schSpaceMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SchSpace record) throws Exception {
        return schSpaceMapper.insertSelective(record);
    }

    @Override
    public SchSpace selectByPrimaryKey(String id) {
        return schSpaceMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SchSpace record) throws Exception {
        return schSpaceMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SchSpace record) throws Exception {
        return schSpaceMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2021-01-20 17:51:36
     */
    @Override
    public SchSpace selectOneByParam(Map<String, Object> map) {
        return schSpaceMapper.selectOneByParam(map);
    }

    /**
     * 查询总数
     * @author WX
     * @date 2020-11-16 15:53:09
     */
    @Override
    public Integer selectCount() {
        return schSpaceMapper.selectCount();
    }

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-11-05 17:18:21
     */
    @Override
    public List<SchSpace> queryListByPage(SchSpace record) {
        return schSpaceMapper.queryListByPage(record);
    }

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-03-02 15:32:06
     */
    @Override
    public Integer batchInsert(List<SchSpace> list) throws Exception {
        return schSpaceMapper.batchInsert(list);
    }

    /**
     * 批量更新根据ID
     * @param map 参数+schSpaceIds列表
     * @author WX
     * @date 2020-11-06 11:20:09
     */
    @Override
    public Integer BatchUpdate(Map<String, Object> map) throws Exception {
        return schSpaceMapper.BatchUpdate(map);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-21 18:14:23
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return schSpaceMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-21 18:18:07
     */
    @Override
    public List<SchSpace> BatchSelect(Map<String, Object> map) {
        return schSpaceMapper.BatchSelect(map);
    }

    /**
     * 新增学校空间信息
     * @param record
     * @param permId
     * @param req
     * @author WX
     * @date 2020-11-06 10:59:12
     */
    @Override
    public Integer save(SchSpace record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 无父节点，
        if(StringUtils.isBlank(record.getParentId())){
            Integer selectCount = this.selectCount();
            // 数据库里存在记录，报错，必须选择父节点才可添加
            if(selectCount>0) throw new IllegalArgumentException("TREE_NOT_SELECTED_PARENT_NODE_ERROR");
            // 库里无记录，说明第一次添加，本条记录为根节点，很特殊
            record.setParentId("0");
            record.setParentIds("/0/");
            record.setParentSpaceCode("/");
            record.setParentSpaceCodes("/");
            record.setParentSpaceName("/");
            record.setParentSpaceNames("/");
        } else {
        // 有父节点
            SchSpace parentSchSpace = this.selectByPrimaryKey(record.getParentId());
            Assert.notNull(parentSchSpace,"DB_SQL_ID_INVALID_ERROR！");
            List<SchSpace> originalSchSpaces = this.originalSchSpacesByParentIds(parentSchSpace.getParentIds() + parentSchSpace.getId() + "/");
            // 检测spaceCode(上边的第一次添加无需检测)
            this.detectSpaceCode(originalSchSpaces, record.getSpaceCode(), null);
            this.fillParentField(parentSchSpace, record);
        }

        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入学校空间信息失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_SPACE, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));

        // 如果空间类型是教室，自动添加‘合成流’到设备信息表(因为合成流的添加放在设备添加处不好做)
        this.makeSchDeviceOverlayStream(record, permId, req);
        return count;
    }

    // 给定一个ParentIds，查询与该ParentIds关联的数据
    private List<SchSpace> originalSchSpacesByParentIds(String parentIds){
        SchSpace schSpace = new SchSpace();
        schSpace.setParentIds(parentIds);
        return this.queryListByPage(schSpace);
    }

    // 生成设备合成流
    private void makeSchDeviceOverlayStream(SchSpace record, String permId, HttpServletRequest req) throws Exception {
        if("5".equals(record.getSpaceType())){
            SchDevice schDevice = new SchDevice();
            schDevice.setSchSpaceId(record.getId());
            schDevice.setDeviceName("设备合成流");
            schDevice.setDeviceLocation("OT");
            schDevice.setDeviceType("overlay");
            schDeviceService.save(schDevice, permId, req);
        }
    }

    // 填充Parent字段信息
    private void fillParentField(SchSpace parentSchSpace, SchSpace record){
        String parentIds = parentSchSpace.getParentIds()+ parentSchSpace.getId()+"/";
        String parentSpaceCodes = parentSchSpace.getParentSpaceCodes()+ parentSchSpace.getSpaceCode()+"/";
        String parentSpaceNames = parentSchSpace.getParentSpaceNames()+ parentSchSpace.getSpaceName()+"/";
        record.setParentIds(parentIds);
        record.setParentSpaceCode(parentSchSpace.getSpaceCode());
        record.setParentSpaceCodes(parentSpaceCodes);
        record.setParentSpaceName(parentSchSpace.getSpaceName());
        record.setParentSpaceNames(parentSpaceNames);
    }

    // 检测spaceCode是否是在字母数字下划线范围内、以及同一父级下是否有重复值(自己更新自己除外)
    private void detectSpaceCode(List<SchSpace> originalSchSpaces, String spaceCode, String id) throws IllegalArgumentException {
        if(!CharacterDetectUtil.isRangeWordNumber_(spaceCode)){
            throw new IllegalArgumentException("SCH_SPACE_CODE_INVALID_ERROR");
        }
        long count = originalSchSpaces.stream().filter(ss -> ss.getSpaceCode().equals(spaceCode) && !ss.getId().equals(id)).count();
        if(count>0){
            throw new IllegalArgumentException("SCH_SPACE_CODE_EXIST_ERROR");
        }
    }


    /**
     * 批量新增学校空间信息
     * @param list
     * @param permId
     * @param req
     * @author WX
     * @date 2021-03-02 15:32:06
     */
    @Override
    public Integer batchSave(List<SchSpace> list, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        //  批量新增学校空间信息
        Integer count = this.batchInsert(list);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_SPACE, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, list);
        Assert.isTrue(count==list.size() && b,"DB_SQL_INSERT_ERROR");
        return count;
    }


    /**
     * 根据ID更新学校空间信息
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param req
     * @param operType
     * @author WX
     * @date 2020-11-06 11:02:06
     */
    @Override
    public Integer update(SchSpace record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setParentSpaceNames(null); // TODO 防止被提交此字段 （解决了屏蔽字段后将此删除）
        SchSpace originalRecord = this.selectByPrimaryKey(record.getId());
        Assert.notNull(originalRecord,"DB_SQL_ID_INVALID_ERROR");
        Assert.isTrue(originalRecord.getSpaceType().equals(record.getSpaceType()),"SCH_SPACE_TYPE_NOT_EQUAL_ERROR");

        SchSpace parentSchSpace = this.selectByPrimaryKey(record.getParentId());
        Assert.notNull(parentSchSpace,"DB_SQL_ID_INVALID_ERROR！");
        List<SchSpace> originalSchSpaces = this.originalSchSpacesByParentIds(parentSchSpace.getParentIds() + parentSchSpace.getId() + "/");
        this.detectSpaceCode(originalSchSpaces, record.getSpaceCode(), record.getId());

        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新学校空间信息失败！");

        // SpaceCode、SpaceName改变时级联更改与其关联的 parentSpaceCode、parentSpaceCodes、parentSpaceName、parentSpaceNames
        // SpaceCode有改变才级联更新 [删除操作不会进入，更新操作未改变该字段也不会进入]
        if(!record.getSpaceCode().equals(originalRecord.getSpaceCode())){
            Map<String, Object> map = new HashMap<>();
            map.put("parentSpaceCode",record.getSpaceCode());
            map.put("parentSpaceCodeEq",originalRecord.getSpaceCode());
            map.put("parentSpaceCodesLike",originalRecord.getParentSpaceCodes());
            this.BatchUpdate(map);
            // 需要有分别更新各自的parentSpaceCode、parentSpaceCodes关联字段，不能一次更新，因为两个字段的内容不在同一个维度
            map = new HashMap<>();
            map.put("parentSpaceCodesReplaceFrom","/"+originalRecord.getSpaceCode()+"/");
            map.put("parentSpaceCodesReplaceTo","/"+record.getSpaceCode()+"/");
            map.put("parentSpaceCodesLike",originalRecord.getParentSpaceCodes()+originalRecord.getSpaceCode()+"/");
            this.BatchUpdate(map);

            // 级联更新设备表里的推流地址，因推流地址是根据SpaceCode生成，SpaceCode更改，推流地址也需更改
            if("5".equals(record.getSpaceType())) schDeviceService.updateStreamUrlBySchSpaceId(record, originalRecord);
        }
        // SpaceName有改变才级联更新 [删除操作不会进入，更新操作未改变该字段也不会进入]
        if(!record.getSpaceName().equals(originalRecord.getSpaceName())){
            Map<String, Object> map = new HashMap<>();
            map.put("parentSpaceName",record.getSpaceName());
            map.put("parentSpaceNameEq",originalRecord.getSpaceName());
            map.put("parentSpaceNamesLike",originalRecord.getParentSpaceNames());
            this.BatchUpdate(map);
            // 需要有分别更新各自的parentSpaceName、parentSpaceNames关联字段，不能一次更新，因为两个字段的内容不在同一个维度
            map = new HashMap<>();
            map.put("parentSpaceNamesReplaceFrom","/"+originalRecord.getSpaceName()+"/");
            map.put("parentSpaceNamesReplaceTo","/"+record.getSpaceName()+"/");
            map.put("parentSpaceNamesLike",originalRecord.getParentSpaceNames()+originalRecord.getSpaceName()+"/");
            this.BatchUpdate(map);
        }

        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_SPACE, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(originalRecord), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除学校空间信息
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param req
     * @author WX
     * @date 2020-11-06 11:07:22
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除学校空间信息
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchSpace record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志(涉及到级联的操作，此日志操作一定要在级联操作前)
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_SPACE, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");

        // 2.移除与该学校空间信息关联的子学校空间信息集
        this.batchDeleteByMultiParam("parentIdsLike", "/"+rowId+"/", permId, userInfo, false);

        // 3.移除与该学校空间信息关联的设备信息
        schDeviceService.batchDeleteBySchSpaceId(rowId, permId, req, false);
        return count;
    }

    /**
     * 根据IDs批量删除学校空间信息
     * @param schSpaceIds 学校空间信息ID，对应学校空间信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId         权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-06 11:18:06
     */
    @Override
    public Integer batchDelete(String schSpaceIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(schSpaceIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SchSpace> schSpaces = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(schSpaces) && schSpaces.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_SPACE, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, schSpaces, null);
        Assert.isTrue(count== schSpaces.size() && b,"DB_SQL_DELETE_ERROR");

        // 2.批量移除与该学校空间信息关联的子学校空间信息集
        this.batchDeleteByMultiParam("parentIdsRegexp", list, permId, userInfo, false);

        // 3.批量移除与该学校空间信息关联的设备信息
        schDeviceService.batchDeleteBySchSpaceId(list, permId, req, false);
        return count;
    }

    /**
     * 根据多参数批量删除
     * @param paramKey   删除参数key
     * @param paramVal   删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-26 17:04:02
     */
    @Override
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        switch (paramKey){
            case "parentIdsLike":
                map.put("parentIdsLike",paramVal);
                break;
            case "parentIdsRegexp":
                List<String> parentIds =  (List<String>)paramVal;
                Assert.isTrue(CollectionUtils.isNotEmpty(parentIds),"COMMON_INVALID_ARG_ERROR");
                map.put("parentIdsRegexp",parentIds.stream().map(s-> String.join(s,"/","/")).collect(Collectors.joining("|")));
                break;
            default:
                throw new IllegalArgumentException("COMMON_INVALID_ARG_ERROR");
        }

        // 查询要删除的数据是否存在
        List<SchSpace> schSpaces = this.BatchSelect(map);
        if(!strictMode&&CollectionUtils.isEmpty(schSpaces)) return 0;
        Assert.isTrue(CollectionUtils.isNotEmpty(schSpaces),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_SPACE, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, schSpaces, null);
        Assert.isTrue(count== schSpaces.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }


    /**
     * 查询学校空间信息树
     * @param req
     * @author WX
     * @date 2020-11-06 16:23:16
     */
    @Override
    public List<TreeNodeVo> findTree(HttpServletRequest req) {
        List<SchSpace> schSpaces = this.queryListByPage(new SchSpace());
        return TreeNodeVo.buildTree(schSpaces.stream().map(s -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("spaceType", s.getSpaceType());
                    jsonObject.put("parentSpaceNames", s.getParentSpaceNames());
                    return new TreeNodeVo(s.getId(), s.getId(), s.getParentId(), s.getSpaceName(), jsonObject);
                }).collect(Collectors.toList()), "0");
    }


    /**
     * 导入部分学校空间信息
     * @param permId     权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile  excel文件
     * @param parentId 父结点id
     * @param delOldData 是否移除旧数据，是为1，否为0
     * @param req
     * @author WX
     * @date 2021-03-02 09:33:19
     */
    @Override
    public Integer impPartSchSpace(String permId, MultipartFile excelFile, String parentId, String delOldData, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        List<ArrayList<String>> ttcList = baseService.verifyImpExcel(excelFile, 3);
        SchSpace parentSchSpace = this.selectByPrimaryKey(parentId);
        if (parentSchSpace == null) throw new FileImportException("父节点不存在");
        Assert.isTrue(!"5".equals(parentSchSpace.getSpaceType()), "SCH_SPACE_SELECTED_PARENT_SPACE_NOT_INSERT_ERROR");
        // 移除旧数据
        if("1".equals(delOldData)){
            SchSpace schSpace = new SchSpace();
            schSpace.setParentIdsLike(String.join(parentId,"/","/"));
            List<SchSpace> schSpaces = this.queryListByPage(schSpace);
            if(CollectionUtils.isNotEmpty(schSpaces)){
                String ids = schSpaces.stream().map(SchSpace::getId).collect(Collectors.joining(","));
                this.batchDelete(ids, permId, req);
            }
        }
        // 根据parentId再次查询其下孩子节点空间数据，用于校验SpaceCode
        List<SchSpace> originalSchSpaces = this.originalSchSpacesByParentIds(parentSchSpace.getParentIds() + parentSchSpace.getId() + "/");

        // 将excel内数据取出组装成 List<SchSpace>
        List<SchSpace> list = new ArrayList<>();
        Set<String> spaceTypeSet = new HashSet<>();
        for (int i=2;i<ttcList.size();i++) {								//从第三行开始取
            ArrayList<String> row = ttcList.get(i);							//获取当前行
            if(row==null){continue;}//判断是否为空
            if(StringUtils.isBlank(row.get(0))||StringUtils.isBlank(row.get(1))||StringUtils.isBlank(row.get(2))||StringUtils.isBlank(row.get(3))){
                throw new FileImportException("当前sheet内存在无效数据，请填写完整或清除后重新上传！参考有效行(空行除外)：第"+(i+1)+"行");
            }
            SchSpace schSpace = new SchSpace();
            schSpace.setId(UuidUtil.get32UUID());
            schSpace.setParentId(parentId);
            schSpace.setSpaceName(row.get(0));
            schSpace.setSpaceCode(row.get(1));
            schSpace.setSpaceType(row.get(2));
            schSpace.setSortNum(Integer.valueOf(row.get(3)));
            schSpace.setRemark(4<row.size()&&StringUtils.isNotBlank(row.get(4))?row.get(4):null);
            // 检测spaceCode 是否合法
            try {
                this.detectSpaceCode(originalSchSpaces, schSpace.getSpaceCode(), null);
            } catch (Exception e){
                throw new FileImportException("该空间编码‘"+schSpace.getSpaceCode()+"’无效或已存在！参考有效行(空行除外)：第"+(i+1)+"行");
            }
            this.fillParentField(parentSchSpace, schSpace);
            schSpace.setCreateId(userInfo.getId());
            schSpace.setCreateBy(userInfo.getUserName());
            schSpace.setCreateWay("1");
            schSpace.setCreateTime(new Date());
            list.add(schSpace);
            spaceTypeSet.add(schSpace.getSpaceType());
        }
        if(spaceTypeSet.size()!=1) throw new FileImportException("模板内存在不同空间类型数据，不可混合交叉导入！");
        Integer count = this.batchSave(list, permId, req);
        // 如果空间类型是教室，自动添加‘合成流’到设备信息表(因为合成流的添加放在设备添加处不好做)
        schDeviceService.batchSaveOverlayStream(list, permId, req);
        return count;
    }
}
