using Ytrue.Models.Entities.System;

namespace Ytrue.Models.Queries.System;

public class SysDeptListQuery
{
    /// <summary>
    /// 岗位名称
    /// </summary>
    public string? DeptName { get; set; }


    /// <summary>
    /// 状态
    /// </summary>
    public bool? Status { get; set; }


    /// <summary>
    /// 创建时间
    /// </summary>
    public List<DateTime>? CreateTime { get; set; }


    /// <summary>
    /// 获取查询条件
    /// </summary>
    /// <param name="queryable"></param>
    /// <returns></returns>
    public IQueryable<SysDept> GetQueryWhere(IQueryable<SysDept> queryable)
    {
        // DeptName 条件
        if (!string.IsNullOrEmpty(DeptName))
        {
            queryable = queryable.Where(job => job.DeptName.Contains(DeptName));
        }

        // Status 条件
        if (Status != null)
        {
            queryable = queryable.Where(job => job.Status == Status);
        }

        // 当CreateTime有两个元素时的逻辑
        if (CreateTime is { Count: 2 })
        {
            queryable = queryable.Where(job => job.CreateTime >= CreateTime[0] && job.CreateTime <= CreateTime[1]);
        }

        return queryable;
    }
}