package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.basic.constants.DatePatterns;
import com.shouzhi.basic.utils.CredentialsUtil;
import com.shouzhi.basic.utils.ExcelRead;
import com.shouzhi.mapper.SysUserMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SysRole;
import com.shouzhi.pojo.db.SysRolePermission;
import com.shouzhi.pojo.db.SysUser;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.common.DistinctWrapper;
import com.shouzhi.service.common.FileUploadService;
import com.shouzhi.service.common.RedisTemplateService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.constants.RedisConst;
import com.shouzhi.service.constants.SecretKeyConst;
import com.shouzhi.service.customexception.FileImportException;
import com.shouzhi.service.interf.db.*;
import com.shouzhi.basic.utils.UuidUtil;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户表业务层接口实现类
 * @author WX
 * @date 2020-06-15 09:51:45
 */
@Service("sysUserService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SysUserServiceImpl implements ISysUserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private IShortcutMenuService shortcutMenuService;

    @Autowired
    private IBasicAuthService basicAuthService;

    @Autowired
    private RedisTemplateService redisTemplateService;

    @Autowired  //@Qualifier("customProperties")
    private Properties customProperties;

    @Autowired
    private BaseService baseService;

    @Autowired
    private FileUploadService fileUploadService;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return sysUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SysUser record) throws Exception {
        return sysUserMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SysUser record) throws Exception {
        return sysUserMapper.insertSelective(record);
    }

    @Override
    public SysUser selectByPrimaryKey(String id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SysUser record) throws Exception {
        return sysUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SysUser record) throws Exception {
        return sysUserMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询系统用户列表
     * @param record
     * @author WX
     * @date 2020-06-15 09:41:23
     */
    @Override
    public List<SysUser> queryListByPage(SysUser record) {
        return sysUserMapper.queryListByPage(record);
    }

    /**
     * 根据参数查询用户信息
     * @param record
     * @author WX
     * @date 2020-06-15 15:53:23
     */
    @Override
    public SysUser selectOneByParam(SysUser record) {
        return sysUserMapper.selectOneByParam(record);
    }

    /**
     * 根据用户名查询用户信息，shiro登录专用，里边涉及到级联查询，其余场景请勿使用
     * @param record
     * @author WX
     * @date 2020-06-15 15:53:23
     */
    @Override
    public SysUser selectByLogin(SysUser record) {
        return sysUserMapper.selectByLogin(record);
    }

    /**
     * 根据角色表的参数字段查询用户信息，比如：根据角色ID、根据角色类型、根据角色code等，不涉及groupBy
     * @param map
     * @author WX
     * @date 2020-08-05 15:56:43
     */
    @Override
    public List<SysUser> selectByRoleParam(Map<String, Object> map) {
        return sysUserMapper.selectByRoleParam(map);
    }

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-02-05 10:46:36
     */
    @Override
    public Integer batchInsert(List<SysUser> list) {
        return sysUserMapper.batchInsert(list);
    }

    /**
     * 批量更新已注册状态根据ID
     * @param map 参数+SysUserIds列表
     * @author WX
     * @date 2020-07-30 17:54:19
     */
    @Override
    public Integer BatchUpdate(Map<String, Object> map) throws Exception {
        return sysUserMapper.BatchUpdate(map);
    }


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 15:41:23
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return sysUserMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 15:47:07
     */
    @Override
    public List<SysUser> BatchSelect(Map<String, Object> map) {
        return sysUserMapper.BatchSelect(map);
    }

    /**
     * 新增系统用户
     * @param record
     * @param permId
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    @Override
    public Integer save(SysUser record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入系统用户失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_USER, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 批量新增系统用户
     * @param list
     * @param permId
     * @param req
     * @author WX
     * @date 2021-02-05 13:50:06
     */
    @Override
    public Integer batchSave(List<SysUser> list, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        //  批量新增系统用户
        Integer count = this.batchInsert(list);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_USER, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, list);
        Assert.isTrue(count==list.size() && b,"DB_SQL_INSERT_ERROR");
        return count;
    }

    /**
     * 新增系统用户和角色
     * @param personNum
     * @param personName
     * @param sysRoleIds
     * @param selectedRegister
     * @param permId
     * @author WX
     * @date 2020-07-29 16:34:02
     */
    @Override
    public Integer saveSysUserAndRole(String personNum, String personName, String sysRoleIds, String selectedRegister, String permId, HttpServletRequest req) throws Exception {
        // 插入系统用户信息
        SysUser record = new SysUser();
        record.setPersonName(personName);
        record.setPersonNum(personNum);
        record.setHeadImgUrl(customProperties.getProperty("file.default.headImg.path"));
        Integer save = this.save(record, permId, req);
        // 批量插入角色
        sysUserRoleService.batchSaveBySysUserId(record.getId(), sysRoleIds, permId, req);
        // 注册账号
        if("1".equals(selectedRegister)){
            this.toRegister(record.getId(), permId, req);
        }
        return save;
    }

    /**
     * 批量保存
     * @param list   系统用户list
     * @param sysRoleIds 角色ids
     * @param selectedRegister 是否自动注册账户，是为1，否为0
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param req
     * @author WX
     * @date 2021-02-05 10:53:29
     */
    @Override
    public Integer batchSaveSysUsersAndRole(List<SysUser> list, String sysRoleIds, String selectedRegister, String permId, HttpServletRequest req) throws Exception {
        // 批量插入系统用户信息
        Integer count = this.batchSave(list, permId, req);
        // 批量插入角色
        String sysUserIds = list.stream().map(SysUser::getId).collect(Collectors.joining(","));
        sysUserRoleService.batchSaveBySysUserIds(sysUserIds, sysRoleIds, permId, req);
        // 注册账号
        if("1".equals(selectedRegister)){
            this.toBatchRegister(sysUserIds, permId, req);
        }
        return count;
    }


    /**
     * 根据ID更新系统用户
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    @Override
    public Integer update(SysUser record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        SysUser su = this.selectByPrimaryKey(record.getId());
        Assert.notNull(su,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新系统用户失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_USER, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(su), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除系统用户
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 1.注销步骤内的移除账户
        BasicAuth basicAuth = new BasicAuth();
        basicAuth.setSysUserId(rowId);
        basicAuth = basicAuthService.selectOneByParam(basicAuth);
        if(basicAuth!=null) basicAuthService.doCancelAccount(basicAuth, permId, req);

        // 2.删除用户
        SysUser record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_USER, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");

        // 3.根据用户id 删除用户-角色关联信息
        sysUserRoleService.batchDeleteByMultiParam("sysUserId", rowId, permId, userInfo, false);

        // 4.根据用户id 删除用户-快捷菜单关联信息
        shortcutMenuService.batchDeleteByMultiParam("sysUserId", rowId, permId, userInfo, false);
        return count;
    }

    /**
     * 根据IDs批量删除系统用户
     * @param sysUserIds 用户ID，对应用户信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId     权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-30 20:08:16
     */
    @Override
    public Integer batchDelete(String sysUserIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 1.批量查询 跟据sysUserIds 用于构建账户信息，批量注销账户
        List<String> list = Arrays.asList(sysUserIds.split(","));
        Map<String, Object> map = new HashMap<>();
        map.put("sysUserId","sysUserId");
        map.put("list",list);
        List<BasicAuth> basicAuths = basicAuthService.BatchSelect(map);
        List<String> basicAuthIds = new ArrayList<>();
        List<String> redisKeys = new ArrayList<>();
        basicAuths.forEach(basicAuth -> {
            basicAuthIds.add(basicAuth.getId());
            redisKeys.add(RedisConst.INFO_USER(basicAuth.getUserName()));
        });
        if(CollectionUtils.isNotEmpty(basicAuths)) basicAuthService.doBatchCancelAccount(basicAuthIds,null,redisKeys,permId,req);

        // 2.查询要删除的数据是否存在
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SysUser> sysUsers = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(sysUsers) && sysUsers.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_USER, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, sysUsers, null);
        Assert.isTrue(count==sysUsers.size() && b,"DB_SQL_DELETE_ERROR");

        // 3.根据用户id 删除角色-用户关联信息
        sysUserRoleService.batchDeleteByMultiParam("sysUserIds", list, permId, userInfo, false);

        // 4.根据用户id 删除用户-快捷菜单关联信息
        shortcutMenuService.batchDeleteByMultiParam("sysUserIds", list, permId, userInfo, false);
        return count;
    }

    /**
     * 导入系统用户
     * @param permId    权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile excel文件
     * @param sysRoleIds 系统角色ID，对应角色列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param selectedRegister 是否自动注册账户，是为1，否为0
     * @author WX
     * @date 2021-02-04 16:42:23
     */
    @Override
    public Integer impSysUser(String permId, MultipartFile excelFile, String sysRoleIds, String selectedRegister, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        List<ArrayList<String>> ttcList = baseService.verifyImpExcel(excelFile, 3);
        // 将excel内数据取出组装成 List<SysUser>
        List<SysUser> list = new ArrayList<>();
        for (int i=2;i<ttcList.size();i++) {								//从第三行开始取
            ArrayList<String> row = ttcList.get(i);							//获取当前行
            if(row==null){continue;}//判断是否为空
            if(StringUtils.isBlank(row.get(0))||StringUtils.isBlank(row.get(5))){
                throw new FileImportException("当前sheet内存在无效数据，请填写完整或清除后重新上传！参考有效行(空行除外)：第"+(i+1)+"行");
            }
            SysUser sysUser = new SysUser();
            sysUser.setId(UuidUtil.get32UUID());
            sysUser.setPersonName(0<row.size()&&StringUtils.isNotBlank(row.get(0))?row.get(0):null);
            sysUser.setNickname(1<row.size()&&StringUtils.isNotBlank(row.get(1))?row.get(1):null);
            sysUser.setSex(2<row.size()&&StringUtils.isNotBlank(row.get(2))?row.get(2):"0");
            sysUser.setAge(3<row.size()&&StringUtils.isNotBlank(row.get(3))?Integer.valueOf(row.get(3)):null);
            sysUser.setBirthday(4<row.size()&&StringUtils.isNotBlank(row.get(4))?DatePatterns.NORM_DATE_FORMAT.parse(row.get(4)):null);
            sysUser.setPersonNum(5<row.size()&&StringUtils.isNotBlank(row.get(5))?row.get(5):null);
            sysUser.setPosition(6<row.size()&&StringUtils.isNotBlank(row.get(6))?row.get(6):null);
            sysUser.setProfile(7<row.size()&&StringUtils.isNotBlank(row.get(7))?row.get(7):null);
            sysUser.setHeadImgUrl(customProperties.getProperty("file.default.headImg.path"));
            sysUser.setIsRegistered("0");
            sysUser.setCreateId(userInfo.getId());
            sysUser.setCreateBy(userInfo.getUserName());
            sysUser.setCreateWay("1");
            sysUser.setCreateTime(new Date());
            list.add(sysUser);
        }
        // 过滤excel内重复数据
        list = list.stream().filter(DistinctWrapper.byKey(SysUser::getPersonNum)).collect(Collectors.toList());
        return this.batchSaveSysUsersAndRole(list, sysRoleIds, selectedRegister, permId, req);
    }

    /**
     * 根据SysUserId插入BasicAuth账号
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-29 14:55:07
     */
    @Override
    public Integer toRegister(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 查询该用户是否已经注册
        SysUser sysUser = this.selectByPrimaryKey(rowId);
        Assert.isTrue("0".equals(sysUser.getIsRegistered()), "REG_USER_EXISTS_ERROR");
        // 根据personNum 生成账号、密码
        BasicAuth basicAuth = this.createBasicAuth(sysUser);
        // 插入
        Integer save = basicAuthService.save(basicAuth, permId, req);
        // 更新该系统用户注册状态
        sysUser = new SysUser();
        sysUser.setId(rowId);
        sysUser.setIsRegistered("1");
        this.update(sysUser, permId, req);
        return save;
    }

    private BasicAuth createBasicAuth(SysUser sysUser){
        BasicAuth basicAuth = new BasicAuth();
        // 这里生成ID是因为要适配批量插入场景，单个保存save方法时内部还会再生成一次，被替换成新的ID
        basicAuth.setId(UuidUtil.get32UUID());
        basicAuth.setSysUserId(sysUser.getId());
        basicAuth.setUserName(sysUser.getPersonNum());
        basicAuth.setPassWord(CredentialsUtil.MD5Pwd(sysUser.getPersonNum(), basicAuth.getUserName(), SecretKeyConst.MD5_HASH_ITERATIONS));
        basicAuth.setSalt(basicAuth.getUserName());
        basicAuth.setPersonNum(sysUser.getPersonNum());
        return basicAuth;
    }

    /**
     * 根据SysUserId插入BasicAuth账号
     * @param sysUserIds 用户ID，对应用户信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId     权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-30 18:01:46
     */
    @Override
    public Integer toBatchRegister(String sysUserIds, String permId, HttpServletRequest req) throws Exception {
        List<String> list = Arrays.asList(sysUserIds.split(","));
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SysUser> sysUsers = this.BatchSelect(map);
        // stream 过滤 等于0的  生成账户List
        List<BasicAuth> basicAuths = sysUsers.stream().filter(sysUser -> "0".equals(sysUser.getIsRegistered()))
                .map(sysUser -> this.createBasicAuth(sysUser))
                .collect(Collectors.toList());
        // 不相等说明存在已注册用户
        Assert.isTrue(basicAuths.size()==sysUsers.size(), "REG_USER_LIST_EXISTS_ERROR");
        // 批量插入账户
        basicAuthService.batchSave(basicAuths, permId, req);
        // 批量更新用户已注册状态
        map = new HashMap<>();
        map.put("isRegistered","1");
        map.put("list",list);
        this.BatchUpdate(map);
        return null;
    }





    /**
     * 根据ID更新用户头像
     * @param imgFile
     * @param rowId
     * @param permId
     * @param req
     * @author WX
     * @date 2020-07-20 12:30:03
     */
    @Override
    public Integer uploadHeadImgById(MultipartFile imgFile, String rowId, String permId, HttpServletRequest req) throws Exception {
        String headImgPath = customProperties.getProperty("file.uploaded.headImg.path");
        String uploadPath = fileUploadService.uploadImg(imgFile, headImgPath, rowId);
        SysUser sysUser = new SysUser();
        sysUser.setId(rowId);
        sysUser.setHeadImgUrl(uploadPath);
        return this.update(sysUser, permId, req);
    }

    /**
     * 根据ID查询系统用户基本资料，包括角色，部门，连表信息
     * @param rowId
     * @param req
     * @author WX
     * @date 2020-07-21 17:45:20
     */
    @Override
    public SysUser findbasicInfoById(String rowId, HttpServletRequest req) {
        SysUser sysUser = this.selectByPrimaryKey(rowId);

        // 角色可能有多个，所以没一起连表查
        List<SysRole> sysRoles = sysUserRoleService.selectRolesByUserId(rowId);
        for (SysRole sysRole : sysRoles){
            if(StringUtils.isBlank(sysUser.getSysRoleNames())){
                sysUser.setSysRoleNames(sysRole.getRoleName());
            }else {
                sysUser.setSysRoleNames(sysUser.getSysRoleNames()+","+sysRole.getRoleName());
            }
        }

        // TODO 查询部门，

        return sysUser;
    }

}
