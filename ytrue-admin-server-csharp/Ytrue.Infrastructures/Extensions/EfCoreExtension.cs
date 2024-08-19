using Microsoft.AspNetCore.Builder;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

namespace Ytrue.Infrastructures.Extensions;

/// <summary>
/// 扩展方法集中定义 Entities Framework Core 上下文的配置。
/// </summary>
public static class EfCoreExtension
{
    /// <summary>
    /// 添加自定义的 Entities Framework Core 上下文配置。
    /// </summary>
    /// <param name="builder"></param>
    public static void AddEfCoreDbContextService<T>(this WebApplicationBuilder builder) where T : DbContext
    {
        builder.Services.AddDbContextPool<T>(p =>
        {
            var connectionString = builder.Configuration.GetConnectionString("Mysql");
            var mySqlServerVersion = new MySqlServerVersion("8.0.36");
            p.UseMySql(connectionString, mySqlServerVersion);

            // 开发环境才打印
            if (!builder.Environment.IsDevelopment()) return;
            // 在控制台打印SQL
            p.LogTo(Console.WriteLine, LogLevel.Information);
            // 打出SQL中的参数值
            p.EnableSensitiveDataLogging();
        }, poolSize: 150);
    }
}