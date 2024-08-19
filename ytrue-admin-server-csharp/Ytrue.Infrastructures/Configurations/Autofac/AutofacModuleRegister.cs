using System.Reflection;
using Autofac;
using Microsoft.Extensions.Options;
using Ytrue.Infrastructures.Configurations.Settings;
using Ytrue.Infrastructures.Security;
using Ytrue.Infrastructures.Utilities;
using Module = Autofac.Module;

namespace Ytrue.Infrastructures.Configurations.Autofac;

public class AutofacModuleRegister : Module
{
    protected override void Load(ContainerBuilder builder)
    {
        var basePath = AppContext.BaseDirectory;

        var serviceDllFile = Path.Combine(basePath, "Ytrue.Services.dll");
        var repositoryDllFile = Path.Combine(basePath, "Ytrue.Repositories.dll");

        if (!(File.Exists(serviceDllFile) && File.Exists(repositoryDllFile)))
        {
            const string msg = "Repositories.dll和Services.dll 丢失，因为项目解耦了，所以需要先F6编译，再F5运行，请检查 bin 文件夹，并拷贝。";
            throw new Exception(msg);
        }

        // 获取 Service.dll 程序集服务，并注册
        var assemblyServices = Assembly.LoadFrom(serviceDllFile);
        //支持属性注入依赖重复
        builder.RegisterAssemblyTypes(assemblyServices).AsImplementedInterfaces().InstancePerDependency()
            .PropertiesAutowired(PropertyWiringOptions.AllowCircularDependencies);

        // 获取 Repository.dll 程序集服务，并注册
        var assemblyRepository = Assembly.LoadFrom(repositoryDllFile);
        //支持属性注入依赖重复
        builder.RegisterAssemblyTypes(assemblyRepository).AsImplementedInterfaces().InstancePerDependency()
            .PropertiesAutowired(PropertyWiringOptions.AllowCircularDependencies);

        //获取所有控制器类型并使用属性注入
        // var controllerBaseType = typeof(ControllerBase);
        // builder.RegisterAssemblyTypes(typeof(Program).Assembly)
        //     .Where(t => controllerBaseType.IsAssignableFrom(t) && t != controllerBaseType)
        //     .PropertiesAutowired();


        // 注册 JwtUtil 类，并使用 IOptions<JwtSettingsConfig> 进行构造函数注入
        builder.RegisterType<JwtHelper>()
            .AsSelf()
            .WithParameter((pi, ctx) => pi.ParameterType == typeof(IOptions<SecurityConfig>),
                (pi, ctx) => ctx.Resolve<IOptions<SecurityConfig>>())
            .SingleInstance();

        // 完成之后回调
        builder.RegisterBuildCallback(AutofacUtil.Initialize);
    }
}