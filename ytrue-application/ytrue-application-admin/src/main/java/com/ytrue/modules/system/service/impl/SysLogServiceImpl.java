package com.ytrue.modules.system.service.impl;

import com.ytrue.common.base.BaseServiceImpl;
import com.ytrue.modules.system.dao.SysLogDao;
import com.ytrue.modules.system.model.po.SysLog;
import com.ytrue.modules.system.service.ISysLogService;
import org.springframework.stereotype.Service;

/**
 * @author ytrue
 * @description: SysLogServiceImpl
 * @date 2022/12/9 10:21
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogDao, SysLog> implements ISysLogService {
}
