using Ytrue.Infrastructures.Extensions;

namespace Ytrue.Infrastructures.Response;

public sealed class ServerResponseInfo : IServerResponseInfo
{
    private ServerResponseInfo(string code, string message)
    {
        _code = code;
        _message = message;
    }

    private readonly string _code;

    private readonly string _message;

    
    /// <summary>
    /// 错误返回
    /// </summary>
    /// <param name="message"></param>
    /// <returns></returns>
    public static IServerResponseInfo Error(string message)
    {
        return new ServerResponseInfo(ServerResponseEnum.InternalServerError.GetDescription(), message);
    }


    public string Code()
    {
        return _code;
    }

    public string Message()
    {
        return _message;
    }
}