package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SecurityQuestionSet;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 密保问题设置表业务层接口
 * @author WX
 * @date 2020-07-15 17:48:11
 */
public interface ISecurityQuestionSetService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SecurityQuestionSet record) throws Exception;

    Integer insertSelective(SecurityQuestionSet record) throws Exception;

    SecurityQuestionSet selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SecurityQuestionSet record) throws Exception;

    Integer updateByPrimaryKey(SecurityQuestionSet record) throws Exception;

    /**
     * 根据参数查询密保问题设置列表
     * @param record
     * @author WX
     * @date 2020-07-15 17:42:15
     */
    List<SecurityQuestionSet> queryListByPage(SecurityQuestionSet record);

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 09:20:15
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 09:26:37
     */
    List<SecurityQuestionSet> BatchSelect(Map<String, Object> map);

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-11-23 09:43:19
     */
    Integer batchInsert(List<SecurityQuestionSet> list);

    /**
     * 新增密保问题设置
     * @param securityQuestionSets
     * @param permId
     * @author WX
     * @date 2020-07-16 10:34:06
     */
    Integer save(String basicAuthId, String permId, List<SecurityQuestionSet> securityQuestionSets, HttpServletRequest req) throws Exception;

    /**
     * 根据多参数批量删除密保问题
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-07-16 10:54:20
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception;

}
