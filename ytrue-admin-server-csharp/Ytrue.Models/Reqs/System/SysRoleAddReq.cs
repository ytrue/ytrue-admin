using System.ComponentModel.DataAnnotations;

namespace Ytrue.Models.Reqs.System;

public class SysRoleAddReq
{
    [Required(ErrorMessage = "角色名称不能为空")]
    public string RoleName { get; set; }

    [Required(ErrorMessage = "角色标识不能为空")] 
    public string RoleCode { get; set; }

    /// <summary>
    /// 数据范围:1=全部数据权限,2=自定数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限
    /// </summary>
    public int DataScope { get; set; }

    /// <summary>
    /// 排序
    /// </summary>
    public int RoleSort { get; set; }

    /// <summary>
    /// 描述
    /// </summary>
    public string Description { get; set; }

    /// <summary>
    /// 状态:0=禁用,1=正常
    /// </summary>
    public bool Status { get; set; }

    /// <summary>
    /// 菜单树选择项是否关联显示:0=父子不互相关联显示,1=父子互相关联显示
    /// </summary>
    public bool MenuCheckStrictly { get; set; }

    /// <summary>
    /// 部门树选择项是否关联显示:0=父子不互相关联显示,1=父子互相关联显示
    /// </summary>
    public bool DeptCheckStrictly { get; set; }

    /// <summary>
    /// 菜单id集合
    /// </summary>
    public List<long>? MenuIds { get; set; }

    /// <summary>
    /// 部门id集合
    /// </summary>
    public List<long>? DeptIds { get; set; }
}