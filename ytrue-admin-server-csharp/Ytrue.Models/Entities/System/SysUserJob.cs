using SqlSugar;
using Ytrue.Models.Bases;

namespace Ytrue.Models.Entities.System;

/// <summary>
/// 用户岗位关联实体
/// </summary>
[SugarTable("sys_user_job")]
public class SysUserJob : BaseIdEntity
{
    /// <summary>
    /// 用户ID
    /// </summary>
    [SugarColumn(ColumnName = "user_id")]
    public long UserId { get; set; }

    /// <summary>
    /// 岗位ID
    /// </summary>
    [SugarColumn(ColumnName = "job_id")]
    public long JobId { get; set; }
}