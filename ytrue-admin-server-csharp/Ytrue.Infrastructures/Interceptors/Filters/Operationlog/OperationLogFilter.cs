using MediatR;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.VisualBasic.CompilerServices;
using Ytrue.Infrastructures.Utilities;

namespace Ytrue.Infrastructures.Interceptors.Filters.Operationlog;

/// <summary>
///  操作日志
/// 参考 https://www.liujiajia.me/2018/11/23/net-core-log-request-and-response-with-filter
/// </summary>
public class OperationLogFilter : IActionFilter, IResultFilter, IExceptionFilter
{
    /// <summary>
    /// 存储线程副本
    /// </summary>
    /// private readonly static AsyncLocal<OperationLogEntity> OperationLogAsyncLocal = new();
    /// <summary>
    /// 调用的时候都是同一个实例你，也可以使用 AsyncLocal ，但是不能使用 ThreadLocal，因为执行的时候不是同一个线程
    /// </summary>
    private readonly OperationLogEntity _operationLogEntity = new();

   


    /// <summary>
    /// 事件发布
    /// </summary>
    private readonly IMediator _mediator;

    public OperationLogFilter(IMediator mediator)
    {
        _mediator = mediator;
    }


    /// <summary>
    /// 进入方法前
    /// </summary>
    /// <param name="context"></param>
    public void OnActionExecuting(ActionExecutingContext context)
    {
        var endpoint = context.HttpContext.GetEndpoint();
        // 获取 OperateLogAttribute 注解
        var operateLogAttribute = endpoint?.Metadata.GetMetadata<OperateLogAttribute>();
        // 获取不到不处理
        if (operateLogAttribute == null)
        {
            return;
        }

        var strArgs = "";
        try
        {
            // 获取方法上面的参数
            strArgs = JsonUtil.SerializeObject(context.ActionArguments);
        }
        catch (Exception e)
        {
            LoggerUtil<OperationLogFilter>.LogWarning(e, "解析参数异常");
        }


        // 修改 operationLog
        _operationLogEntity.Type = OperationLogTypeEnum.OPT.ToString(); // 请求参数
        _operationLogEntity.Params = StringUtil.SafeSubString(strArgs, 0, 65535); // client ip
        _operationLogEntity.RequestIp = context.HttpContext.Connection.RemoteIpAddress?.ToString() ?? ""; // uri
        _operationLogEntity.RequestUri = context.HttpContext.Request.Path; // 浏览器
        _operationLogEntity.Browser = context.HttpContext.Request.Headers.UserAgent.ToString();
        _operationLogEntity.HttpMethod = context.HttpContext.Request.Method; // 控制器类名
        _operationLogEntity.ClassPath = context.Controller.GetType().FullName ?? ""; // 方法名称
        _operationLogEntity.ActionMethod = context.RouteData.Values["action"]?.ToString() ?? ""; // 开始时间
        _operationLogEntity.StartTime = DateTime.Now;
    }


    /// <summary>
    /// 结果返回
    /// </summary>
    /// <param name="context"></param>
    public async void OnResultExecuted(ResultExecutedContext context)
    {
        _operationLogEntity.Result = JsonUtil.SerializeObject(context.Result);
        await PublishOperationLogEventAsync(_operationLogEntity);
    }


    /// <summary>
    /// 出现异常
    /// </summary>
    /// <param name="context"></param>
    public async void OnException(ExceptionContext context)
    {
        _operationLogEntity.Type = OperationLogTypeEnum.EX.ToString();
        _operationLogEntity.ExDesc = context.Exception.Message;
        _operationLogEntity.ExDetail = context.Exception.StackTrace ?? "";
        // 发送事件
        await PublishOperationLogEventAsync(_operationLogEntity);
    }

    /// <summary>
    /// 发送事件
    /// </summary>
    /// <param name="operationLog"></param>
    private async Task PublishOperationLogEventAsync(OperationLogEntity operationLog)
    {
        try
        {
            operationLog.EndTime = DateTime.Now;
            var duration = operationLog.EndTime - operationLog.StartTime;
            // 毫秒
            operationLog.ConsumingTime = Conversions.ToLong(duration.TotalMilliseconds);

            // 发送事件
            await _mediator.Publish(new OperationLogEvent(operationLog));
        }
        catch (Exception e)
        {
            LoggerUtil<OperationLogFilter>.LogWarning(e, "OperationLogFilter publishEvent error");
        }
    }


    public void OnActionExecuted(ActionExecutedContext context)
    {
        // ignore
    }

    public void OnResultExecuting(ResultExecutingContext context)
    {
        // ignore
    }
}