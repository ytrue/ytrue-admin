using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using Ytrue.Infrastructures.Configurations.Settings;

namespace Ytrue.Infrastructures.Security;

public class JwtHelper
{
    /// <summary>
    /// Token加密解密的密码
    /// </summary>
    // ReSharper disable once PrivateFieldCanBeConvertedToLocalVariable
    private readonly string _tokenSecret;

    /// <summary>
    /// Token过期时间
    /// </summary>
    private readonly long _tokenExpireTime;


    /// <summary>
    /// token在什么时间之前是不可用的
    /// </summary>
    private readonly long _beforeTime;


    /// <summary>
    /// 加密类型 三个值可取 HS256  HS384  HS512
    /// </summary>
    private readonly string _jwtAlg;

    /// <summary>
    /// 签名密钥
    /// </summary>
    private readonly SecurityKey _securityKey;

    /// <summary>
    /// 添加一个前缀
    /// </summary>
    private readonly string _jwtSeparator;

    /// <summary>
    /// 设置 签发人（谁签发的）
    /// </summary>
    private readonly string? _issuer;

    /// <summary>
    /// 设置 受众(给谁用的)比如：http://www.xxx.com
    /// </summary>
    private readonly string? _audience;


    /// <summary>
    /// 构造
    /// </summary>
    /// <param name="tokenSecret"></param>
    /// <param name="tokenExpireTime"></param>
    /// <param name="beforeTime"></param>
    /// <param name="jwtAlg"></param>
    /// <param name="issuer"></param>
    /// <param name="audience"></param>
    /// <param name="jwtSeparator"></param>
    public JwtHelper(string tokenSecret,
        long tokenExpireTime = 30 * 60 * 1000L,
        long beforeTime = 0L,
        string jwtAlg = SecurityAlgorithms.HmacSha256,
        string issuer = "",
        string audience = "",
        string jwtSeparator = ""
    )
    {
        _tokenSecret = tokenSecret;
        _tokenExpireTime = tokenExpireTime;
        _beforeTime = beforeTime;
        _jwtAlg = jwtAlg;
        _jwtSeparator = jwtSeparator;
        _issuer = issuer;
        _audience = audience;


        // 使用对称安全密钥初始化SecurityKey
        _securityKey = new SymmetricSecurityKey(Encoding.ASCII.GetBytes(_tokenSecret));
    }


    /// <summary>
    /// ioc方式
    /// </summary>
    /// <param name="options"></param>
    public JwtHelper(IOptions<SecurityConfig> options)
    {
        var securityConfig = options.Value ?? throw new ArgumentNullException(nameof(options));
        var jwtSettingsConfig = securityConfig.JwtSetting;

        _tokenSecret = jwtSettingsConfig.TokenSecret;
        _tokenExpireTime = jwtSettingsConfig.TokenExpireTime;
        _beforeTime = jwtSettingsConfig.BeforeTime;
        _jwtSeparator = jwtSettingsConfig.JwtSeparator;
        _jwtAlg = jwtSettingsConfig.JwtAlg;
        _issuer = jwtSettingsConfig.Issuer;
        _audience = jwtSettingsConfig.Audience;

        // 使用对称安全密钥初始化SecurityKey
        _securityKey = new SymmetricSecurityKey(Encoding.ASCII.GetBytes(_tokenSecret));
    }


    /// <summary>
    /// 获取载荷
    /// </summary>
    /// <param name="token"></param>
    /// <returns></returns>
    public IEnumerable<Claim> GetClaims(string token)
    {
        var handler = new JwtSecurityTokenHandler();
        var jwtToken = handler.ReadJwtToken(token);
        return jwtToken.Claims;
    }


    /// <summary>
    /// 获取指定的内容
    /// </summary>
    /// <param name="claims"></param>
    /// <param name="key"></param>
    /// <returns></returns>
    public string? GetClaimValue(IEnumerable<Claim> claims, string key)
    {
        return claims.Where(c => c.Type.Equals(key)).Select(c => c.Value).FirstOrDefault();
    }

    /// <summary>
    /// 获取指定的内容
    /// </summary>
    /// <param name="token"></param>
    /// <param name="key"></param>
    /// <returns></returns>
    public string? GetClaimValue(string token, string key)
    {
        var claims = GetClaims(token);
        return claims.Where(c => c.Type.Equals(key)).Select(c => c.Value).FirstOrDefault();
    }


    /// <summary>
    /// 创建token
    /// </summary>
    /// <param name="claims"></param>
    /// <returns></returns>
    public string CreateToken(IEnumerable<Claim> claims)
    {
        var now = DateTime.Now;

        // 生成token的参数
        var jwtToken = new JwtSecurityToken(
            // 设置 签发人（谁签发的）
            issuer: _issuer,
            // 设置 受众(给谁用的)比如：http://www.xxx.com
            audience: _audience,
            // 负载
            claims: claims,
            // 什么时间之前不可使用
            notBefore: now.AddMilliseconds(_beforeTime),
            // 过期时间
            expires: now.AddMilliseconds(_tokenExpireTime),
            // 签名 = 密钥 + 签名算法
            signingCredentials: new SigningCredentials(_securityKey, _jwtAlg)
        );

        // 生成JWT Token
        var tokenHandler = new JwtSecurityTokenHandler();
        // 返回带有分隔符的JWT Token字符串
        return _jwtSeparator + tokenHandler.WriteToken(jwtToken);
    }


    /// <summary>
    /// 验证JWT token是否有效
    /// </summary>
    /// <param name="token"></param>
    /// <returns></returns>
    public bool ValidateToken(string token)
    {
        try
        {
            var handler = new JwtSecurityTokenHandler();
            // 配置 TokenValidationParameters
            var validationParameters = new TokenValidationParameters
            {
                // 验证密钥
                ValidateIssuerSigningKey = true,
                IssuerSigningKey = _securityKey,

                // 验证签发人（可选）
                ValidateIssuer = !string.IsNullOrEmpty(_issuer),
                ValidIssuer = _issuer,

                // 验证受众（可选）
                ValidateAudience = !string.IsNullOrEmpty(_audience),
                ValidAudience = _audience,

                // 验证过期时间
                ValidateLifetime = true,
                ClockSkew = TimeSpan.Zero // 设置为零，禁用默认的时间偏移量
            };

            _ = handler.ValidateToken(token, validationParameters, out _);
            // 验证通过
            return true;
        }
        catch (Exception)
        {
            // 异常情况认为 Token 无效
            return false;
        }
    }


    /// <summary>
    /// token是否过期
    /// </summary>
    /// <param name="token"></param>
    /// <returns></returns>
    public bool IsExpired(string token)
    {
        try
        {
            var handler = new JwtSecurityTokenHandler();
            var jwt = handler.ReadJwtToken(token);

            if (jwt == null)
            {
                return true;
            }

            // 获取当前时间戳 和 过期时间对比
            return jwt.Payload.Expiration < DateTimeOffset.UtcNow.ToUnixTimeSeconds();
        }
        catch
        {
            return true;
        }
    }
}