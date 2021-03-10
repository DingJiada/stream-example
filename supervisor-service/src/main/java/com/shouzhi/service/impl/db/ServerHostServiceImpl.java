package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.constants.DatePatterns;
import com.shouzhi.basic.utils.WeeksUtil;
import com.shouzhi.mapper.ServerHostMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchSemester;
import com.shouzhi.pojo.db.ServerHost;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.IServerHostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 服务主机表业务层接口实现类
 * @author WX
 * @date 2020-11-25 09:42:08
 */
@Service("serverHostService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class ServerHostServiceImpl implements IServerHostService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServerHostMapper serverHostMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return serverHostMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(ServerHost record) throws Exception {
        return serverHostMapper.insert(record);
    }

    @Override
    public Integer insertSelective(ServerHost record) throws Exception {
        return serverHostMapper.insertSelective(record);
    }

    @Override
    public ServerHost selectByPrimaryKey(String id) {
        return serverHostMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(ServerHost record) throws Exception {
        return serverHostMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(ServerHost record) throws Exception {
        return serverHostMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询
     * @param record
     * @author WX
     * @date 2020-11-25 09:49:03
     */
    @Override
    public ServerHost selectOneByParam(ServerHost record) {
        return serverHostMapper.selectOneByParam(record);
    }

    /**
     * 查询总数
     * @author WX
     * @date 2021-02-03 17:24:16
     */
    @Override
    public Integer selectCount() {
        return serverHostMapper.selectCount();
    }

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2021-02-03 17:31:07
     */
    @Override
    public List<ServerHost> queryListByPage(Map<String, Object> map) {
        return serverHostMapper.queryListByPage(map);
    }

    /**
     * 根据ID更新
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2021-02-03 17:34:26
     */
    @Override
    public Integer update(ServerHost record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        ServerHost originalRecord = this.selectByPrimaryKey(record.getId());
        Assert.notNull(originalRecord,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新服务器主机失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SERVER_HOST, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(originalRecord), JSON.toJSONString(record));
        return count;
    }
}
