package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SchDeviceMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchDevice;
import com.shouzhi.pojo.db.SchSpace;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.common.FunctionWrapper;
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
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 学校设备信息表业务层接口实现类
 * @author WX
 * @date 2020-11-11 14:12:11
 */
@Service("schDeviceService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SchDeviceServiceImpl implements ISchDeviceService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchDeviceMapper schDeviceMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;

    @Autowired  //@Qualifier("customProperties")
    private Properties customProperties;

    @Autowired
    private ISchSpaceService schSpaceService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return schDeviceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SchDevice record) throws Exception {
        return schDeviceMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SchDevice record) throws Exception {
        return schDeviceMapper.insertSelective(record);
    }

    @Override
    public SchDevice selectByPrimaryKey(String id) {
        return schDeviceMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SchDevice record) throws Exception {
        return schDeviceMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SchDevice record) throws Exception {
        return schDeviceMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-11-11 14:00:10
     */
    @Override
    public List<SchDevice> queryListByPage(Map<String, Object> map) {
        return schDeviceMapper.queryListByPage(map);
    }

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-03-08 13:47:19
     */
    @Override
    public Integer batchInsert(List<SchDevice> list) throws Exception {
        return schDeviceMapper.batchInsert(list);
    }

    /**
     * 批量更新根据ID
     * @param map 参数+schDeviceIds列表
     * @author WX
     * @date 2020-11-11 14:01:26
     */
    @Override
    public Integer BatchUpdate(Map<String, Object> map) throws Exception {
        return schDeviceMapper.BatchUpdate(map);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-20 14:11:06
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return schDeviceMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-20 14:23:17
     */
    @Override
    public List<SchDevice> BatchSelect(Map<String, Object> map) {
        return schDeviceMapper.BatchSelect(map);
    }

    /**
     * 新增学校设备信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-11-11 14:08:06
     */
    @Override
    public Integer save(SchDevice record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 检查某个设备类型下的设备已有数量
        this.checkSchDeviceTypeQuantity(record);
        SchSpace ss = schSpaceService.selectByPrimaryKey(record.getSchSpaceId());
        Assert.notNull(ss,"DB_SQL_ID_INVALID_ERROR！");
        this.setUrlForSchDevice(record, ss);
        // 设备本身是否支持云台控制，0：不支持，1：支持
        if("1".equals(record.getIsSupportControl())){
            record.setIsEnableControl("1");
        }
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入学校设备信息失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_DEVICE, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    // 检查某个设备类型下的设备已有数量，同一空间设备类型为1的允许存在2个，2和overlay允许存在1个
    private void checkSchDeviceTypeQuantity(SchDevice record){
        HashMap<String, Object> map = new HashMap<>();
        map.put("schSpaceId", record.getSchSpaceId());
        List<SchDevice> schDevices = this.queryListByPage(map);
        long count = schDevices.parallelStream().filter(sd -> sd.getDeviceType().equals(record.getDeviceType())).count();
        switch (record.getDeviceType()){
            case "1":
                Assert.isTrue(count<2, "SCH_DEVICE_TYPE_QUANTITY_REACHED_LIMIT_ERROR");
                break;
            case "2":
            case "overlay":
                Assert.isTrue(count<1, "SCH_DEVICE_TYPE_QUANTITY_REACHED_LIMIT_ERROR");
                break;
        }
    }
    // 为SchDevice设置各种url
    private void setUrlForSchDevice(SchDevice record, SchSpace ss){
        Assert.isTrue("5".equals(ss.getSpaceType()), "SCH_DEVICE_SELECTED_SPACE_NOT_INSERT_ERROR");
        String baseStream = this.getBaseStream(ss.getSpaceCode(), ss.getParentSpaceCodes());
        // 生成RTSP主码流、子码流地址 | 设备类型，1：摄像机，2：讲台计算机
        if("1".equals(record.getDeviceType())){
            // 接入协议，1：海康，2：大华，3：ONVIF
            switch(record.getAccessProtocol()){
                case "1":
                    record.setRtspUrlMain(this.rtspUrlByTemplet("rtsp.hikvision.templet", record, "main"));
                    record.setRtspUrlSub(this.rtspUrlByTemplet("rtsp.hikvision.templet", record, "sub"));
                    break;
                case "2":
                    record.setRtspUrlMain(this.rtspUrlByTemplet("rtsp.dahuatech.templet", record, "0"));
                    record.setRtspUrlSub(this.rtspUrlByTemplet("rtsp.dahuatech.templet", record, "1"));
                    break;
            }
            // 拼接推拉流地址，规则：appName+空间编号(保证不能是中文)_deviceLocation，如：/live/ZJCMXY_XSXQ_JXL1_3F_302_NW
            baseStream+="_"+record.getDeviceLocation();
        }
        if("2".equals(record.getDeviceType())){
            baseStream+="_VGA";
            record.setDeviceLocation("PF");
        }
        if("overlay".equals(record.getDeviceType())){
            baseStream+="_OVERLAY";
        }
        record.setRtmpGetUrl(baseStream);
        record.setFlvGetUrl(baseStream+".flv");
        record.setHlsGetUrl(baseStream+".m3u8");
    }


    /**
     * 批量新增学校设备信息
     * @param list
     * @param permId
     * @author WX
     * @date 2021-03-08 13:51:19
     */
    @Override
    public Integer batchSave(List<SchDevice> list, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        //  批量新增学校设备信息
        Integer count = this.batchInsert(list);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_DEVICE, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, list);
        Assert.isTrue(count==list.size() && b,"DB_SQL_INSERT_ERROR");
        return count;
    }

    /**
     * 批量新增学校设备合成流，每间教室对应一个合成流设备
     * 目前用在空间批量导入时的批量生成设备合成流
     * @param records
     * @param permId
     * @author WX
     * @date 2021-03-09 15:31:35
     */
    @Override
    public Integer batchSaveOverlayStream(List<SchSpace> records, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        if(CollectionUtils.isEmpty(records)) return 0;
        List<SchDevice> schDevices = records.stream().filter(r -> "5".equals(r.getSpaceType())).map(r -> {
            SchDevice schDevice = new SchDevice();
            schDevice.setSchSpaceId(r.getId());
            schDevice.setDeviceName("设备合成流");
            schDevice.setDeviceLocation("OT");
            schDevice.setDeviceType("overlay");
            this.setUrlForSchDevice(schDevice, r);
            schDevice.setConnectStatus("0");
            schDevice.setIsSupportControl("0");
            schDevice.setIsSupportAbsoluteMove("0");
            schDevice.setIsSupportContinuousMove("0");
            schDevice.setIsEnableControl("0");
            schDevice.setIsActive("1");
            schDevice.setId(UuidUtil.get32UUID());
            schDevice.setCreateId(userInfo.getId());
            schDevice.setCreateBy(userInfo.getUserName());
            schDevice.setCreateWay("1");
            schDevice.setCreateTime(new Date());
            return schDevice;
        }).collect(Collectors.toList());
        // 生成设备合成流的数量不等于教室空间的数量（一间教室仅存在一个合成流）说明存在不是教室类型的记录，直接返回
        if(schDevices.size()!=records.size()) return 0;
        Integer count = this.batchSave(schDevices, permId, req);
        return count;
    }


    /**
     * 根据ID更新学校设备信息
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2020-11-11 14:10:32
     */
    @Override
    public Integer update(SchDevice record, Boolean allColumn, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchDevice sd = this.selectByPrimaryKey(record.getId());
        Assert.notNull(sd,"DB_SQL_ID_INVALID_ERROR！");
        if(allColumn){
            SchSpace ss = schSpaceService.selectByPrimaryKey(record.getSchSpaceId());
            Assert.notNull(ss,"DB_SQL_ID_INVALID_ERROR！");
            this.setUrlForSchDevice(record, ss);
        }
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新学校设备信息失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_DEVICE, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(sd), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除学校设备信息
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-11 14:13:16
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除学校设备信息
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchDevice record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_DEVICE, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

    /**
     * 根据IDs批量删除学校设备信息
     * @param schDeviceIds 学校设备信息ID，对应学校设备信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId          权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-11 14:16:20
     */
    @Override
    public Integer batchDelete(String schDeviceIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(schDeviceIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SchDevice> schDevices = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(schDevices) && schDevices.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_DEVICE, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, schDevices, null);
        Assert.isTrue(count== schDevices.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

    /**
     * 导入学校设备信息
     * @param permId     权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile  excel文件
     * @author WX
     * @date 2021-03-05 09:24:36
     */
    @Override
    public Integer impSchDevice(String permId, MultipartFile excelFile, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        List<ArrayList<String>> ttcList = baseService.verifyImpExcel(excelFile, 3);

        // 根据空间名称查询对应的空间
        List<ArrayList<String>> tempList = new ArrayList<>(ttcList);
        // 去掉表头行和注意事项行
        tempList.remove(0);
        tempList.remove(0);
        Set<String> schSpaceNameSet = tempList.parallelStream().map(strings -> strings.get(2).trim()).collect(Collectors.toSet());
        Map<String, Object> map = new HashMap<>();
        map.put("list",schSpaceNameSet);
        map.put("schSpaceNamesIn","1");
        List<SchSpace> schSpaces = schSpaceService.BatchSelect(map);
        if(schSpaces.size()!=schSpaceNameSet.size()){
            throw new FileImportException("‘设备所在区域’存在无效数据，请检查更正后重新上传！模板内存在"+schSpaceNameSet.size()+"个空间(去重后)，实际查到"+schSpaces.size()+"个");
        }
        Map<String, SchSpace> sSNameMap = schSpaces.parallelStream().collect(Collectors.toMap(s -> s.getParentSpaceNames()+s.getSpaceName(), s -> s));

        // 检查某个设备类型下的设备已有数量，同一空间设备类型为1的允许存在2个，2和overlay允许存在1个
        Map<String, Map<String, List<ArrayList<String>>>> sSNameGroupMap =
                tempList.parallelStream().collect(Collectors.groupingBy(strings -> strings.get(2), Collectors.groupingBy(strings -> {
                    if ("2".equals(strings.get(5))) {
                        return "2";
                    } else if ("1".equals(strings.get(5))) {
                        return "1";
                    }
                    return null;
                })));
        schSpaceNameSet.parallelStream().forEach(schSpaceName -> {
            List<ArrayList<String>> arrayLists1 = sSNameGroupMap.get(schSpaceName).get("1");
            List<ArrayList<String>> arrayLists2 = sSNameGroupMap.get(schSpaceName).get("2");
            if(arrayLists1!=null&&arrayLists2!=null&&(arrayLists1.size()+arrayLists2.size()!=3)){
                throw new FileImportException("同一‘设备所在区域’设备上限为3个：摄像机2个+讲台计算机1个。参考区域‘"+schSpaceName+"’");
            }
        });

        // 将excel内数据取出组装成 List<SchDevice>
        List<SchDevice> list = new ArrayList<>();
        Set<String> schSpaceIds = new HashSet<>();
        for (int i=2;i<ttcList.size();i++) {								//从第三行开始取
            ArrayList<String> row = ttcList.get(i);							//获取当前行
            if(row==null){continue;}//判断是否为空
            if(row.size()<6||StringUtils.isBlank(row.get(0))||StringUtils.isBlank(row.get(2))||StringUtils.isBlank(row.get(3))||StringUtils.isBlank(row.get(4))||StringUtils.isBlank(row.get(5))){
                throw new FileImportException("当前sheet内存在无效数据，请填写完整或清除后重新上传！参考有效行(空行除外)：第"+(i+1)+"行");
            }
            SchDevice schDevice = new SchDevice();
            schDevice.setId(UuidUtil.get32UUID());
            schDevice.setDeviceName(row.get(0));
            schDevice.setSerialNumber(row.get(1));
            SchSpace ss = sSNameMap.get(row.get(2).trim());
            schDevice.setSchSpaceId(ss.getId());
            schDevice.setDeviceLocation(row.get(3));
            schDevice.setIpAddr(row.get(4));
            schDevice.setDeviceType(row.get(5));
            // 以下四个字段默认为‘0’，若支持，则会被覆盖为‘1’
            schDevice.setIsSupportControl("0");
            schDevice.setIsSupportAbsoluteMove("0");
            schDevice.setIsSupportContinuousMove("0");
            schDevice.setIsEnableControl("0");
            // 摄像机类型继续校验其余必填字段
            if("1".equals(row.get(5))){
                if(row.size()<10||StringUtils.isBlank(row.get(6))||StringUtils.isBlank(row.get(7))||StringUtils.isBlank(row.get(8))||StringUtils.isBlank(row.get(9))){
                    throw new FileImportException("设备类型为摄像机的必填列部分未填写，请填写完整后重新上传！参考有效行(空行除外)：第"+(i+1)+"行");
                }
                schDevice.setAccessProtocol(row.get(6));
                schDevice.setAccessUser(row.get(7));
                schDevice.setAccessPwd(row.get(8));
                schDevice.setIsSupportControl(row.get(9));
                // 摄像机类型且支持云台继续校验其余必填字段
                if("1".equals(row.get(9))){
                    if(row.size()<12||StringUtils.isBlank(row.get(10))||StringUtils.isBlank(row.get(11))){
                        throw new FileImportException("设备类型为摄像机且支持云台的必填列部分未填写，请填写完整后重新上传！参考有效行(空行除外)：第"+(i+1)+"行");
                    }
                    schDevice.setOnvifUser(row.get(10));
                    schDevice.setOnvifPwd(row.get(11));
                    schDevice.setIsEnableControl("1");
                    schDevice.setIsSupportAbsoluteMove("1");
                    schDevice.setIsSupportContinuousMove("1");
                }
            }
            schDevice.setRemark(12<row.size()&&StringUtils.isNotBlank(row.get(12))?row.get(12):null);
            schDevice.setConnectStatus("0");
            schDevice.setIsActive("1");
            schDevice.setCreateId(userInfo.getId());
            schDevice.setCreateBy(userInfo.getUserName());
            schDevice.setCreateWay("1");
            schDevice.setCreateTime(new Date());
            this.setUrlForSchDevice(schDevice, ss);
            list.add(schDevice);
            schSpaceIds.add(ss.getId());
        }

        this.batchDeleteBySchSpaceId(schSpaceIds, permId, req, false);
        Integer count = this.batchSave(list, permId, req);
        //
        ttcList.clear();ttcList=null;
        tempList.clear();tempList=null;
        schSpaceNameSet.clear();schSpaceNameSet=null;
        schSpaces.clear();schSpaces=null;
        sSNameMap.clear();sSNameMap=null;
        sSNameGroupMap.clear();
        list.clear();list=null;
        return count;
    }



    /**
     * 根据学校空间信息ID批量删除设备信息
     * @param schSpaceIdOrIds 单个schSpaceId或List的schSpaceIds
     * @param permId         权限ID或菜单ID(仅限于最后级别的菜单)
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-11 16:52:18
     */
    @Override
    public Integer batchDeleteBySchSpaceId(Object schSpaceIdOrIds, String permId, HttpServletRequest req, boolean strictMode) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        Map<String, Object> map = new HashMap<>();
        if(schSpaceIdOrIds instanceof String){
            String schSpaceId = (String)schSpaceIdOrIds;
            map.put("schSpaceIdEq",schSpaceId);
        } else if(schSpaceIdOrIds instanceof List){
            List<String> schSpaceIds =  (List<String>)schSpaceIdOrIds;
            Assert.isTrue(CollectionUtils.isNotEmpty(schSpaceIds),"COMMON_INVALID_ARG_ERROR");
            map.put("list",schSpaceIds);
            map.put("schSpaceIdIn","1");
        } else if(schSpaceIdOrIds instanceof Set){
            List<String> schSpaceIds = new ArrayList<>((Set<String>)schSpaceIdOrIds);
            Assert.isTrue(CollectionUtils.isNotEmpty(schSpaceIds),"COMMON_INVALID_ARG_ERROR");
            map.put("list",schSpaceIds);
            map.put("schSpaceIdIn","1");
        }else {
            throw new IllegalArgumentException("COMMON_INVALID_ARG_ERROR");
        }

        // 查询要删除的数据是否存在
        List<SchDevice> schDevices = this.BatchSelect(map);
        if(!strictMode&&CollectionUtils.isEmpty(schDevices)) return 0;
        Assert.isTrue(CollectionUtils.isNotEmpty(schDevices),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_DEVICE, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, schDevices, null);
        Assert.isTrue(count== schDevices.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }


    /**
     * 根据学校空间信息ID更新流地址
     * @param newSchSpace 新学校空间信息
     * @param oldSchSpace 旧学校空间信息
     * @author WX
     * @date 2020-11-19 10:51:06
     */
    @Override
    public Integer updateStreamUrlBySchSpaceId(SchSpace newSchSpace, SchSpace oldSchSpace) throws Exception {
        String newBaseStream = this.getBaseStream(newSchSpace.getSpaceCode(), oldSchSpace.getParentSpaceCodes())+"_";
        String oldBaseStream = this.getBaseStream(oldSchSpace.getSpaceCode(), oldSchSpace.getParentSpaceCodes())+"_";
        Map map = new HashMap<>();
        map.put("rtmpGetUrlReplaceFrom",oldBaseStream);
        map.put("rtmpGetUrlReplaceTo",newBaseStream);
        map.put("flvGetUrlReplaceFrom",oldBaseStream);
        map.put("flvGetUrlReplaceTo",newBaseStream);
        map.put("hlsGetUrlReplaceFrom",oldBaseStream);
        map.put("hlsGetUrlReplaceTo",newBaseStream);
        map.put("schSpaceIdEq",newSchSpace.getId());
        return this.BatchUpdate(map);
    }


    /**
     * 查询设备是否在线
     * @param schDeviceId
     * @author WX
     * @date 2020-11-12 10:28:16
     */
    @Override
    public SchDevice checkOnline(String schDeviceId) throws Exception {
        SchDevice sd = this.selectByPrimaryKey(schDeviceId);
        return this.doCheckOnline(sd);
    }

    /**
     * 批量查询设备是否在线
     * @param schDeviceIds 学校设备信息ID，对应学校设备信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-12-14 16:40:27
     */
    @Override
    public List<SchDevice> batchCheckOnline(String schDeviceIds) throws Exception {
        List<String> list = Arrays.asList(schDeviceIds.split(","));
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SchDevice> schDevices = this.BatchSelect(map);
        schDevices = schDevices.parallelStream().map(FunctionWrapper.throwingFunctionWrapper(s -> this.doCheckOnline(s))).collect(Collectors.toList());
        logger.info("---------------检测完成---------------");
        return schDevices;
    }

    // 根据rtspUrlTemplet生成对应的rtspUrl
    private String rtspUrlByTemplet(String propertyKey, SchDevice record, String subtype){
        return customProperties.getProperty(propertyKey).replace("[username]", record.getAccessUser()).replace("[password]", record.getAccessPwd()).replace("[ip]", record.getIpAddr()).replace("[subtype]",subtype);
    }

    // 根据parentSpaceCodes、spaceCode、appName获取拼接后的基础流地址，如：/live/ZJCMXY_XSXQ_JXL1_3F_302
    private String getBaseStream(String spaceCode, String parentSpaceCodes){
        return customProperties.getProperty("live.app.name.base")+String.join("", parentSpaceCodes.substring(1).replaceAll("/","_"), spaceCode);
    }

    // 检测单个设备是否在线
    private SchDevice doCheckOnline(SchDevice sd) throws Exception {
        if (sd==null) throw new IllegalArgumentException("COMMON_INVALID_ARG_ERROR");
        SchDevice online = new SchDevice();
        online.setId(sd.getId());
        online.setConnectStatus("0");
        if(StringUtils.isBlank(sd.getIpAddr())) return online;

        InetAddress inet = null;
        try {
            inet = InetAddress.getByName(sd.getIpAddr());
            if(inet.isReachable(1000)) // 5000
                online.setConnectStatus("1");
        } catch (IOException e) {
        }finally {
            inet = null;
            sd = null;
        }
        return online;
    }
}
