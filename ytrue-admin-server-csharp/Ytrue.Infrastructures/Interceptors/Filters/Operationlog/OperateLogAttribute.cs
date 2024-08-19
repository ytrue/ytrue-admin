namespace Ytrue.Infrastructures.Interceptors.Filters.Operationlog;

/// <summary>
/// 操作日志记录
/// </summary>
[AttributeUsage(AttributeTargets.Method)]
public class OperateLogAttribute : Attribute
{
    public OperateLogAttribute(string description)
    {
        Description = description;
    }

    public string Description { get; private set; }
}