using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.FileProviders;

namespace Ytrue.Infrastructures.Extensions.Yaml;

/// <summary>
/// Extensions methods for adding <see cref="YamlConfigurationExtensions"/>.
/// </summary>
public static class YamlConfigurationExtension
{
    public static IConfigurationBuilder AddYamlFile(this IConfigurationBuilder builder, string path)
    {
        return AddYamlFile(builder, provider: null, path: path, optional: false, reloadOnChange: false);
    }


    public static IConfigurationBuilder AddYamlFile(this IConfigurationBuilder builder, string path, bool optional)
    {
        return AddYamlFile(builder, provider: null, path: path, optional: optional, reloadOnChange: false);
    }


    public static IConfigurationBuilder AddYamlFile(this IConfigurationBuilder builder, string path, bool optional,
        bool reloadOnChange)
    {
        return AddYamlFile(builder, provider: null, path: path, optional: optional, reloadOnChange: reloadOnChange);
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="builder"></param>
    /// <param name="provider"></param>
    /// <param name="path">file path</param>
    /// <param name="optional"> 文件是可选的</param>
    /// <param name="reloadOnChange"> 保存更改后会重载文件</param>
    /// <returns></returns>
    /// <exception cref="ArgumentNullException"></exception>
    /// <exception cref="ArgumentException"></exception>
    // ReSharper disable once MemberCanBePrivate.Global
    public static IConfigurationBuilder AddYamlFile(this IConfigurationBuilder builder, IFileProvider? provider,
        string path, bool optional, bool reloadOnChange)
    {
        ArgumentNullException.ThrowIfNull(builder);

        if (string.IsNullOrEmpty(path))
        {
            throw new ArgumentException(Resources.FormatError_InvalidFilePath(), nameof(path));
        }

        return builder.AddYamlFile(s =>
        {
            s.FileProvider = provider;
            s.Path = path;
            s.Optional = optional;
            s.ReloadOnChange = reloadOnChange;
            s.ResolveFileProvider();
        });
    }

    /// <summary>
    /// Adds a YAML configuration source to <paramref name="builder"/>.
    /// </summary>
    /// <param name="builder">The <see cref="IConfigurationBuilder"/> to add to.</param>
    /// <param name="configureSource">Configures the source.</param>
    /// <returns>The <see cref="IConfigurationBuilder"/>.</returns>
    public static IConfigurationBuilder AddYamlFile(this IConfigurationBuilder builder,
        Action<YamlConfigurationSource> configureSource) => builder.Add(configureSource);
}