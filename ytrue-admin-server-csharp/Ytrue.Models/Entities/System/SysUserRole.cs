using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Ytrue.Models.Entities.System;

/// <summary>
/// 用户角色关联实体
/// </summary>
[Table("sys_user_role")]
public class SysUserRole
{
    /// <summary>
    /// id
    /// </summary>
    [Key]
    public long Id { get; set; }

    /// <summary>
    /// 用户ID
    /// </summary>
    [Column("user_id")]
    public long UserId { get; set; }

    /// <summary>
    /// 角色ID
    /// </summary>
    [Column("role_id")]
    public long RoleId { get; set; }
}