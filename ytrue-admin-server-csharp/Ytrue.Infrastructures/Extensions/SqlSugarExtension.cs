using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using SqlSugar;
using Ytrue.Infrastructures.Utilities;

namespace Ytrue.Infrastructures.Extensions;

public static class SqlSugarExtension
{
    public static void AddSqlSugar(this WebApplicationBuilder builder)
    {
        //注册SqlSugar
        builder.Services.AddSingleton<ISqlSugarClient>(s =>
        {
            var connectionString = builder.Configuration.GetConnectionString("Mysql");
            var sqlSugar = new SqlSugarScope(new ConnectionConfig()
                {
                    DbType = DbType.MySql,
                    ConnectionString = connectionString,
                    IsAutoCloseConnection = true,
                },
                db =>
                {
                    //调式代码 用来打印SQL 
                    db.Aop.OnLogExecuting = (sql, pars) =>
                    {
                        if (!builder.Environment.IsDevelopment()) return;
                        LoggerUtil<SqlSugarClient>.LogInformation(sql);
                    };
                });
            return sqlSugar;
        });
    }
}