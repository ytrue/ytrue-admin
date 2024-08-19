using Ytrue.Infrastructures.Bases;
using Ytrue.Models.Bases;
using Ytrue.Models.Entities.System;
using Ytrue.Models.Queries.System;
using Ytrue.Models.Reqs.System;
using Ytrue.Models.Resps.System;

namespace Ytrue.Services.Interfaces.System;

public interface ISysJobService : IBaseService<SysJob>
{
    /// <summary>
    /// 新增岗位
    /// </summary>
    /// <param name="requestParam"></param>
    Task AddSysJobAsync(SysJobAddReq requestParam);

    /// <summary>
    /// 更新
    /// </summary>
    /// <param name="requestParam"></param>
    /// <returns></returns>
    Task UpdateSysJobAsync(SysJobUpdateReq requestParam);


    /// <summary>
    /// 批量删除
    /// </summary>
    /// <param name="ids"></param>
    /// <returns></returns>
    Task RemoveBySysJobIdsAsync(List<long> ids);

    /// <summary>
    /// 根据id获取
    /// </summary>
    /// <param name="id"></param>
    /// <typeparam name="T"></typeparam>
    /// <returns></returns>
    Task<SysJobIdResp> GetBySysJobIdAsync(long id);

    /// <summary>
    /// 列表查询
    /// </summary>
    /// <param name="query"></param>
    /// <returns></returns>
    Task<PageResult<SysJobListResp>> ListBySysJobPageQueryAsync(SysJobPageQuery query);
}