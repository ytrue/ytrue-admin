using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Options;
using Ytrue.Infrastructures.Configurations.Settings;
using Ytrue.Infrastructures.Utilities;

namespace Ytrue.Infrastructures.Security;

/// <summary>
/// 权限处理器
/// </summary>
public class PermissionHandler : IAuthorizationHandler
{
    private readonly IOptions<SecurityConfig> _options;

    public PermissionHandler(IOptions<SecurityConfig> options)
    {
        _options = options;
    }

    /// <summary>
    /// 匹配权限
    /// </summary>
    /// <param name="context"></param>
    /// <returns></returns>
    public Task HandleAsync(AuthorizationHandlerContext context)
    {
        // 获取当前终结点（Endpoint）
        var httpContext = context.Resource as DefaultHttpContext;
        var endpoint = httpContext?.GetEndpoint();
        if (endpoint == null)
        {
            context.Fail();
            return Task.CompletedTask;
        }

        // 对配置 AllowAnonymousPaths 这一块的处理 
        var allowAnonymousPaths = _options.Value.AllowAnonymousPath;
        var requestPath = httpContext?.Request.Path;
        var requestMethod = httpContext?.Request.Method;
        if (requestMethod != null && requestPath != null && allowAnonymousPaths.Match(requestPath, requestMethod))
        {
            return Task.CompletedTask;
        }


        // 有AllowAnonymousAttribute直接放行，- 其实也不会走这里
        var ignoringAuthorization = endpoint
            .Metadata
            .GetOrderedMetadata<IAuthorizeData>()
            .Any(attribute => attribute.GetType() == typeof(AllowAnonymousAttribute));

        // 获取PermissionAttribute注解
        var permissionAttribute = endpoint.Metadata.GetMetadata<PermissionAttribute>();
        // 直接放行，说明方法上面没标注 PermissionAttribute
        if (permissionAttribute == null || ignoringAuthorization)
        {
            // 直接放行
            return Task.CompletedTask;
        }

        // 获取PermissionAttribute注解PermissionName
        var permissionName = permissionAttribute.PermissionName;


        // 获取认证的ClaimsIdentity
        var claimsIdentity = context.User.Identity as ClaimsIdentity;
        // 获取jwt的内容了，里面会有 authorities 这里个用户的权限
        var authoritiesClaimValue = claimsIdentity?
            .Claims
            .Where(c => c.Type.Equals("authorities"))
            .Select(c => c.Value)
            .FirstOrDefault();

        // 如果为空，说明没用权限
        if (string.IsNullOrEmpty(authoritiesClaimValue))
        {
            context.Fail();
            return Task.CompletedTask;
        }


        // 如果为空，说明没用权限
        var list = JsonUtil.DeserializeObject<List<string>>(authoritiesClaimValue);
        if (list == null)
        {
            context.Fail();
            return Task.CompletedTask;
        }

        // 没用匹配到，没用权限
        if (!list.Any(s => s.Equals(permissionName)))
        {
            context.Fail();
        }

        return Task.CompletedTask;
    }
}