using Ytrue.Infrastructures.Response;

namespace Ytrue.Infrastructures.Bases;

public abstract class BaseCodeException : Exception
{
    /// <summary>
    /// 状态吗
    /// </summary>
    public string Code { set; get; } = "500";


    protected BaseCodeException()
    {
    }

    protected BaseCodeException(string? message) : base(message)
    {
    }


    protected BaseCodeException(string code, string message) : base(message)
    {
        Code = code;
    }

    protected BaseCodeException(string? message, Exception? innerException) : base(message, innerException)
    {
    }


    protected BaseCodeException(IServerResponseInfo responseCode) : base(responseCode.Message())
    {
        Code = responseCode.Code();
    }
}