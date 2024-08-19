using System.ComponentModel;

namespace Ytrue.Infrastructures.Extensions;

public static class EnumExtension
{
    /// <summary>
    /// 获取DescriptionAttribute内容
    /// </summary>
    /// <param name="val"></param>
    /// <returns></returns>
    public static string GetDescription(this Enum val)
    {
        var field = val.GetType().GetField(val.ToString());
        if (field == null)
        {
            return val.ToString();
        }

        var customAttribute = Attribute.GetCustomAttribute(field, typeof(DescriptionAttribute));
        return customAttribute == null ? val.ToString() : ((DescriptionAttribute)customAttribute).Description;
    }
}