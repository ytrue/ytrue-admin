namespace Ytrue.Models.Resps.System;

public class SysDeptIdResp
{
    
    /// <summary>
    /// ID
    /// </summary>
    public long Id { get; set; }
    
    /// <summary>
    /// 上级部门
    /// </summary>
    public long Pid { get; set; }
 

    /// <summary>
    /// 名称
    /// </summary>
    public string DeptName { get; set; }

    /// <summary>
    /// 排序
    /// </summary>
    public int DeptSort { get; set; }

    /// <summary>
    /// 负责人
    /// </summary>
    public string Leader { get; set; }

    /// <summary>
    /// 邮箱
    /// </summary>
    public string Email { get; set; }

    /// <summary>
    /// 联系电话
    /// </summary>
    public string Phone { get; set; }

    /// <summary>
    /// 状态:1=启用,0=禁用
    /// </summary>
    public bool Status { get; set; }
}