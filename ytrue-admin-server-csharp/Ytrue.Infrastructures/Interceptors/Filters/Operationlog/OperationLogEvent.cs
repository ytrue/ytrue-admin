using MediatR;

namespace Ytrue.Infrastructures.Interceptors.Filters.Operationlog;

public record OperationLogEvent(OperationLogEntity OperationLog) : INotification
{
}