using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Ytrue.Models.Entities.System;

/// <summary>
/// 菜单权限实体
/// </summary>
[Table("sys_menu")]
public class SysMenu
{
    /// <summary>
    /// 菜单ID
    /// </summary>
    [Key]
    public long Id { get; set; }

    /// <summary>
    /// 菜单名称
    /// </summary>
    [Column("menu_name")]
    public string MenuName { get; set; }

    /// <summary>
    /// 父菜单ID
    /// </summary>
    [Column("pid")]
    public long Pid { get; set; }

    /// <summary>
    /// 显示顺序
    /// </summary>
    [Column("menu_sort")]
    public int MenuSort { get; set; }

    /// <summary>
    /// 路由地址
    /// </summary>
    [Column("path")]
    public string Path { get; set; }

    /// <summary>
    /// 组件路径
    /// </summary>
    [Column("component")]
    public string? Component { get; set; }

    /// <summary>
    /// 路由参数
    /// </summary>
    [Column("query")]
    public string? Query { get; set; }

    /// <summary>
    /// 是否为外链（1是 0否）
    /// </summary>
    [Column("is_frame")]
    public bool IsFrame { get; set; }

    /// <summary>
    /// 是否缓存（1缓存 0不缓存）
    /// </summary>
    [Column("is_cache")]
    public bool IsCache { get; set; }

    /// <summary>
    /// 菜单类型（M目录 C菜单 F按钮）
    /// </summary>
    [Column("menu_type")]
    public char MenuType { get; set; }

    /// <summary>
    /// 菜单状态（1显示 0隐藏）
    /// </summary>
    [Column("visible")]
    public bool Visible { get; set; }

    /// <summary>
    /// 菜单状态（1正常 0停用）
    /// </summary>
    [Column("status")]
    public bool Status { get; set; }

    /// <summary>
    /// 权限标识
    /// </summary>
    [Column("perms")]
    public string? Perms { get; set; }

    /// <summary>
    /// 菜单图标
    /// </summary>
    [Column("icon")]
    public string Icon { get; set; }

    /// <summary>
    /// 子菜单数量
    /// </summary>
    [Column("sub_count")]
    public int SubCount { get; set; }

    /// <summary>
    /// 创建者
    /// </summary>
    [Column("create_by")]
    public string CreateBy { get; set; }

    /// <summary>
    /// 创建时间
    /// </summary>
    [Column("create_time")]
    public DateTime CreateTime { get; set; }

    /// <summary>
    /// 更新者
    /// </summary>
    [Column("update_by")]
    public string? UpdateBy { get; set; }

    /// <summary>
    /// 更新时间
    /// </summary>
    [Column("update_time")]
    public DateTime? UpdateTime { get; set; }
}
