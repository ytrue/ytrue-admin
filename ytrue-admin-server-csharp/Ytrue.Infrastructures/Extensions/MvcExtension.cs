using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using Ytrue.Infrastructures.Interceptors.Filters;
using Ytrue.Infrastructures.Interceptors.Filters.Operationlog;

namespace Ytrue.Infrastructures.Extensions;

public static class MvcExtension
{
    /// <summary>
    /// 添加自定义的 MVC 过滤器配置。
    /// </summary>
    /// <param name="builder">MVC 构建器实例。</param>
    public static void AddMvcService(this WebApplicationBuilder builder)
    {
        builder.Services.AddMvc(p =>
            {
                // 全局异常处理过滤器
                p.Filters.Add<GlobalExceptionFilter>();
                // 模型验证处理器
                p.Filters.Add<ValidateModelActionFilter>();
                // 操作日志
                p.Filters.Add<OperationLogFilter>();
            })
            .ConfigureApiBehaviorOptions(p =>
            {
                // 关闭自带的模型验证
                p.SuppressModelStateInvalidFilter = true;
            })
            .AddNewtonsoftJson(p =>
            {
                //数据格式首字母小写 不使用驼峰
                p.SerializerSettings.ContractResolver = new CamelCasePropertyNamesContractResolver();
                //不使用驼峰样式的key
                //p.SerializerSettings.ContractResolver = new DefaultContractResolver();
                //忽略循环引用
                p.SerializerSettings.ReferenceLoopHandling = ReferenceLoopHandling.Ignore;
                //设置时间格式
                p.SerializerSettings.DateFormatString = "yyyy-MM-dd HH:mm:ss";
            });
    }
}