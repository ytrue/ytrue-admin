using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Ytrue.Models.Entities.System;

/// <summary>
/// 系统角色菜单关联实体类
/// </summary>
[Table("sys_role_menu")]
public class SysRoleMenu
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
    /// 菜单ID
    /// </summary>
    [Column("menu_id")]
    public long MenuId { get; set; }
}