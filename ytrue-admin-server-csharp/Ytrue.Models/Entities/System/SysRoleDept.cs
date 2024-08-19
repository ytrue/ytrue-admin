using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Ytrue.Models.Entities.System;

/// <summary>
/// 角色部门关联实体
/// </summary>
[Table("sys_role_dept")]
public class SysRoleDept
{
    /// <summary>
    /// id
    /// </summary>
    [Key]
    public long Id { get; set; }

    /// <summary>
    /// 角色ID
    /// </summary>
    [Column("role_id")]
    public long RoleId { get; set; }

    /// <summary>
    /// 部门ID
    /// </summary>
    [Column("dept_id")]
    public long DeptId { get; set; }
}