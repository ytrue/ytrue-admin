package com.ytrue.serviceimpl.system;

import com.ytrue.infra.db.base.BaseServiceImpl;
import com.ytrue.infra.db.dao.system.SysJobDao;
import com.ytrue.bean.dataobject.system.SysJob;
import com.ytrue.service.system.SysJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ytrue
 * @description: SysJobServiceiMPL
 * @date 2022/12/7 10:56
 */
@Service
@RequiredArgsConstructor
public class SysJobServiceImpl extends BaseServiceImpl<SysJobDao, SysJob> implements SysJobService {

    @Override
    public List<SysJob> listByUserId(Long userId) {
        return baseMapper.listByUserId(userId);
    }
}
