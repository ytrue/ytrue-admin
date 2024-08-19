namespace Ytrue.Infrastructures.Response;

public interface IServerResponseInfo
{
    /// <summary>
    /// 获取code
    /// </summary>
    /// <returns></returns>
    string Code();
    
    
    /// <summary>
    /// 获取错误信息
    /// </summary>
    /// <returns></returns>
    string Message();
}