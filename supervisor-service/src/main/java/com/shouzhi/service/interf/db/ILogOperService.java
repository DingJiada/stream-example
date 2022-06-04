package com.shouzhi.service.interf.db;


import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.LogOper;
import com.shouzhi.pojo.db.LogOperDetail;

import java.util.List;

/**
 * 日志操作表业务层接口
 * @author WX
 * @date 2020-06-09 14:48:40
 */
public interface ILogOperService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(LogOper record) throws Exception;

    Integer insertSelective(LogOper record) throws Exception;

    LogOper selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(LogOper record) throws Exception;

    Integer updateByPrimaryKey(LogOper record) throws Exception;

    /**
     * 根据参数查询日志操作列表
     * @param record
     * @author WX
     * @date 2020-06-09 14:35:23
     */
    List<LogOper> queryListByPage(LogOper record);

    /**
     * 插入操作日志和操作日志详情
     * 为什么不直接接收两个对象？因为那样每个插入日志的service都会有new两个对象并set字段的代码，臃肿
     * @author WX
     * @date 2020-06-11 09:28:51
     */
    boolean insertLogOperAndDetail(String tabName, String operType, String permId, String isCascade, String cascadeId, BasicAuth userInfo, String rowId, String oldVal, String newVal) throws Exception;

    /**
     * 批量插入操作日志和操作日志详情
     * @author WX
     * @date 2020-11-20 14:47:13
     */
    boolean batchInsertLogOperAndDetail(String tabName, String operType, String permId, String isCascade, String cascadeId, BasicAuth userInfo, String rowIdFiledName, List<?> oldVal, List<?> newVal) throws Exception;

}
