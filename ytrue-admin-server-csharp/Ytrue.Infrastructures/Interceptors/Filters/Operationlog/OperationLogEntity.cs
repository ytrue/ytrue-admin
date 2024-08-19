namespace Ytrue.Infrastructures.Interceptors.Filters.Operationlog;

/// <summary>
/// 记录数据
/// </summary>
public class OperationLogEntity
{
    /// <summary>
    /// 操作IP
    /// </summary>
    public string RequestIp { get; set; }

    /// <summary>
    /// 日志类型,这里的异常是系统异常
    /// #LogType{OPT:操作类型;EX:异常类型}
    /// </summary>
    public string Type { get; set; }

    /// <summary>
    /// 操作描述
    /// </summary>
    public string Description { get; set; }

    /// <summary>
    /// 类路径
    /// </summary>
    public string ClassPath { get; set; }

    /// <summary>
    /// 请求类型
    /// </summary>
    public string ActionMethod { get; set; }

    /// <summary>
    /// 请求地址
    /// </summary>
    public string RequestUri { get; set; }

    /// <summary>
    /// 请求类型
    /// #HttpMethod{GET;POST;PUT;DELETE;PATCH;TRACE;HEAD;OPTIONS;}
    /// </summary>
    public string HttpMethod { get; set; }

    /// <summary>
    /// 请求参数
    /// </summary>
    public string Params { get; set; }

    /// <summary>
    /// 返回值
    /// </summary>
    public string Result { get; set; }

    /// <summary>
    /// 异常详情信息
    /// </summary>
    public string ExDesc { get; set; }

    /// <summary>
    /// 异常描述
    /// </summary>
    public string ExDetail { get; set; }

    /// <summary>
    /// 开始时间
    /// </summary>
    public DateTime StartTime { get; set; }

    /// <summary>
    /// 结束时间
    /// </summary>
    public DateTime EndTime { get; set; }

    /// <summary>
    /// 消耗时间
    /// </summary>
    public long ConsumingTime { get; set; }

    /// <summary>
    /// 浏览器
    /// </summary>
    public string Browser { get; set; }
}