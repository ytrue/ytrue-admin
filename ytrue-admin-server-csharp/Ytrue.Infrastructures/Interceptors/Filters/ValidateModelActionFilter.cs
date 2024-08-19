using System.Net.Mime;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Ytrue.Infrastructures.Extensions;
using Ytrue.Infrastructures.Response;
using Ytrue.Infrastructures.Utilities;

namespace Ytrue.Infrastructures.Interceptors.Filters;

/// <summary>
/// 模型校验
/// </summary>
public class ValidateModelActionFilter : IActionFilter
{
    /// <summary>
    /// 处理验证
    /// </summary>
    /// <param name="context"></param>
    public void OnActionExecuting(ActionExecutingContext context)
    {
        // 模型状态
        var modelState = context.ModelState;
        // 验证通过，不处理
        if (modelState.IsValid) return;
        // 获取第一个错误
        var message = modelState[modelState.Keys.First()]?.Errors.FirstOrDefault()?.ErrorMessage;
        var errorMessage = "参数校验错误：" + message;

        // 响应的json字符串
        context.Result = new ContentResult()
        {
            Content = JsonUtil.SerializeObject(
                ServerResponseEntity<string>.FailResult(ServerResponseEnum.BadRequest.GetDescription(), errorMessage)),
            ContentType = MediaTypeNames.Application.Json
        };
    }


    /// <summary>
    /// 执行后操作
    /// </summary>
    /// <param name="context"></param>
    public void OnActionExecuted(ActionExecutedContext context)
    {
        // ignore
    }
}