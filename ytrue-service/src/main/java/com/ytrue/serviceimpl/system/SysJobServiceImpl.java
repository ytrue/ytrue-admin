package com.ytrue.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysJob;
import com.ytrue.bean.dataobject.system.SysUserJob;
import com.ytrue.bean.query.system.SysJobPageQuery;
import com.ytrue.bean.req.system.SysJobAddReq;
import com.ytrue.bean.req.system.SysJobUpdateReq;
import com.ytrue.bean.resp.system.SysJobIdResp;
import com.ytrue.bean.resp.system.SysJobListResp;
import com.ytrue.infra.core.response.ResponseInfoEnum;
import com.ytrue.infra.core.response.ServerResponseInfo;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.core.util.BeanUtils;
import com.ytrue.infra.db.base.BaseServiceImpl;
import com.ytrue.dao.system.SysJobDao;
import com.ytrue.dao.system.SysUserJobDao;
import com.ytrue.service.system.SysJobService;
import com.ytrue.infra.db.query.util.QueryHelp;
import lombok.RequiredArgsConstructor;
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
    public IPage<SysJobListResp> listBySysJobPageQuery(SysJobPageQuery queryParam) {

        LambdaQueryWrapper<SysJob> queryWrapper = QueryHelp.<SysJob>lambdaQueryWrapperBuilder(queryParam).orderByAsc(SysJob::getJobSort).orderByDesc(SysJob::getId);

        return this.page(queryParam.page(), queryWrapper).convert(sysJob -> BeanUtils.copyProperties(sysJob, SysJobListResp::new));
    }

    @Override
    public List<SysJob> listBySysUserId(Long userId) {
        return baseMapper.selectBySysUserId(userId);
    }

    @Override
    public void addSysJob(SysJobAddReq requestParam) {

        SysJob job = this.lambdaQuery().eq(SysJob::getJobName, requestParam.getJobName()).one();
        AssertUtil.isNull(job, ServerResponseInfo.error("岗位已存在"));

        SysJob sysJob = BeanUtils.copyProperties(requestParam, SysJob::new);
        this.save(sysJob);
    }

    @Override
    public void updateSysJob(SysJobUpdateReq requestParam) {
        SysJob data = this.getById(requestParam.getId());
        AssertUtil.notNull(data, ResponseInfoEnum.ILLEGAL_OPERATION);

        SysJob job = this.lambdaQuery()
                .eq(SysJob::getJobName, requestParam.getJobName())
                .ne(SysJob::getId, requestParam.getId()).one();
        AssertUtil.isNull(job, ServerResponseInfo.error("岗位已存在"));

        BeanUtils.copyProperties(requestParam, job);

        this.updateById(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBySysJobIds(List<Long> ids) {
        // 校验集合
        AssertUtil.collectionIsNotEmpty(ids, ResponseInfoEnum.ILLEGAL_OPERATION);

        // 只要存在一个 就是无法删除的
        SysUserJob sysUserJob = sysUserJobDao.selectOne(Wrappers.<SysUserJob>lambdaQuery().in(SysUserJob::getJobId, ids));
        AssertUtil.isNull(sysUserJob, ServerResponseInfo.error("存在用户关联，请解除后再试"));

        // 删除
        this.removeBatchByIds(ids);
    }

    @Override
    public SysJobIdResp getBySysJobId(Long id) {
        SysJob data = this.getById(id);
        AssertUtil.notNull(data, ResponseInfoEnum.DATA_NOT_FOUND);

        return BeanUtils.copyProperties(data, SysJobIdResp::new);
    }
}
