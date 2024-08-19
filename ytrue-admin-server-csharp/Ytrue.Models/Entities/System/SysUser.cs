using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Ytrue.Models.Entities.System;

/// <summary>
/// 系统用户实体
/// </summary>
[Table("sys_user")]
public class SysUser
{
    /// <summary>
    /// 用户ID
    /// </summary>
    [Key]
    public long Id { get; set; }

    /// <summary>
    /// 部门ID
    /// </summary>
    [Column("dept_id")]
    public long? DeptId { get; set; }

    /// <summary>
    /// 用户名
    /// </summary>
    [Column("username")]
    public string? UserName { get; set; }

    /// <summary>
    /// 昵称
    /// </summary>
    [Column("nick_name")]
    public string? NickName { get; set; }

    /// <summary>
    /// 性别: 0=女, 1=男
    /// </summary>
    [Column("gender")]
    public bool Gender { get; set; }

    /// <summary>
    /// 手机号码
    /// </summary>
    [Column("phone")]
    public string? Phone { get; set; }

    /// <summary>
    /// 邮箱
    /// </summary>
    [Column("email")]
    public string? Email { get; set; }

    /// <summary>
    /// 头像地址
    /// </summary>
    [Column("avatar_name")]
    public string? AvatarName { get; set; }

    /// <summary>
    /// 头像真实路径
    /// </summary>
    [Column("avatar_path")]
    public string? AvatarPath { get; set; }

    /// <summary>
    /// 密码
    /// </summary>
    [Column("password")]
    public string? Password { get; set; }

    /// <summary>
    /// 是否为admin账号: 1=是, 0=否
    /// </summary>
    [Column("admin")]
    public bool? Admin { get; set; }

    /// <summary>
    /// 状态: 1=启用, 0=禁用
    /// </summary>
    [Column("status")]
    public bool? Status { get; set; }

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
    /// 修改密码的时间
    /// </summary>
    [Column("pwd_reset_time")]
    public DateTime? PwdResetTime { get; set; }

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
