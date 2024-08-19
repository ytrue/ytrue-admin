using Mapster;
using SqlSugar;
using Ytrue.Infrastructures.Bases;
using Ytrue.Infrastructures.Response;
using Ytrue.Infrastructures.Utilities;
using Ytrue.Models.Bases;
using Ytrue.Models.Entities.System;
using Ytrue.Models.Queries.System;
using Ytrue.Models.Reqs.System;
using Ytrue.Models.Resps.System;
using Ytrue.Repositories.Interfaces.System;
using Ytrue.Services.Interfaces.System;

namespace Ytrue.Services.Implementations.System;

/// <summary>
/// 系统岗位服务实现类，提供与系统作业相关的业务逻辑操作。
/// </summary>
public class SysJobService : BaseService<SysJob>, ISysJobService
{
    private readonly ISysUserJobRepository _sysUserJobRepository;


    public SysJobService(IBaseRepository<SysJob> repository, ISysUserJobRepository sysUserJobRepository) :
        base(repository)
    {
        _sysUserJobRepository = sysUserJobRepository;
    }

    /// <summary>
    /// 添加新的系统岗位。
    /// </summary>
    /// <param name="requestParam">包含系统岗位信息的请求参数</param>
    /// <returns>操作结果的 Task 对象</returns>
    /// <exception cref="NotImplementedException">方法尚未实现</exception>
    public async Task AddSysJobAsync(SysJobAddReq requestParam)
    {
        var job = await GetByConditionAsync(entity => entity.JobName.Equals(requestParam.JobName));
        AssertUtil.IsNull(job, ServerResponseInfo.Error("岗位已存在"));

        var sysJob = requestParam.Adapt<SysJob>();
        await AddAsync(sysJob);
    }

    /// <summary>
    /// 更新现有的系统岗位信息。
    /// </summary>
    /// <param name="requestParam">包含更新信息的请求参数</param>
    /// <returns>操作结果的 Task 对象</returns>
    /// <exception cref="NotImplementedException">方法尚未实现</exception>
    public async Task UpdateSysJobAsync(SysJobUpdateReq requestParam)
    {
        var data = await GetByIdAsync(requestParam.Id);
        AssertUtil.NotNull(data, ServerResponseInfo.Error("数据不存在"));
        // 还要校验一下，就id不等于我这个的
        var b = await IsAnyByConditionAsync(entity =>
            entity.JobName.Equals(requestParam.JobName) && entity.Id != requestParam.Id);

        AssertUtil.IsFalse(b, ServerResponseInfo.Error("岗位已存在"));

        // bean copy
        requestParam.Adapt(data);
        await UpdateByIdAsync(data!);
    }

    /// <summary>
    /// 根据系统岗位 ID 列表删除系统岗位。
    /// </summary>
    /// <param name="ids">系统岗位的 ID 列表</param>
    /// <returns>操作结果的 Task 对象</returns>
    /// <exception cref="NotImplementedException">方法尚未实现</exception>
    public async Task RemoveBySysJobIdsAsync(List<long> ids)
    {
        // 校验集合
        AssertUtil.CollectionIsNotEmpty(ids, ServerResponseInfo.Error("参数错误"));
        // 只要存在一个 就是无法删除的
        var sysUserJob = await _sysUserJobRepository.SelectOneByConditionAsync(item => ids.Contains(item.JobId));
        AssertUtil.IsNull(sysUserJob, ServerResponseInfo.Error("存在用户关联，请解除后再试"));

        // 事务处理
        try
        {
            await Ado.BeginTranAsync();

            await RemoveByIdsAsync(ids.ToArray());

            await Ado.CommitTranAsync();
        }
        catch (Exception)
        {
            await Ado.RollbackTranAsync();
            throw;
        }
    }

    /// <summary>
    /// 根据系统岗位 ID 获取系统岗位信息。
    /// </summary>
    /// <param name="id">系统岗位的 ID</param>
    /// <returns>操作结果的 Task 对象，返回包含系统岗位信息的响应对象</returns>
    /// <exception cref="NotImplementedException">方法尚未实现</exception>
    public async Task<SysJobIdResp> GetBySysJobIdAsync(long id)
    {
        var data = await GetByIdAsync(id);
        AssertUtil.NotNull(data, ServerResponseInfo.Error("数据不存在"));
        return data.Adapt<SysJobIdResp>();
    }

    /// <summary>
    /// 根据分页查询条件获取系统岗位列表。
    /// </summary>
    /// <param name="query">分页查询条件</param>
    /// <returns>操作结果的 Task 对象，返回分页结果</returns>
    /// <exception cref="NotImplementedException">方法尚未实现</exception>
    public async Task<PageResult<SysJobListResp>> ListBySysJobPageQueryAsync(SysJobPageQuery query)
    {
        // REF和OUT不支持异步,想要真的异步这是最优解
        RefAsync<int> totalCount = 0;

        var jobList = await BaseRepository
            .GetSugarQueryable()
            .Where(query.GetQueryExpression())
            .OrderBy(job => SqlFunc.Desc(job.JobSort))
            .OrderBy(job => SqlFunc.Desc(job.Id))
            .ToPageListAsync(query.PageIndex, query.PageSize, totalCount);

        // bean copy
        var sysJobListRespList = jobList.Adapt<List<SysJobListResp>>();

        var rest = new PageResult<SysJobListResp>
        {
            Records = sysJobListRespList,
            Current = query.PageIndex,
            Size = query.PageSize,
            Total = totalCount
        };
        return rest;
    }
}