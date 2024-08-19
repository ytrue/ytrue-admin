using Ytrue.Infrastructures.Extensions;
using Ytrue.Infrastructures.Extensions.Yaml;
using Ytrue.Infrastructures.Interceptors.Middlewares;
using Ytrue.Repositories;


var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers();
builder.Services.AddHttpContextAccessor();

builder.AddAutoFacService();
builder.AddSerilogService();
//builder.AddEfCoreDbContextService<ApplicationDbContext>();
builder.AddSqlSugar();
builder.AddJwtSecurityService();
builder.AddMvcService();
// 使用yml作为配置文件
builder.Configuration.AddYamlFile("application.yml");
builder.Configuration.AddYamlFile($"application.{builder.Environment.EnvironmentName}.yml");
// 注册事件 MediatR
builder.Services.AddMediatR(cfg => cfg.RegisterServicesFromAssemblyContaining<Program>());


// 构建
var app = builder.Build();

// 添加404异常处理器
app.UseMiddleware<NotFoundMiddleware>();
// 静态文件
// app.UseStaticFiles();
// 跨域中间件的顺序不能乱
app.UseCors(config =>
{
    config.AllowAnyHeader();
    // 这里需要改成WithOrigins()方法,填写你实际的客户端地址
    config.SetIsOriginAllowed(s => true);
    config.AllowAnyMethod();
    // 主要是为了允许signalR跨域通讯
    config.AllowCredentials();
});

// 路由
app.UseRouting();
// 认证
app.UseAuthentication();
// 授权
app.UseAuthorization();
// 路由映射
app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();