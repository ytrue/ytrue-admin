namespace Ytrue.Infrastructures.Utilities;

/// <summary>
/// url util
/// </summary>
public static class MatchUtil
{
    /// <summary>
    /// 判断请求路径是否匹配指定的模式
    /// </summary>
    /// <param name="pattern">模式字符串，可以包含 * 和 ** 通配符</param>
    /// <param name="requestPath">要检查的请求路径</param>
    /// <returns>如果请求路径匹配模式，则返回 true，否则返回 false</returns>
    public static bool IsPathMatch(string pattern, string requestPath)
    {
        pattern = pattern.Trim('/');
        requestPath = requestPath.Trim('/');

        // 将模式字符串分割为部分
        var patternParts = pattern.Split('/');
        var pathParts = requestPath.Split('/');

        var patternIndex = 0;
        var pathIndex = 0;

        while (patternIndex < patternParts.Length && pathIndex < pathParts.Length)
        {
            var patternPart = patternParts[patternIndex];
            var pathPart = pathParts[pathIndex];

            if (patternPart.Equals("*", StringComparison.OrdinalIgnoreCase))
            {
                // * 匹配当前路径部分，继续下一个部分
                patternIndex++;
                pathIndex++;
            }
            else if (patternPart.Equals("**", StringComparison.OrdinalIgnoreCase))
            {
                // ** 匹配剩余所有路径部分，直接返回 true
                return true;
            }
            else if (!patternPart.Equals(pathPart, StringComparison.OrdinalIgnoreCase))
            {
                // 不匹配，返回 false
                return false;
            }
            else
            {
                // 匹配当前部分，继续下一个部分
                patternIndex++;
                pathIndex++;
            }
        }

        // 检查是否还有剩余的模式部分，如果有且不是 **，则不匹配
        while (patternIndex < patternParts.Length)
        {
            if (!patternParts[patternIndex].Equals("**", StringComparison.OrdinalIgnoreCase))
                return false;

            patternIndex++;
        }

        // 如果路径部分还有剩余未匹配的，也不匹配
        return pathIndex >= pathParts.Length;
    }
}