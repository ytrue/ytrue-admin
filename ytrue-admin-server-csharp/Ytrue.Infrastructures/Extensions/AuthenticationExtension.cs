using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Ytrue.Infrastructures.Configurations.Settings;
using Ytrue.Infrastructures.Security;

namespace Ytrue.Infrastructures.Extensions;

public static class AuthenticationExtension
{
    /// <summary>
    ///  扩展方法集中定义身份验证和授权的配置。
    /// </summary>
    /// <param name="builder"></param>
    public static void AddJwtSecurityService(this WebApplicationBuilder builder)
    {
        // 注册jwt认证器 [Authorize(AuthenticationSchemes = "JwtAuthenticationScheme")] 默认的
        builder.Services.AddAuthentication(o =>
        {
            // 请求需要进行身份验证时，默认会使用名为 "JwtAuthenticationScheme" 的身份验证方案来验证请求
            o.DefaultAuthenticateScheme = JwtAuthenticationHandler.JwtAuthenticationScheme;
            // 设置默认的挑战方案为 "JwtAuthenticationScheme"。挑战方案用于未授权或者拒绝访问时返回的响应。
            o.DefaultChallengeScheme = JwtAuthenticationHandler.JwtAuthenticationScheme;
            o.AddScheme<JwtAuthenticationHandler>(JwtAuthenticationHandler.JwtAuthenticationScheme,
                JwtAuthenticationHandler.JwtAuthenticationScheme);
        });
        // 注册security配置
        builder.Services.Configure<SecurityConfig>(builder.Configuration.GetSection("Security"));

        // 授权的策略 [Authorize(Policy = "RequireAdmin")] 这么使用
        // builder.Services.AddAuthorization(options =>
        // {
        //     options.AddPolicy("Permission", p => p.Requirements.Add(new PermissionRequirement()));
        // });
        // 注入权限处理器
        builder.Services.AddTransient<IAuthorizationHandler, PermissionHandler>();
    }
}