using System.ComponentModel.DataAnnotations.Schema;
using Ytrue.Models.Bases;

namespace Ytrue.Models.Entities.System;

/// <summary>
/// 系统部门实体
/// </summary>
[Table("sys_dept")]
public class SysDept : BaseEntity
{
    /// <summary>
    /// 上级部门
    /// </summary>
    [Column("pid")]
    public long Pid { get; set; }

    /// <summary>
    /// 子部门数目
    /// </summary>
    [Column("sub_count")]
    public int SubCount { get; set; }

    /// <summary>
    /// 名称
    /// </summary>
    [Column("dept_name")]
    public string DeptName { get; set; }

    /// <summary>
    /// 排序
    /// </summary>
    [Column("dept_sort")]
    public int DeptSort { get; set; }

    /// <summary>
    /// 负责人
    /// </summary>
    [Column("leader")]
    public string Leader { get; set; }

    /// <summary>
    /// 邮箱
    /// </summary>
    [Column("email")]
    public string Email { get; set; }

    /// <summary>
    /// 联系电话
    /// </summary>
    [Column("phone")]
    public string Phone { get; set; }

    /// <summary>
    /// 状态:1=启用,0=禁用
    /// </summary>
    [Column("status")]
    public bool Status { get; set; }
}