﻿namespace Ytrue.Models.Resps.System;

public class SysJobIdResp
{
    
    /// <summary>
    /// ID
    /// </summary>
    public long Id { get; set; }
    
    /// <summary>
    /// 岗位名称
    /// </summary>
    public string JobName { get; set; }

    /// <summary>
    /// 岗位状态:0=禁用,1=启用
    /// </summary>
    public bool Status { get; set; }

    /// <summary>
    /// 排序
    /// </summary>
    public int JobSort { get; set; }
}