using System.ComponentModel;

namespace Ytrue.Infrastructures.Interceptors.Filters.Operationlog;

public enum OperationLogTypeEnum
{
    [Description("异常类型")] EX,
    [Description("操作类型")] OPT = 1
}