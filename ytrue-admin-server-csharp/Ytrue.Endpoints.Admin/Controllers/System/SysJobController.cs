using Microsoft.AspNetCore.Mvc;
using Ytrue.Infrastructures.Interceptors.Filters.Operationlog;
using Ytrue.Infrastructures.Response;
using Ytrue.Infrastructures.Security;
using Ytrue.Models.Bases;
using Ytrue.Models.Queries.System;
using Ytrue.Models.Reqs.System;
using Ytrue.Models.Resps.System;
using Ytrue.Services.Interfaces.System;
using static System.Int32;

namespace Ytrue.Endpoints.Admin.Controllers.System;

/// <summary>
/// 岗位管理
/// </summary>
[ApiController]
[Route("sys/job")]
//[Authorize]
public class SysJobController : ControllerBase
{
    private readonly ISysJobService _sysJobService;


    public SysJobController(ISysJobService sysJobService)
    {
        _sysJobService = sysJobService;
    }


    /// <summary>
    /// 分页查询
    /// </summary>
    /// <param name="query"></param>
    /// <returns></returns>
    [HttpGet("page")]
    [OperateLog("岗位管理-列表")]
    [Permission("system:job:page")]
    public async Task<ServerResponseEntity<PageResult<SysJobListResp>>> ListBySysJobPageQueryAsync(
        [FromQuery] SysJobPageQuery query)
    {
        var data = await _sysJobService.ListBySysJobPageQueryAsync(query);
        return ServerResponseEntity<PageResult<SysJobListResp>>.SuccessResult(data);
    }


    /// <summary>
    /// 列表
    /// </summary>
    /// <param name="query"></param>
    /// <returns></returns>
    [HttpGet("list")]
    [Permission("system:job:list")]
    public async Task<ServerResponseEntity<List<SysJobListResp>>> ListAllAsync()
    {
        var query = new SysJobPageQuery
        {
            Status = true,
            PageIndex = 1,
            PageSize = MaxValue
        };
        var data = await _sysJobService.ListBySysJobPageQueryAsync(query);
        return ServerResponseEntity<List<SysJobListResp>>.SuccessResult(data.Records);
    }


    /// <summary>
    /// 保存
    /// </summary>
    /// <param name="requestParam"></param>
    /// <returns></returns>
    [HttpPost]
    [Permission("system:job:add")]
    public async Task<ServerResponseEntity<string>> AddSysJobAsync([FromBody] SysJobAddReq requestParam)
    {
        await _sysJobService.AddSysJobAsync(requestParam);
        return ServerResponseEntity<string>.SuccessResult();
    }


    /// <summary>
    /// 根据id获取
    /// </summary>
    /// <param name="id"></param>
    /// <returns></returns>
    [HttpGet("detail/{id:long}")]
    [Permission("system:job:detail")]
    public async Task<ServerResponseEntity<SysJobIdResp>> GetBySysJobIdAsync(long id)
    {
        var data = await _sysJobService.GetBySysJobIdAsync(id);
        return ServerResponseEntity<SysJobIdResp>.SuccessResult(data);
    }


    /// <summary>
    /// 更新
    /// </summary>
    /// <param name="requestParam"></param>
    /// <returns></returns>
    [HttpPut]
    [Permission("system:job:update")]
    public async Task<ServerResponseEntity<string>> UpdateSysJobAsync([FromBody] SysJobUpdateReq requestParam)
    {
        await _sysJobService.UpdateSysJobAsync(requestParam);
        return ServerResponseEntity<string>.SuccessResult();
    }


    /// <summary>
    /// 删除
    /// </summary>
    /// <param name="ids"></param>
    /// <returns></returns>
    [HttpDelete]
    [Permission("system:job:delete")]
    public async Task<ServerResponseEntity<string>> RemoveBySysJobIdsAsync([FromBody] List<long> ids)
    {
        await _sysJobService.RemoveBySysJobIdsAsync(ids);
        return ServerResponseEntity<string>.SuccessResult();
    }
}