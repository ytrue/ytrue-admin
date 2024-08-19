using System.ComponentModel.DataAnnotations.Schema;
using SqlSugar;

namespace Ytrue.Models.Bases;

public class BaseEntity
{
    /// <summary>
    /// ID
    /// </summary>
    [SugarColumn(IsPrimaryKey = true, IsIdentity = true)]
    public long Id { get; set; }

    /// <summary>
    /// 创建者
    /// </summary>
    [Column("create_by")]
    [SugarColumn(ColumnName = "create_by")]
    public string? CreateBy { get; set; }

    /// <summary>
    /// 更新者
    /// </summary>
    [SugarColumn(ColumnName = "update_by")]
    public string? UpdateBy { get; set; }

    /// <summary>
    /// 创建日期
    /// </summary>
    [SugarColumn(ColumnName = "create_time", InsertServerTime = true)]
    public DateTime CreateTime { get; set; }

    /// <summary>
    /// 更新时间
    /// </summary>
    [SugarColumn(ColumnName = "update_time", UpdateServerTime = true)]
    public DateTime? UpdateTime { get; set; }
}