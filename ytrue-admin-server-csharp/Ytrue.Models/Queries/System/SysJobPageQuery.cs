using System.Linq.Expressions;
using SqlSugar;
using Ytrue.Models.Bases;
using Ytrue.Models.Entities.System;

namespace Ytrue.Models.Queries.System;

public class SysJobPageQuery : PageQuery<SysJob>
{
    /// <summary>
    /// 岗位名称
    /// </summary>
    public string? JobName { get; set; }


    /// <summary>
    /// 状态
    /// </summary>
    public bool? Status { get; set; }


    /// <summary>
    /// 创建时间
    /// </summary>
    public List<DateTime>? CreateTime { get; set; }


    /// <summary>
    /// 获取查询表达式
    /// </summary>
    /// <returns></returns>
    public override Expression<Func<SysJob, bool>> GetQueryExpression()
    {
        return Expressionable.Create<SysJob>()
            .AndIF(!string.IsNullOrEmpty(JobName), entity => entity.Equals(JobName))
            .AndIF(Status != null, entity => entity.Status == Status)
            .AndIF(CreateTime is { Count: 2 },
                entity => entity.CreateTime >= CreateTime![0] && entity.CreateTime <= CreateTime[1])
            .ToExpression();
    }
}