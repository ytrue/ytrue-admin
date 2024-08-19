using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Ytrue.Models.Entities.System;

/// <summary>
/// 系统角色实体
/// </summary>
[Table("sys_role")]
public class SysRole
{
    /// <summary>
    /// ID
    /// </summary>
    [Key]
    public long Id { get; set; }

    /// <summary>
    /// 名称
    /// </summary>
    [Column("role_name")]
    public string RoleName { get; set; }

    /// <summary>
    /// 标识
    /// </summary>
    [Column("role_code")]
    public string RoleCode { get; set; }

    /// <summary>
    /// 描述
    /// </summary>
    [Column("description")]
    public string? Description { get; set; }

    /// <summary>
    /// 数据范围:1=全部数据权限,2=自定数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限
    /// </summary>
    [Column("data_scope")]
    public int DataScope { get; set; }

    /// <summary>
    /// 排序
    /// </summary>
    [Column("role_sort")]
    public int RoleSort { get; set; }

    /// <summary>
    /// 菜单树选择项是否关联显示:0=父子不互相关联显示,1=父子互相关联显示
    /// </summary>
    [Column("menu_check_strictly")]
    public bool? MenuCheckStrictly { get; set; }

    /// <summary>
    /// 部门树选择项是否关联显示:0=父子不互相关联显示,1=父子互相关联显示
    /// </summary>
    [Column("dept_check_strictly")]
    public bool? DeptCheckStrictly { get; set; }

    /// <summary>
    /// 状态:0=禁用,1=正常
    /// </summary>
    [Column("status")]
    public bool Status { get; set; }

    /// <summary>
    /// 创建者
    /// </summary>
    [Column("create_by")]
    public string? CreateBy { get; set; }

    /// <summary>
    /// 更新者
    /// </summary>
    [Column("update_by")]
    public string? UpdateBy { get; set; }

    /// <summary>
    /// 创建日期
    /// </summary>
    [Column("create_time")]
    public DateTime? CreateTime { get; set; }

    /// <summary>
    /// 更新时间
    /// </summary>
    [Column("update_time")]
    public DateTime? UpdateTime { get; set; }
}



