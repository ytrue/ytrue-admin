using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace Ytrue.Infrastructures.Utilities;

/// <summary>
/// json 工具列
/// </summary>
public static class JsonUtil
{
    /// <summary>
    /// 序列化
    /// </summary>
    /// <param name="value"></param>
    /// <returns></returns>
    public static string SerializeObject(object value)
    {
        var settings = new JsonSerializerSettings()
        {
            ContractResolver = new CamelCasePropertyNamesContractResolver()
        };
        return JsonConvert.SerializeObject(value, settings);
    }


    /// <summary>
    /// 反列列话
    /// </summary>
    /// <param name="json"></param>
    /// <typeparam name="T"></typeparam>
    /// <returns></returns>
    public static T? DeserializeObject<T>(string json)
    {
        var settings = new JsonSerializerSettings
        {
            ContractResolver = new CamelCasePropertyNamesContractResolver()
        };
        return JsonConvert.DeserializeObject<T>(json, settings);
    }
}