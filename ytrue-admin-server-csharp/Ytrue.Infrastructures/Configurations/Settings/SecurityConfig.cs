using Microsoft.IdentityModel.Tokens;
using Ytrue.Infrastructures.Utilities;

namespace Ytrue.Infrastructures.Configurations.Settings;

/// <summary>
/// 安全配置
/// </summary>
public class SecurityConfig
{
    /// <summary>
    /// 授权头参数名称
    /// </summary>
    public string AuthorizationHeaderParameterName { get; set; } = "Authorization";

    /// <summary>
    /// jwt的
    /// </summary>
    public JwtSettingConfig JwtSetting { get; set; }

    /// <summary>
    /// 匿名的
    /// </summary>
    public AllowAnonymousPathConfig AllowAnonymousPath { get; set; }


    public class AllowAnonymousPathConfig
    {
        /// <summary>
        /// 路径和允许的请求方法列表
        /// </summary>
        public List<AllowAllowAnonymousPathItem>? Endpoints { get; set; }


        /// <summary>
        /// 允许匿名访问的路径项
        /// </summary>
        public class AllowAllowAnonymousPathItem
        {
            /// <summary>
            /// 路径 为null不处理
            /// </summary>
            public string? Path { get; set; }

            /// <summary>
            /// 允许的请求方法列表 为null不处理
            ///    "*" 表示所有方法，多个使用 , 隔开
            /// </summary>
            public string? Methods { get; set; }
        }


        /// <summary>
        /// AllowAnonymousPathsConfig 匹配
        /// </summary>
        /// <param name="requestPath"></param>
        /// <param name="requestMethod"></param>
        /// <returns></returns>
        public bool Match(string requestPath, string requestMethod)
        {
            var allowAnonymousPaths = this;

            // 不为空才处理
            if (allowAnonymousPaths.Endpoints == null)
            {
                return false;
            }

            foreach (var item in allowAnonymousPaths.Endpoints)
            {
                // path or method为空不处理
                if (string.IsNullOrEmpty(item.Path) || string.IsNullOrEmpty(item.Methods))
                {
                    continue;
                }

                var methodArray = item.Methods.Split(',');
                var pathMatch = MatchUtil.IsPathMatch(item.Path, requestPath);

                var methodMatch = (item.Methods.Equals("*") || methodArray.Any(m =>
                    m.Trim().Equals(requestMethod, StringComparison.OrdinalIgnoreCase)));

                if (pathMatch && methodMatch)
                {
                    return true;
                }
            }

            return false;
        }
    }


    /// <summary>
    /// jwt 配置
    /// </summary>
    /// <summary>
    /// jwt 配置
    /// </summary>
    public class JwtSettingConfig
    {
        private const string DefaultJwtAlg = SecurityAlgorithms.HmacSha256;

        /// <summary>
        /// 获取或设置用于JWT加密和解密的密钥。
        /// </summary>
        public string TokenSecret { get; set; } = "";

        /// <summary>
        /// 获取或设置JWT令牌的过期时间（毫秒为单位）。
        /// </summary>
        public long TokenExpireTime { get; set; } = 30 * 60 * 1000L;

        /// <summary>
        /// 获取或设置在此时间之前JWT令牌无效的时间（毫秒为单位）。
        /// </summary>
        public long BeforeTime { get; set; } = 0L;

        /// <summary>
        /// 获取或设置JWT加密算法。可以取值为HS256、HS384、HS512。
        /// </summary>
        public string JwtAlg { get; set; } = DefaultJwtAlg;

        /// <summary>
        /// 获取或设置生成JWT令牌时使用的分隔符。
        /// </summary>
        public string JwtSeparator { get; set; } = "Bearer#";

        /// <summary>
        /// 获取或设置JWT令牌的签发者。
        /// </summary>
        public string Issuer { get; set; } = "";

        /// <summary>
        /// 获取或设置JWT令牌的预期接收者。
        /// </summary>
        public string Audience { get; set; } = "";
    }
}