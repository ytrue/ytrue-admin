package com.ytrue.modules.system.service.impl;

import com.ytrue.db.base.BaseServiceImpl;
import com.ytrue.modules.system.dao.SysJobDao;
import com.ytrue.modules.system.model.po.SysJob;
import com.ytrue.modules.system.service.ISysJobService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ytrue
 * @description: SysJobServiceiMPL
 * @date 2022/12/7 10:56
 */
@Service
@AllArgsConstructor
public class SysJobServiceImpl extends BaseServiceImpl<SysJobDao, SysJob> implements ISysJobService {

    private final SysJobDao sysJobDao;

    @Override
    public List<SysJob> listByUserId(Long userId) {
        return sysJobDao.listByUserId(userId);
    }
}
