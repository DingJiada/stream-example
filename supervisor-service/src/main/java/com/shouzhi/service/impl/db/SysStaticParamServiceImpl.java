package com.shouzhi.service.impl.db;

import com.shouzhi.basic.constants.FileTypeConstants;
import com.shouzhi.mapper.SysStaticParamMapper;
import com.shouzhi.pojo.db.ServerHost;
import com.shouzhi.pojo.db.SysStaticParam;
import com.shouzhi.pojo.vo.SysPermissionVo;
import com.shouzhi.service.common.FileUploadService;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.IServerHostService;
import com.shouzhi.service.interf.db.ISysStaticParamService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统静态参数设置表业务层接口实现类
 * @author WX
 * @date 2020-11-04 15:46:14
 */
@Service("sysStaticParamService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SysStaticParamServiceImpl implements ISysStaticParamService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysStaticParamMapper sysStaticParamMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    private IServerHostService serverHostService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired  //@Qualifier("customProperties")
    private Properties customProperties;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return sysStaticParamMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SysStaticParam record) throws Exception {
        return sysStaticParamMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SysStaticParam record) throws Exception {
        return sysStaticParamMapper.insertSelective(record);
    }

    @Override
    public SysStaticParam selectByPrimaryKey(String id) {
        return sysStaticParamMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SysStaticParam record) throws Exception {
        return sysStaticParamMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SysStaticParam record) throws Exception {
        return sysStaticParamMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询系统静态参数设置列表
     * @param record
     * @author WX
     * @date 2020-11-04 16:02:10
     */
    @Override
    public List<SysStaticParam> queryListByPage(SysStaticParam record) {
        return sysStaticParamMapper.queryListByPage(record);
    }

    /**
     * 批量更新
     * @param records
     * @author WX
     * @date 2020-11-05 09:58:26
     */
    @Override
    public Integer batchUpdate(List<SysStaticParam> records) throws Exception {
        // 此业务的数据记录并不多，都是些固定静态记录，直接采用for循环更新
        if (CollectionUtils.isEmpty(records)) {
            throw new IllegalArgumentException("更新记录为空");
        }
        Integer count = 0;
        for (SysStaticParam record : records) {
            count += sysStaticParamMapper.updateByPrimaryKeySelective(record);
        }
        Assert.isTrue(count==records.size(),"更新失败，已更新记录数与待更新数不匹配！");
        return count;
    }

    /**
     * 上传图片
     * @param imgFiles 图片文件
     * @param imgType 图片类型，1：登录轮播图、2：注册轮播图、3：网站icon、4：前台页眉左上角logo、5：后台页眉左上角logo
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-24 14:30:27
     */
    @Override
    public Integer uploadImgs(MultipartFile[] imgFiles, String imgType, String isAppend, String rowId, String permId, HttpServletRequest req) throws Exception {
        SysStaticParam originalRecord = this.selectByPrimaryKey(rowId);
        Assert.notNull(originalRecord,"DB_SQL_ID_INVALID_ERROR");
        if(!"3".equals(originalRecord.getShowType())&&!"4".equals(originalRecord.getShowType()))
            throw new IllegalArgumentException("STATIC_PARAM_SHOW_TYPE_MISMATCH_ERROR");
        // imgType 图片类型，1：登录轮播图、2：注册轮播图、3：网站icon、4：前台页眉左上角logo、5：后台页眉左上角logo
        String typePath=null, customPath=null;
        switch (imgType){
            case "1":
                typePath = customProperties.getProperty("file.uploaded.bannerPic.path");
                customPath = customProperties.getProperty("file.uploaded.bannerPic.login.path");
                break;
            case "2":
                typePath = customProperties.getProperty("file.uploaded.bannerPic.path");
                customPath = customProperties.getProperty("file.uploaded.bannerPic.register.path");
                break;
            case "3":
                Assert.isTrue(imgFiles[0].getOriginalFilename().endsWith(FileTypeConstants.ICO), "FILE_UPLOAD_TYPE_ERROR");
            case "4":
            case "5":
                typePath = customProperties.getProperty("file.uploaded.logos.path");
                break;
            default:
                throw new IllegalArgumentException();
        }
        List<String> uploadPaths = fileUploadService.uploadImg(imgFiles, typePath, customPath);
        SysStaticParam sysStaticParam = new SysStaticParam();
        sysStaticParam.setId(rowId);
        if(StringUtils.isBlank(isAppend)){
            sysStaticParam.setParamVal(uploadPaths.stream().collect(Collectors.joining(",")));
        }else {
            Optional.ofNullable(originalRecord.getParamVal()).ifPresent(pv -> uploadPaths.addAll(Arrays.asList(pv.split(","))));
            sysStaticParam.setParamVal(uploadPaths.stream().collect(Collectors.joining(",")));
        }
        return this.updateByPrimaryKeySelective(sysStaticParam);
    }

    /**
     * 删除图片
     * @param rowId      被操作记录行ID
     * @param imgRelPath 图片相对路径
     * @param req
     * @author WX
     * @date 2020-12-18 10:15:23
     */
    @Override
    public Integer delImg(String rowId, String imgRelPath, HttpServletRequest req) throws Exception {
        SysStaticParam originalRecord = this.selectByPrimaryKey(rowId);
        Assert.notNull(originalRecord,"DB_SQL_ID_INVALID_ERROR");
        if(!"3".equals(originalRecord.getShowType())&&!"4".equals(originalRecord.getShowType()))
            throw new IllegalArgumentException("STATIC_PARAM_SHOW_TYPE_MISMATCH_ERROR");

        List<String> pathList = Arrays.stream(originalRecord.getParamVal().split(",")).collect(Collectors.toList());
        Assert.isTrue(pathList.remove(imgRelPath), "FILE_NOT_EXIST_ERROR");

        return this.updateByPrimaryKeySelective(new SysStaticParam(rowId,pathList.stream().collect(Collectors.joining(","))));
    }

    /**
     * 查询登录页静态参数
     * @param pageType 页面类型，如：登录页、注册页、门户首页等等
     * @author WX
     * @date 2020-11-24 17:19:06
     */
    @Override
    public Map<String, Object> findPageParam(String pageType) {
        Map<String, Object> resultMap = new HashMap<>();
        // 查询所有静态参数
        List<SysStaticParam> sysStaticParams = this.queryListByPage(new SysStaticParam());
        Map<String, SysStaticParam> staticParamMap = sysStaticParams.stream().collect(Collectors.toMap(SysStaticParam::getId, s -> s));
        // 查询静态资源服务器地址
        /*ServerHost serverHost = new ServerHost();
        serverHost.setHostType("3");
        serverHost = serverHostService.selectOneByParam(serverHost);
        String hostAddr = serverHost.getHostProtocol()+serverHost.getHostAddr()+serverHost.getHostPort();*/

        switch (pageType){
            case "1":
                // 门户首页 portalHomePage
                resultMap = new HashMap<>();
                // 前台页眉
                Map<String, Object> FGHeaderPage = new HashMap<>();
                FGHeaderPage.put("fgTopLeftLogo", staticParamMap.get("1_1_1").getParamVal());
                FGHeaderPage.put("fgTopLeftName", staticParamMap.get("1_1_2").getParamVal());
                FGHeaderPage.put("browserTabTitlePrefix", staticParamMap.get("1_1_3").getParamVal());
                // 前台页脚
                Map<String, Object> FGFooterPage = new HashMap<>();
                FGFooterPage.put("copyright", staticParamMap.get("1_2_1").getParamVal());
                FGFooterPage.put("ICPBei", staticParamMap.get("1_2_2").getParamVal());
                FGFooterPage.put("GongAnBei", staticParamMap.get("1_2_3").getParamVal());
                // 站点Icon
                Map<String, Object> siteIcon = new HashMap<>();
                siteIcon.put("siteIcon", staticParamMap.get("1_6_1").getParamVal());

                resultMap.put("FGHeaderPage", FGHeaderPage);
                resultMap.put("FGFooterPage", FGFooterPage);
                resultMap.put("globalIcon", siteIcon);

                break;
            case "2":
                // 登陆页面 loginPage
                resultMap = new HashMap<>();
                resultMap.put("bannerPics", staticParamMap.get("1_3_1").getParamVal());
                resultMap.put("loginBoxTitle", staticParamMap.get("1_3_2").getParamVal());
                resultMap.put("interval", staticParamMap.get("1_3_3").getParamVal());
                break;
            case "3":
                // 注册页面 registerPage
                resultMap = new HashMap<>();
                resultMap.put("bannerPics", staticParamMap.get("1_4_1").getParamVal());
                resultMap.put("registerBoxTitle", staticParamMap.get("1_4_2").getParamVal());
                resultMap.put("interval", staticParamMap.get("1_4_3").getParamVal());
                break;
            case "4":
                // 后台页眉 BGHeaderPage
                resultMap = new HashMap<>();
                resultMap.put("bgTopLeftLogo", staticParamMap.get("1_5_1").getParamVal());
                resultMap.put("bgTopLeftName", staticParamMap.get("1_5_2").getParamVal());
                resultMap.put("browserTabTitlePrefix", staticParamMap.get("1_5_3").getParamVal());
                break;
        }
        return resultMap;
    }
}
