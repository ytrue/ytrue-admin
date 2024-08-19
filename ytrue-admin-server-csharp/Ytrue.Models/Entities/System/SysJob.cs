using SqlSugar;
using Ytrue.Models.Bases;

namespace Ytrue.Models.Entities.System;

/// <summary>
/// 系统岗位实体
/// </summary>
[SugarTable("sys_job")]
public class SysJob : BaseEntity
{
    /// <summary>
    /// 岗位名称
    /// </summary>
    [SugarColumn(ColumnName = "job_name")]
    public string JobName { get; set; } = null!;

    /// <summary>
    /// 岗位状态:0=禁用,1=启用
    /// </summary>
    [SugarColumn(ColumnName = "status")]
    public bool Status { get; set; }

    /// <summary>
    /// 排序
    /// </summary>
    [SugarColumn(ColumnName = "job_sort")]
    public int JobSort { get; set; }
}