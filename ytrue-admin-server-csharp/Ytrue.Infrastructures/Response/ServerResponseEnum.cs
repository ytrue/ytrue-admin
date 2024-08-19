using System.ComponentModel;

namespace Ytrue.Infrastructures.Response;

/// <summary>
/// 响应状态码
/// </summary>
public enum ServerResponseEnum
{
    /// <summary>
    /// 表示请求成功，状态码为200。
    /// </summary>
    [Description("200")] Ok = 200,

    /// <summary>
    /// 表示服务器内部错误，状态码为500。
    /// </summary>
    [Description("500")] InternalServerError = 500,

    /// <summary>
    /// 表示错误的请求，状态码为400。
    /// </summary>
    [Description("400")] BadRequest = 400,

    /// <summary>
    /// 表示未授权的请求，状态码为401。
    /// </summary>
    [Description("401")] Unauthorized = 401,

    /// <summary>
    /// 表示禁止访问，状态码为403。
    /// </summary>
    [Description("403")] Forbidden = 403,

    /// <summary>
    /// 表示请求的资源未找到，状态码为404。
    /// </summary>
    [Description("404")] NotFound = 404
}