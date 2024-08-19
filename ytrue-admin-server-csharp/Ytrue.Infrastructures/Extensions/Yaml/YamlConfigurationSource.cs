using Microsoft.Extensions.Configuration;

namespace Ytrue.Infrastructures.Extensions.Yaml;

public class YamlConfigurationSource : FileConfigurationSource
{
    public override IConfigurationProvider Build(IConfigurationBuilder builder)
    {
        EnsureDefaults(builder);
        return new YamlConfigurationProvider(this);
    }
}