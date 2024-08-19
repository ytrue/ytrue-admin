using Autofac;

namespace Ytrue.Infrastructures.Utilities;

/// <summary>
/// Autofac依赖注入服务
/// </summary>
public static class AutofacUtil
{
    private static ILifetimeScope _container = null!;

    /// <summary>
    /// 初始化Autofac依赖注入容器
    /// </summary>
    /// <param name="container">Autofac容器</param>
    public static void Initialize(ILifetimeScope container)
    {
        _container = container;
    }


    /// <summary>
    /// 获取服务(Single)
    /// </summary>
    /// <typeparam name="T">接口类型</typeparam>
    /// <returns>服务实例</returns>
    public static T GetService<T>() where T : class
    {
        if (_container == null)
        {
            throw new InvalidOperationException("Autofac container has not been initialized.");
        }

        return _container.Resolve<T>();
    }
}