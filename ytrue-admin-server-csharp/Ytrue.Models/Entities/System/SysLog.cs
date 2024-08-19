using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Ytrue.Models.Entities.System;

/// <summary>
/// 操作日志实体
/// </summary>
[Table("sys_log")]
public class SysLog
{
    /// <summary>
    /// id
    /// </summary>
    [Key]
    public long Id { get; set; }

    /// <summary>
    /// 操作IP
    /// </summary>
    [Column("request_ip")]
    public string? RequestIp { get; set; }

    /// <summary>
    /// OPT:操作类型;EX:异常类型
    /// </summary>
    [Column("type")]
    public string? Type { get; set; }

    /// <summary>
    /// 操作人
    /// </summary>
    [Column("operator")]
    public string? Operator { get; set; }

    /// <summary>
    /// 操作描述
    /// </summary>
    [Column("description")]
    public string? Description { get; set; }

    /// <summary>
    /// 类路径
    /// </summary>
    [Column("class_path")]
    public string? ClassPath { get; set; }

    /// <summary>
    /// 请求方法
    /// </summary>
    [Column("action_method")]
    public string? ActionMethod { get; set; }

    /// <summary>
    /// 请求url
    /// </summary>
    [Column("request_uri")]
    public string? RequestUri { get; set; }

    /// <summary>
    /// 请求类型
    /// </summary>
    [Column("http_method")]
    public string? HttpMethod { get; set; }

    /// <summary>
    /// 请求参数
    /// </summary>
    [Column("params")]
    public string? Params { get; set; }

    /// <summary>
    /// 结果
    /// </summary>
    [Column("result")]
    public string? Result { get; set; }

    /// <summary>
    /// 异常详情信息
    /// </summary>
    [Column("ex_desc")]
    public string? ExDesc { get; set; }

    /// <summary>
    /// 异常描述
    /// </summary>
    [Column("ex_detail")]
    public string? ExDetail { get; set; }

    /// <summary>
    /// 浏览器
    /// </summary>
    [Column("browser")]
    public string? Browser { get; set; }

    /// <summary>
    /// 开始时间
    /// </summary>
    [Column("start_time")]
    public DateTime? StartTime { get; set; }

    /// <summary>
    /// 完成时间
    /// </summary>
    [Column("end_time")]
    public DateTime? EndTime { get; set; }

    /// <summary>
    /// 消耗时间
    /// </summary>
    [Column("consuming_time")]
    public long? ConsumingTime { get; set; }
}

