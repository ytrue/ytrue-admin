using Microsoft.Extensions.Configuration;
using YamlDotNet.Core;

namespace Ytrue.Infrastructures.Extensions.Yaml;

/// <summary>
/// https://github.com/andrewlock/NetEscapades.Configuration
/// Yaml是基于文件的，可以直接从FileConfigurationProvider继承，在FileConfigurationProvider实现了监控文件变化并自动重新加载的功能 
/// </summary>
public class YamlConfigurationProvider : FileConfigurationProvider
{
    public YamlConfigurationProvider(YamlConfigurationSource source) : base(source)
    {
    }

    public override void Load(Stream stream)
    {
        var parser = new YamlConfigurationStreamParser();
        try
        {
            Data = parser.Parse(stream);
        }
        catch (YamlException e)
        {
            throw new FormatException(Resources.FormatError_YamlParseError(e.Message), e);
        }
    }
}