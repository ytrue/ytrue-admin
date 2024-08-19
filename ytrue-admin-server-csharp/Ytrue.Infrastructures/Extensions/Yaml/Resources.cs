﻿using System.Diagnostics;
using System.Globalization;
using System.Reflection;
using System.Resources;

namespace Ytrue.Infrastructures.Extensions.Yaml;

internal static class Resources
{
    private static readonly ResourceManager _resourceManager
        = new ResourceManager("NetEscapades.Configuration.Yaml.Resources", typeof(Resources).GetTypeInfo().Assembly);

    /// <summary>
    /// The configuration file '{0}' was not found and is not optional.
    /// </summary>
    internal static string Error_FileNotFound => GetString("Error_FileNotFound");

    /// <summary>
    /// The configuration file '{0}' was not found and is not optional.
    /// </summary>
    internal static string FormatError_FileNotFound(object p0)
    {
        return string.Format(CultureInfo.CurrentCulture, GetString("Error_FileNotFound"), p0);
    }

    /// <summary>
    /// File path must be a non-empty string.
    /// </summary>
    internal static string Error_InvalidFilePath => GetString("Error_InvalidFilePath");

    /// <summary>
    /// File path must be a non-empty string.
    /// </summary>
    internal static string FormatError_InvalidFilePath()
    {
        return GetString("Error_InvalidFilePath");
    }

    /// <summary>
    /// Could not parse the Yaml file. Error on line number '{0}': '{1}'.
    /// </summary>
    internal static string Error_YamlParseError => GetString("Error_YamlParseError");

    /// <summary>
    /// Could not parse the YAML file: {0}.
    /// </summary>
    internal static string FormatError_YamlParseError(object p0)
    {
        return string.Format(CultureInfo.CurrentCulture, GetString("Error_YamlParseError"), p0);
    }

    /// <summary>
    /// A duplicate key '{0}' was found.
    /// </summary>
    internal static string Error_KeyIsDuplicated => GetString("Error_KeyIsDuplicated");

    /// <summary>
    /// A duplicate key '{0}' was found.
    /// </summary>
    internal static string FormatError_KeyIsDuplicated(object p0)
    {
        return string.Format(CultureInfo.CurrentCulture, GetString("Error_KeyIsDuplicated"), p0);
    }

    private static string GetString(string name, params string[]? formatterNames)
    {
        var value = _resourceManager.GetString(name);

        Debug.Assert(value != null);

        if (formatterNames == null) return value;
        for (var i = 0; i < formatterNames.Length; i++)
        {
            value = value.Replace("{" + formatterNames[i] + "}", "{" + i + "}");
        }

        return value;
    }
}