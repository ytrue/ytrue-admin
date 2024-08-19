using System.Linq.Expressions;
using SqlSugar;

namespace Ytrue.Infrastructures.Bases;

/// <summary>
/// 基础仓储接口，提供常见的 CRUD 操作方法。
/// </summary>
/// <typeparam name="T">实体类型</typeparam>
public interface IBaseRepository<T> where T : class, new()
{
    /// <summary>
    /// 获取一个用于查询的 <see cref="ISugarQueryable{T}"/> 实例。
    /// </summary>
    /// <returns>用于查询的 <see cref="ISugarQueryable{T}"/> 实例</returns>
    ISugarQueryable<T> GetSugarQueryable();

    /// <summary>
    /// 获取 db Ado
    /// </summary>
    /// <returns></returns>
    IAdo GetAdo();


    /// <summary>
    /// 获取db
    /// </summary>
    /// <returns></returns>
    ISqlSugarClient getDb();

    /// <summary>
    /// 插入一条记录。
    /// </summary>
    /// <param name="model">要插入的实体对象</param>
    /// <returns>操作影响的行数</returns>
    Task<int> InsertAsync(T model);

    /// <summary>
    /// 批量插入记录。
    /// </summary>
    /// <param name="list">要插入的实体对象列表</param>
    /// <returns>操作影响的行数</returns>
    Task<int> InsertRangeAsync(List<T> list);

    /// <summary>
    /// 插入一条记录，并返回生成的自增主键值。
    /// </summary>
    /// <param name="model">要插入的实体对象</param>
    /// <returns>生成的自增主键值</returns>
    Task<int> InsertReturnIdentityAsync(T model);

    /// <summary>
    /// 插入一条记录，并返回插入后的实体对象。
    /// </summary>
    /// <param name="model">要插入的实体对象</param>
    /// <returns>插入后的实体对象</returns>
    Task<T> InsertReturnEntityAsync(T model);

    /// <summary>
    /// 插入一条记录，仅包含指定列。
    /// </summary>
    /// <param name="model">要插入的实体对象</param>
    /// <param name="columns">要插入的列名</param>
    /// <returns>操作影响的行数</returns>
    Task<int> InsertColumnsAsync(T model,  string[] columns);

    /// <summary>
    /// 插入一条记录，但忽略指定的列。
    /// </summary>
    /// <param name="model">要插入的实体对象</param>
    /// <param name="ignoreColumns">要忽略的列名</param>
    /// <returns>操作影响的行数</returns>
    Task<int> InsertColumnsByIgnoreColumnsAsync(T model,  string[] ignoreColumns);

    /// <summary>
    /// 根据主键删除一条记录。
    /// </summary>
    /// <param name="id">主键值</param>
    /// <returns>操作影响的行数</returns>
    Task<int> DeleteByIdAsync(object id);

    /// <summary>
    /// 根据主键批量删除记录。
    /// </summary>
    /// <typeparam name="TS">主键类型</typeparam>
    /// <param name="keys">主键值列表</param>
    /// <returns>操作影响的行数</returns>
    Task<int> DeleteByIdsAsync<TS>( TS[] keys);

    /// <summary>
    /// 根据条件删除记录。
    /// </summary>
    /// <param name="where">删除条件表达式</param>
    /// <returns>操作影响的行数</returns>
    Task<int> DeleteByConditionAsync(Expression<Func<T, bool>> where);

    /// <summary>
    /// 根据主键更新记录。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作影响的行数</returns>
    Task<int> UpdateByIdAsync(T model, bool ignoreAllNullColumns = true);

    /// <summary>
    /// 批量更新记录。
    /// </summary>
    /// <param name="list">包含更新数据的实体对象列表</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作影响的行数</returns>
    Task<int> UpdateRangeAsync(List<T> list, bool ignoreAllNullColumns = true);

    /// <summary>
    /// 根据条件更新记录。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="expression">指定列的条件表达式，例如 t => t.Id > 5</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作影响的行数</returns>
    Task<int> UpdateByConditionAsync(T model, Expression<Func<T, object>> expression,
        bool ignoreAllNullColumns = true);

    /// <summary>
    /// 根据主键更新指定列。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="columns">要更新的列名</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作影响的行数</returns>
    Task<int> UpdateColumnsByIdAsync(T model, string[] columns, bool ignoreAllNullColumns = false);

    /// <summary>
    /// 根据主键更新指定列。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="columns">要更新的列的表达式</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作影响的行数</returns>
    Task<int> UpdateColumnsByIdAsync(T model, Expression<Func<T, object>> columns,
        bool ignoreAllNullColumns = false);

    /// <summary>
    /// 根据主键更新记录，并忽略指定列。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="columns">要忽略的列名</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作影响的行数</returns>
    Task<int> UpdateByIdWithIgnoredColumnsAsync(T model, string[] columns, bool ignoreAllNullColumns = false);

    /// <summary>
    /// 根据主键更新记录，并忽略指定列。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="columns">要忽略的列的表达式</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作影响的行数</returns>
    Task<int> UpdateByIdWithIgnoredColumnsAsync(T model, Expression<Func<T, object>> columns,
        bool ignoreAllNullColumns = false);


    /// <summary>
    /// 根据主键查询单条记录。
    /// </summary>
    /// <param name="id">主键值</param>
    /// <returns>查询到的实体对象，如果不存在则为 <c>null</c></returns>
    Task<T?> SelectByIdAsync(object id);

    /// <summary>
    /// 根据条件查询单条记录。
    /// </summary>
    /// <param name="where">查询条件表达式</param>
    /// <returns>查询到的实体对象，如果不存在则为 <c>null</c></returns>
    Task<T?> SelectOneByConditionAsync(Expression<Func<T, bool>> where);

    /// <summary>
    /// 判断是否存在符合指定条件的记录。
    /// </summary>
    /// <param name="where">查询条件表达式</param>
    /// <returns><c>true</c> 如果存在符合条件的记录；否则 <c>false</c></returns>
    Task<bool> SelectIsAnyByConditionAsync(Expression<Func<T, bool>> where);

    /// <summary>
    /// 分页查询
    /// </summary>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <param name="totalCount"></param>
    /// <param name="where"></param>
    /// <param name="order"></param>
    /// <returns></returns>
    Task<List<T>> SelectPageByConditionAsync(
        int pageIndex,
        int pageSize,
        RefAsync<int> totalCount,
        Expression<Func<T, bool>>? where = null,
        Expression<Func<T, object>>? order = null
    );


    /// <summary>
    /// 分页查询
    /// </summary>
    /// <param name="where"></param>
    /// <param name="order"></param>
    /// <returns></returns>
    Task<List<T>> SelectListByConditionAsync(Expression<Func<T, bool>> where,
        Expression<Func<T, object>>? order = null);
}