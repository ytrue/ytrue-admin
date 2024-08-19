namespace Ytrue.Models.Bases;

/// <summary>
/// 表示一个分页结果，包含类型为 T 的项目列表。
/// </summary>
/// <typeparam name="T">分页结果中项目的类型。</typeparam>
public class PageResult<T>
{
    /// <summary>
    /// 获取或设置当前页的项目列表。
    /// </summary>
    public List<T> Records { get; set; }

    /// <summary>
    /// 获取或设置当前页的页码。
    /// </summary>
    public int Current { get; set; }

    /// <summary>
    /// 获取或设置每页显示的项目数量。
    /// </summary>
    public int Size { get; set; }

    /// <summary>
    /// 获取或设置所有页中的总项目数。
    /// </summary>
    public int Total { get; set; }

    /// <summary>
    /// 根据总项目数和每页显示数量计算总页数。
    /// </summary>
    public int Page => (int)Math.Ceiling(Total / (double)Size);
    
    
    
    // 暂时不需要
    //"optimizeCountSql": true,
    //"searchCount": true,
    //"maxLimit": null,
    //"countId": null,
}