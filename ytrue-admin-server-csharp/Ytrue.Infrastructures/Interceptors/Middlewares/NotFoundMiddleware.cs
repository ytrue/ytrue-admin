using Microsoft.AspNetCore.Http;
using Ytrue.Infrastructures.Extensions;
using Ytrue.Infrastructures.Response;
using Ytrue.Infrastructures.Utilities;

namespace Ytrue.Infrastructures.Interceptors.Middlewares;

/// <summary>
/// 全局异常补充
/// </summary>
public class NotFoundMiddleware
{
    private readonly RequestDelegate _next;

    /// <summary>
    /// 构造
    /// </summary>
    /// <param name="next"></param>
    public NotFoundMiddleware(RequestDelegate next)
    {
        _next = next;
    }


    /// <summary>
    /// 处理
    /// </summary>
    /// <param name="context"></param>
    public async Task Invoke(HttpContext context)
    {
        await _next(context);
        if (context.Response.StatusCode == StatusCodes.Status404NotFound)
        {
            // 设置响应头的字符编码为 UTF-8
            context.Response.ContentType = "application/json; charset=utf-8";
            await context.Response.WriteAsync(
                JsonUtil.SerializeObject(
                    ServerResponseEntity<string>.FailResult(ServerResponseEnum.NotFound.GetDescription(), "资源不存在")));
        }
    }
}