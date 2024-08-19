using Autofac;
using Autofac.Extensions.DependencyInjection;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.Hosting;
using Ytrue.Infrastructures.Configurations.Autofac;
 

namespace Ytrue.Infrastructures.Extensions;

/// <summary>
/// 扩展方法集中定义 Ioc 容器的配置。
/// </summary>
public static class AutoFacExtension
{
    /// <summary>
    /// 配置 Ioc 容器。
    /// </summary>asas
    /// <param name="builder"></param>
    public static void AddAutoFacService(this WebApplicationBuilder builder)
    {
        builder.Host.UseServiceProviderFactory(new AutofacServiceProviderFactory());
        builder.Host.ConfigureContainer<ContainerBuilder>(containerBuilder =>
        {
            containerBuilder.RegisterModule(new AutofacModuleRegister());
        });
    }
}