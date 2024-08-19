using System.Net;
using System.Net.Mime;
using System.Security.Claims;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Options;
using Ytrue.Infrastructures.Configurations.Settings;
using Ytrue.Infrastructures.Extensions;
using Ytrue.Infrastructures.Response;
using Ytrue.Infrastructures.Utilities;

namespace Ytrue.Infrastructures.Security;

/// <summary>
/// jwt 认证处理器
/// </summary>
public class JwtAuthenticationHandler : IAuthenticationHandler
{
    private HttpContext _context = null!;

    private AuthenticationScheme _scheme = null!;

    private readonly JwtHelper _jwtHelper;

    private readonly IOptions<SecurityConfig> _options;


    /// <summary>
    /// jwt Scheme
    /// </summary>
    public const string JwtAuthenticationScheme = "JwtAuthenticationScheme";

    /// <summary>
    /// 认证的key
    /// </summary>
    private readonly string _authorizationKey;

    /// <summary>
    ///  token前缀
    /// </summary>
    private readonly string _tokenPrefix;


    /// <summary>
    /// 构造
    /// </summary>
    /// <param name="jwtUtil"></param>
    /// <param name="options"></param>
    public JwtAuthenticationHandler(
        JwtHelper jwtUtil,
        IOptions<SecurityConfig> options
    )
    {
        _jwtHelper = jwtUtil;
        _options = options;

        _authorizationKey = _options.Value.AuthorizationHeaderParameterName;
        _tokenPrefix = _options.Value.JwtSetting.JwtSeparator;
    }


    /// <summary>
    /// 在处理程序初始化时调用此方法，用于执行初始化逻辑
    /// </summary>
    /// <param name="scheme"></param>
    /// <param name="context"></param>
    /// <returns></returns>
    /// <exception cref="NotImplementedException"></exception>
    public Task InitializeAsync(AuthenticationScheme scheme, HttpContext context)
    {
        _scheme = scheme;
        _context = context;
        return Task.CompletedTask;
    }

    /// <summary>
    ///  执行身份验证的核心方法
    /// </summary>
    /// <returns></returns>
    /// <exception cref="NotImplementedException"></exception>
    public Task<AuthenticateResult> AuthenticateAsync()
    {
        // 检查控制器和动作方法上是否存在特定的授权要求
        if (ShouldBypassAuthenticationCheck())
        {
            // 如果存在特定的授权要求，可以直接放行
            var empty = new ClaimsPrincipal(new ClaimsIdentity(Array.Empty<Claim>(), _scheme.Name));
            return Task.FromResult(AuthenticateResult.Success(new AuthenticationTicket(empty, _scheme.Name)));
        }


        _context.Request.Headers.TryGetValue(_authorizationKey, out var authHeader);
        if (string.IsNullOrEmpty(authHeader))
        {
            return Task.FromResult(AuthenticateResult.Fail("未提供身份验证信息"));
        }

        // 处理 Bearer Token
        var token = authHeader.ToString();
        if (!string.IsNullOrEmpty(_tokenPrefix))
        {
            if (!token.StartsWith(_tokenPrefix))
            {
                return Task.FromResult(AuthenticateResult.Fail("无效的身份验证头部"));
            }

            // 获取后面的内容
            token = token.Substring(_tokenPrefix.Length);
        }

        if (!_jwtHelper.ValidateToken(token))
        {
            return Task.FromResult(AuthenticateResult.Fail("Token 验证失败"));
        }

        // 获取 JWT 载荷中的声明，例如用户信息     
        var claims = _jwtHelper.GetClaims(token);
        var enumerable = claims as Claim[] ?? claims.ToArray();
        // 创建 ClaimsPrincipal 对象
        var identity = new ClaimsIdentity(enumerable, _scheme.Name);
        var principal = new ClaimsPrincipal(identity);

        // 返回成功的身份验证结果
        var ticket = new AuthenticationTicket(principal, _scheme.Name);
        return Task.FromResult(AuthenticateResult.Success(ticket));
    }

    /// <summary>
    /// 是否绕过验证
    /// </summary>
    /// <returns></returns>
    private bool ShouldBypassAuthenticationCheck()
    {
        //var allowAnonymousPaths = _configuration.GetSection("AllowAnonymousPaths").Get<AllowAnonymousPathsConfig>();
        var allowAnonymousPaths = _options.Value.AllowAnonymousPath;
        // 检查请求路径和方法是否在不需要认证的路径列表中
        var requestPath = _context.Request.Path;
        var requestMethod = _context.Request.Method;


        // AllowAnonymousPathsConfig 匹配
        if (allowAnonymousPaths.Match(requestPath, requestMethod))
        {
            return true;
        }

        // 下面是注解的
        var endpoint = _context.GetEndpoint();
        if (endpoint == null)
        {
            return false;
        }

        // GetOrderedMetadata<IAuthorizeData>() 方法的作用是从 Endpoint 的元数据集合中获取实现了 IAuthorizeData 接口的元数据对象。
        // 在 ASP.NET Core 中，授权要求（Authorization Requirements）可以通过 [Authorize] 或者更具体的授权特性（如 [AllowAnonymous]、[Authorize(Roles = "Admin")] 等）来标记在控制器和动作方法上。
        // 当你调用 GetOrderedMetadata<IAuthorizeData>() 时，它会返回所有与当前 Endpoint 相关的授权要求（AuthorizeData），并且这些授权要求会按照它们在代码中声明的顺序排序。
        // 检查动作方法上是否存在特定的注解或标记
        var actionAttributes = endpoint.Metadata.GetOrderedMetadata<IAuthorizeData>();
        return actionAttributes.Any(attribute => attribute.GetType() == typeof(AllowAnonymousAttribute));
    }


    /// <summary>
    /// 当请求需要进行身份验证但未提供有效凭据时，会调用此方法
    /// </summary>
    /// <param name="properties"></param>
    /// <returns></returns>
    /// <exception cref="NotImplementedException"></exception>
    public Task ChallengeAsync(AuthenticationProperties? properties)
    {
        // 通常情况下应该从 AuthenticateAsync 方法获取结果
        var authenticateResult = AuthenticateAsync().Result;

        var errorMessage = "未登录";
        if (authenticateResult.Failure != null)
        {
            errorMessage = authenticateResult.Failure.Message;
        }

        _context.Response.StatusCode = Convert.ToInt32(HttpStatusCode.OK);
        // 设置响应格式是json
        _context.Response.ContentType = MediaTypeNames.Application.Json;
        // 响应的json字符串
        return _context.Response.WriteAsync(
            JsonUtil.SerializeObject(
                ServerResponseEntity<string>.FailResult(ServerResponseEnum.Unauthorized.GetDescription(),
                    errorMessage)));
    }

    /// <summary>
    /// 当用户虽然已经认证，但由于权限不足而无法访问资源时，会调用此方法
    /// </summary>
    /// <param name="properties"></param>
    /// <returns></returns>
    /// <exception cref="NotImplementedException"></exception>
    public Task ForbidAsync(AuthenticationProperties? properties)
    {
        _context.Response.StatusCode = Convert.ToInt32(HttpStatusCode.OK);
        // 设置响应格式是json
        _context.Response.ContentType = MediaTypeNames.Application.Json;
        // 响应的json字符串

        return _context.Response.WriteAsync(
            JsonUtil.SerializeObject(
                ServerResponseEntity<string>.FailResult(ServerResponseEnum.Forbidden.GetDescription(), "没权限")));
    }
}