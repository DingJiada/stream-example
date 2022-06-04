package com.shouzhi.service.impl.db;

import com.shouzhi.basic.utils.IpUtil;
import com.shouzhi.basic.utils.UserAgentUtil;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.LogLoginMapper;
import com.shouzhi.pojo.db.LogLogin;
import com.shouzhi.service.interf.db.ILogLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日志登录表业务层接口实现类
 * @author WX
 * @date 2020-06-08 10:32:00
 */
@Service("logLoginService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class LogLoginServiceImpl implements ILogLoginService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogLoginMapper logLoginMapper;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return logLoginMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(LogLogin record) throws Exception {
        return logLoginMapper.insert(record);
    }

    @Override
    public Integer insertSelective(LogLogin record) throws Exception {
        return logLoginMapper.insertSelective(record);
    }

    @Override
    public LogLogin selectByPrimaryKey(String id) {
        return logLoginMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(LogLogin record) throws Exception {
        return logLoginMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(LogLogin record) throws Exception {
        return logLoginMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询日志登录列表
     * @param map
     * @author WX
     * @date 2020-06-08 10:32:00
     */
    @Override
    public List<LogLogin> queryListByPage(Map<String, Object> map) {
        return logLoginMapper.queryListByPage(map);
    }

    /**
     * 新增日志登录记录
     * @param loginState 登录状态，1成功，0失败
     * @param loginCode  状态码，具体的状态描述
     * @param createId   创建人id
     * @param createBy   创建人账号
     * @param req
     * @author WX
     * @date 2020-06-29 14:51:20
     */
    @Override
    public Integer save(String loginState, String loginCode, String createId, String createBy, HttpServletRequest req) throws Exception {
        String userAgent = UserAgentUtil.getUserAgent(req);
        LogLogin logLogin = new LogLogin();
        logLogin.setId(UuidUtil.get32UUID());
        logLogin.setLoginIp(IpUtil.getIp(req));
        logLogin.setLoginTime(new Date());
        logLogin.setLoginState(loginState);
        logLogin.setLoginCode(loginCode);
        logLogin.setLoginUa(userAgent);
        logLogin.setOsName(UserAgentUtil.getOsName(userAgent));
        logLogin.setOsGroupName(UserAgentUtil.getOsGroupName(userAgent));
        logLogin.setOsDeviceTypeName(UserAgentUtil.getOsDeviceTypeName(userAgent));
        logLogin.setOsManufacturerName(UserAgentUtil.getOsManufacturerName(userAgent));
//        logLogin.setOsVersion(UserAgentUtil.getOsVersion(userAgent));
        logLogin.setBrowserName(UserAgentUtil.getBrowserName(userAgent));
        logLogin.setBrowserGroupName(UserAgentUtil.getBrowserGroupName(userAgent));
        logLogin.setBrowserTypeName(UserAgentUtil.getBrowserTypeName(userAgent));
        logLogin.setBrowserManufacturerName(UserAgentUtil.getBrowserManufacturerName(userAgent));
        logLogin.setBrowserRenderingEngine(UserAgentUtil.getBrowserRenderingEngine(userAgent));
//        logLogin.setBrowserVersion(UserAgentUtil.getBrowserVersion(userAgent));
        logLogin.setCreateId(createId);
        logLogin.setCreateBy(createBy);
        return this.insertSelective(logLogin);
    }
}
