using System.Text;
using Microsoft.AspNetCore.Builder;
using Serilog;
using Serilog.Events;
using Serilog.Sinks.SystemConsole.Themes;

namespace Ytrue.Infrastructures.Extensions;

/// <summary>
/// 扩展方法集中定义 Serilog 日志记录器的配置
/// </summary>
public static class SerilogExtension
{
    /// <summary>
    /// 配置 Serilog 日志记录器。
    /// </summary>
    /// <param name="builder"></param>
    public static void AddSerilogService(this WebApplicationBuilder builder)
    {
        builder.Host.UseSerilog((context, config) =>
        {
            const string outputTemplateString = "{Timestamp:yyyy-MM-dd HH:mm:ss} " +
                                                "{Level:u3} " +
                                                "{SourceContext} " +
                                                "{Message} " +
                                                "{Exception}" +
                                                "{NewLine}";
            
            config
                .WriteTo.Console(
                    outputTemplate: outputTemplateString,
                    theme: AnsiConsoleTheme.Sixteen
                )
                .WriteTo.File(
                    path: $"{AppContext.BaseDirectory}/logs/{DateTime.Now:yyyy-MM-dd}/log.txt",
                    outputTemplate: outputTemplateString,
                    rollingInterval: RollingInterval.Day,
                    rollOnFileSizeLimit: true,
                    fileSizeLimitBytes: 1L * 1024 * 1024 * 1024,
                    encoding: Encoding.UTF8,
                    retainedFileCountLimit: 31
                )
                .MinimumLevel.Information()
                .MinimumLevel.Override("Default", LogEventLevel.Information)
                .MinimumLevel.Override("Microsoft", LogEventLevel.Information)
                .MinimumLevel.Override("Default.Hosting.Lifetime", LogEventLevel.Information)
                .Enrich.FromLogContext();
        });
    }
}