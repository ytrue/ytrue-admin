namespace Ytrue.Infrastructures.Utilities;

public class StringUtil
{
    /// <summary>
    /// 安全的子字符串截取方法，防止越界异常
    /// </summary>
    /// <param name="input"></param>
    /// <param name="startIndex"></param>
    /// <param name="length"></param>
    /// <returns></returns>
    public static string SafeSubString(string input, int startIndex, int length)
    {
        if (string.IsNullOrEmpty(input))
            return input;

        if (startIndex < 0)
            startIndex = 0;

        if (startIndex >= input.Length)
            return string.Empty;

        var endIndex = startIndex + length - 1;
        if (endIndex >= input.Length)
            endIndex = input.Length - 1;

        return input.Substring(startIndex, endIndex - startIndex + 1);
    }
}