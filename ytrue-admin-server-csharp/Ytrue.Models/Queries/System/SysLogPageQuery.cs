using System.Linq.Expressions;
using SqlSugar;
using Ytrue.Models.Bases;
using Ytrue.Models.Entities.System;

namespace Ytrue.Models.Queries.System;

public class SysLogPageQuery : PageQuery<SysLog>
{
    /// <summary>
    /// 请求类型
    /// #HttpMethod{GET;POST;PUT;DELETE;PATCH;TRACE;HEAD;OPTIONS;}
    /// </summary>
    public string? HttpMethod { get; set; }

    /// <summary>
    /// 操作IP
    /// </summary>
    public string? RequestIp { get; set; }

    /// <summary>
    /// 请求地址
    /// </summary>
    public string? RequestUri { get; set; }

    /// <summary>
    /// 日志类型,这里的异常是系统异常
    /// #LogType{OPT:操作类型;EX:异常类型}
    /// </summary>
    public string? Type { get; set; }


    /// <summary>
    /// 开始时间
    /// </summary>
    public List<DateTime>? StartTime { get; set; }

    /// <summary>
    /// 获取查询表达式
    /// </summary>
    /// <returns></returns>
    public override Expression<Func<SysLog, bool>> GetQueryExpression()
    {
        var predicate = Expressionable.Create<SysLog>()
            .AndIF(!string.IsNullOrEmpty(HttpMethod),
                entity => entity.HttpMethod != null && entity.HttpMethod.Contains(HttpMethod!))
            .AndIF(!string.IsNullOrEmpty(RequestIp),
                entity => entity.RequestIp != null && entity.RequestIp.Contains(RequestIp!))
            .AndIF(!string.IsNullOrEmpty(RequestUri),
                entity => entity.RequestUri != null && entity.RequestUri.Contains(RequestUri!))
            .AndIF(!string.IsNullOrEmpty(Type), entity => entity.RequestUri != null && entity.RequestUri.Equals(Type))
            .AndIF(StartTime is { Count: 2 },
                entity => entity.StartTime >= StartTime![0] && entity.StartTime <= StartTime[1])
            .ToExpression();

        return predicate;
    }
}