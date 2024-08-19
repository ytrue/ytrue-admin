using Ytrue.Infrastructures.Extensions;

namespace Ytrue.Infrastructures.Response;

/// <summary>
/// 统一返回
/// </summary>
/// <typeparam name="T"></typeparam>
public class ServerResponseEntity<T>
{
    /// <summary>
    /// 自定义的响应码，可以和http响应码一致，也可以不一致
    /// </summary>
    public string Code { get; set; } = null!;

    /// <summary>
    /// 中文消息提示
    /// </summary>
    public string Message { get; set; } = null!;

    /// <summary>
    /// 是否成功
    /// </summary>
    public bool Success { get; set; }

    /// <summary>
    /// 响应的数据
    /// </summary>
    public T? Data { get; set; }


    /// <summary>
    ///  成功
    /// </summary>
    /// <returns></returns>
    public static ServerResponseEntity<T> SuccessResult()
    {
        return new ServerResponseEntity<T>
            { Code = ServerResponseEnum.Ok.GetDescription(), Data = default, Message = "成功", Success = true };
    }

    /// <summary>
    ///  成功
    /// </summary>
    /// <param name="data"></param>
    /// <returns></returns>
    public static ServerResponseEntity<T> SuccessResult(T data)
    {
        return new ServerResponseEntity<T>
            { Code = ServerResponseEnum.Ok.GetDescription(), Data = data, Message = "成功", Success = true };
    }

    /// <summary>
    /// 失败
    /// </summary>
    /// <param name="code"></param>
    /// <param name="message"></param>
    /// <returns></returns>
    public static ServerResponseEntity<T> FailResult(string code, string message)
    {
        return new ServerResponseEntity<T> { Code = code, Data = default, Message = message, Success = false };
    }

    /// <summary>
    /// 失败
    /// </summary>
    /// <param name="message"></param>
    /// <returns></returns>
    public static ServerResponseEntity<T> FailResult(string message)
    {
        return new ServerResponseEntity<T>
        {
            Code = ServerResponseEnum.InternalServerError.GetDescription(), Data = default, Message = message,
            Success = false
        };
    }
}