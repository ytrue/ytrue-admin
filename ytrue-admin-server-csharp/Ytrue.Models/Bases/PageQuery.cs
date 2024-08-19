namespace Ytrue.Models.Bases;

public abstract class PageQuery<T> : ListQuery<T> where T : class
{
    /// <summary>
    /// 当前页码，默认是1
    /// </summary>
    public int PageIndex { get; set; } = 1;

    /// <summary>
    /// 当前页码，默认是10
    /// </summary>
    public int PageSize { get; set; } = 10;

    /// <summary>
    /// 获取Skip
    /// </summary>
    /// <returns></returns>
    public int GetSkip()
    {
        return (PageIndex - 1) * PageSize;
    }

    /// <summary>
    /// 获取Take
    /// </summary>
    /// <returns></returns>
    public int GetTake()
    {
        return PageSize;
    }
}