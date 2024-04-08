package com.ytrue.serviceimpl.system;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysJob;
import com.ytrue.bean.dataobject.system.SysUserJob;
import com.ytrue.bean.req.system.SysJobAddReq;
import com.ytrue.bean.req.system.SysJobUpdateReq;
import com.ytrue.infra.core.response.ResponseCodeEnum;
import com.ytrue.infra.core.response.ServerResponseCode;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.db.base.BaseServiceImpl;
import com.ytrue.infra.db.dao.system.SysJobDao;
import com.ytrue.infra.db.dao.system.SysUserJobDao;
import com.ytrue.service.system.SysJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ytrue
 * @description: SysJobServiceiMPL
 * @date 2022/12/7 10:56
 */
@Service
@RequiredArgsConstructor
public class SysJobServiceImpl extends BaseServiceImpl<SysJobDao, SysJob> implements SysJobService {


    private final SysUserJobDao sysUserJobDao;

    @Override
    public List<SysJob> listByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public void addSysJob(SysJobAddReq requestParam) {

        SysJob job = this.lambdaQuery().eq(SysJob::getJobName, requestParam.getJobName()).one();
        AssertUtil.isNull(job, ServerResponseCode.error("岗位已存在"));

        SysJob sysJob = new SysJob();
        BeanUtils.copyProperties(requestParam, sysJob);

        this.save(sysJob);
    }

    @Override
    public void updateSysJob(SysJobUpdateReq requestParam) {
        SysJob job = this.lambdaQuery().eq(SysJob::getJobName, requestParam.getJobName()).ne(SysJob::getId, requestParam.getId()).one();

        AssertUtil.isNull(job, ServerResponseCode.error("岗位已存在"));

        BeanUtils.copyProperties(requestParam, job);

        this.updateById(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatchSysJobByIds(List<Long> ids) {
        // 校验集合
        AssertUtil.collectionIsNotEmpty(ids, ResponseCodeEnum.ILLEGAL_OPERATION);

        // 只要存在一个 就是无法删除的
        SysUserJob sysUserJob = sysUserJobDao.selectOne(Wrappers.<SysUserJob>lambdaQuery().in(SysUserJob::getJobId, ids));
        AssertUtil.isNull(sysUserJob, ServerResponseCode.error("存在用户关联，请解除后再试"));

        // 删除
        this.removeBatchByIds(ids);
    }

    @Override
    public SysJob getSysJobById(Long id) {
        SysJob data = this.getById(id);
        AssertUtil.notNull(data, ResponseCodeEnum.DATA_NOT_FOUND);
        return data;
    }
}
