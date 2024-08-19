using System.Net.Mime;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.Extensions.Logging;
using Ytrue.Infrastructures.Bases;
using Ytrue.Infrastructures.Extensions;
using Ytrue.Infrastructures.Response;
using Ytrue.Infrastructures.Utilities;

namespace Ytrue.Infrastructures.Interceptors.Filters;

/// <summary>
/// 全局异常处理器
/// </summary>
public class GlobalExceptionFilter : IExceptionFilter
{
    private readonly ILogger<GlobalExceptionFilter> _logger;

    /// <summary>
    /// 构造
    /// </summary>
    /// <param name="logger"></param>
    public GlobalExceptionFilter(ILogger<GlobalExceptionFilter> logger)
    {
        _logger = logger;
    }

    /// <summary>
    /// 处理异常
    /// </summary>
    /// <param name="context"></param>
    /// <exception cref="NotImplementedException"></exception>
    public async void OnException(ExceptionContext context)
    {
        // 获取httpContext
        var httpContext = context.HttpContext;
        // 获取异常
        var exception = context.Exception;
        // print error log
        _logger.LogError(exception, "GlobalExceptionFilter get error");

        var errorCode = ServerResponseEnum.InternalServerError.GetDescription();
        if (exception is BaseCodeException baseCodeException)
        {
            errorCode = baseCodeException.Code;
        }

        // 设置响应格式是json
        httpContext.Response.ContentType = MediaTypeNames.Application.Json;
        // 响应的json字符串
        await httpContext.Response.WriteAsync(
            JsonUtil.SerializeObject(ServerResponseEntity<string>.FailResult(errorCode, exception.Message)));
        // 设置为true，表示异常已经被处理了
        context.ExceptionHandled = true;
    }
}