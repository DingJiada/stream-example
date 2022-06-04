package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.SysStaticParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统静态参数设置表业务层接口
 * @author WX
 * @date 2020-11-04 15:43:16
 */
public interface ISysStaticParamService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysStaticParam record) throws Exception;

    Integer insertSelective(SysStaticParam record) throws Exception;

    SysStaticParam selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysStaticParam record) throws Exception;

    Integer updateByPrimaryKey(SysStaticParam record) throws Exception;

    /**
     * 根据参数查询系统静态参数设置列表
     * @param record
     * @author WX
     * @date 2020-11-04 16:02:10
     */
    List<SysStaticParam> queryListByPage(SysStaticParam record);

    /**
     * 批量更新
     * @param records
     * @author WX
     * @date 2020-11-05 09:58:26
     */
    Integer batchUpdate(List<SysStaticParam> records) throws Exception;

    /**
     * 上传图片
     * @param imgFiles 图片文件
     * @param imgType 图片类型，1：登录轮播图、2：注册轮播图、3：网站icon、4：前台页眉左上角logo、5：后台页眉左上角logo
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-24 14:30:27
     */
    Integer uploadImgs(MultipartFile[] imgFiles, String imgType, String isAppend, String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 删除图片
     * @param rowId 被操作记录行ID
     * @param imgRelPath 图片相对路径
     * @author WX
     * @date 2020-12-18 10:15:23
     */
    Integer delImg(String rowId, String imgRelPath, HttpServletRequest req) throws Exception;


    /**
     * 查询登录页静态参数
     * @param pageType 页面类型，如：登录页、注册页、门户首页等等
     * @author WX
     * @date 2020-11-24 17:19:06
     */
    Map<String, Object> findPageParam(String pageType);
}
