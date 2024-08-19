using System.Linq.Expressions;
using SqlSugar;

namespace Ytrue.Infrastructures.Bases;

/// <summary>
/// 基础仓储实现类，提供常见的数据库操作方法。
/// </summary>
/// <typeparam name="T">实体类型</typeparam>
public abstract class BaseRepository<T> : IBaseRepository<T> where T : class, new()
{
    private readonly ISqlSugarClient _db;

    protected BaseRepository(ISqlSugarClient db)
    {
        _db = db;
    }

    /// <summary>
    /// 获取db
    /// </summary>
    /// <returns></returns>
    /// <exception cref="NotImplementedException"></exception>
    public ISqlSugarClient getDb()
    {
        return _db;
    }

    /// <summary>
    /// 获取一个用于查询的 <see cref="ISugarQueryable{T}"/> 实例。
    /// </summary>
    /// <returns>用于查询的 <see cref="ISugarQueryable{T}"/> 实例</returns>
    public ISugarQueryable<T> GetSugarQueryable()
    {
        return _db.Queryable<T>();
    }

    /// <summary>
    /// 获取ado
    /// </summary>
    /// <returns></returns>
    /// <exception cref="NotImplementedException"></exception>
    public IAdo GetAdo()
    {
        return _db.Ado;
    }

    /// <summary>
    /// 插入一条记录。
    /// </summary>
    /// <param name="model">要插入的实体对象</param>
    /// <returns>插入操作影响的行数</returns>
    public async Task<int> InsertAsync(T model)
    {
        return await _db.Insertable(model).ExecuteCommandAsync();
    }

    /// <summary>
    /// 插入多条记录。
    /// </summary>
    /// <param name="list">要插入的实体对象列表</param>
    /// <returns>插入操作影响的行数</returns>
    public async Task<int> InsertRangeAsync(List<T> list)
    {
        return await _db.Insertable(list).ExecuteCommandAsync();
    }

    /// <summary>
    /// 插入一条记录，并返回主键。
    /// </summary>
    /// <param name="model">要插入的实体对象</param>
    /// <returns>插入操作生成的主键值</returns>
    public async Task<int> InsertReturnIdentityAsync(T model)
    {
        return await _db.Insertable(model).ExecuteReturnIdentityAsync();
    }

    /// <summary>
    /// 插入一条记录，并返回实体对象。
    /// </summary>
    /// <param name="model">要插入的实体对象</param>
    /// <returns>插入后的实体对象</returns>
    public async Task<T> InsertReturnEntityAsync(T model)
    {
        return await _db.Insertable(model).ExecuteReturnEntityAsync();
    }

    /// <summary>
    /// 插入一条记录，仅包含指定列。
    /// </summary>
    /// <param name="model">要插入的实体对象</param>
    /// <param name="columns">需要插入的列名</param>
    /// <returns>插入操作影响的行数</returns>
    public async Task<int> InsertColumnsAsync(T model, string[] columns)
    {
        return await _db.Insertable(model).InsertColumns(columns).ExecuteCommandAsync();
    }

    /// <summary>
    /// 插入一条记录，但忽略指定的列。
    /// </summary>
    /// <param name="model">要插入的实体对象</param>
    /// <param name="ignoreColumns">需要忽略的列名</param>
    /// <returns>插入操作影响的行数</returns>
    public async Task<int> InsertColumnsByIgnoreColumnsAsync(T model, string[] ignoreColumns)
    {
        return await _db.Insertable(model).IgnoreColumns(ignoreColumns).ExecuteCommandAsync();
    }

    /// <summary>
    /// 根据主键删除一条记录。
    /// </summary>
    /// <param name="key">主键值</param>
    /// <returns>删除操作影响的行数</returns>
    public async Task<int> DeleteByIdAsync(object key)
    {
        return await _db.Deleteable<T>(key).ExecuteCommandAsync();
    }

    /// <summary>
    /// 根据主键批量删除记录。
    /// </summary>
    /// <param name="keys">主键值列表</param>
    /// <typeparam name="TS">主键类型</typeparam>
    /// <returns>删除操作影响的行数</returns>
    public async Task<int> DeleteByIdsAsync<TS>(TS[] keys)
    {
        return await _db.Deleteable<T>().In(keys).ExecuteCommandAsync();
    }

    /// <summary>
    /// 根据指定条件删除记录。
    /// </summary>
    /// <param name="where">条件表达式</param>
    /// <returns>删除操作影响的行数</returns>
    public async Task<int> DeleteByConditionAsync(Expression<Func<T, bool>> where)
    {
        return await _db.Deleteable<T>().Where(where).ExecuteCommandAsync();
    }

    /// <summary>
    /// 根据主键更新记录。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>更新操作影响的行数</returns>
    public async Task<int> UpdateByIdAsync(T model, bool ignoreAllNullColumns = true)
    {
        return await _db.Updateable(model).IgnoreColumns(ignoreAllNullColumns: ignoreAllNullColumns)
            .ExecuteCommandAsync();
    }

    /// <summary>
    /// 批量更新记录。
    /// </summary>
    /// <param name="list">包含更新数据的实体对象列表</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>更新操作影响的行数</returns>
    public async Task<int> UpdateRangeAsync(List<T> list, bool ignoreAllNullColumns = true)
    {
        return await _db.Updateable(list).IgnoreColumns(ignoreAllNullColumns: ignoreAllNullColumns)
            .ExecuteCommandAsync();
    }

    /// <summary>
    /// 根据指定列条件更新记录。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="expression">指定列条件表达式</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>更新操作影响的行数</returns>
    public async Task<int> UpdateByConditionAsync(T model, Expression<Func<T, object>> expression,
        bool ignoreAllNullColumns = true)
    {
        return await _db.Updateable(model).WhereColumns(expression)
            .IgnoreColumns(ignoreAllNullColumns: ignoreAllNullColumns).ExecuteCommandAsync();
    }

    /// <summary>
    /// 根据主键更新指定列。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="columns">指定的列名</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>更新操作影响的行数</returns>
    public async Task<int> UpdateColumnsByIdAsync(T model, string[] columns, bool ignoreAllNullColumns = false)
    {
        return await _db.Updateable(model).UpdateColumns(columns)
            .IgnoreColumns(ignoreAllNullColumns: ignoreAllNullColumns).ExecuteCommandAsync();
    }

    /// <summary>
    /// 根据主键更新指定列。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="columns">指定的列表达式</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>更新操作影响的行数</returns>
    public async Task<int> UpdateColumnsByIdAsync(T model, Expression<Func<T, object>> columns,
        bool ignoreAllNullColumns = false)
    {
        return await _db.Updateable(model).UpdateColumns(columns)
            .IgnoreColumns(ignoreAllNullColumns: ignoreAllNullColumns).ExecuteCommandAsync();
    }

    /// <summary>
    /// 根据主键，忽略更新指定列。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="columns">需要忽略的列名</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>更新操作影响的行数</returns>
    public async Task<int> UpdateByIdWithIgnoredColumnsAsync(T model, string[] columns,
        bool ignoreAllNullColumns = false)
    {
        return await _db.Updateable(model).IgnoreColumns(columns)
            .IgnoreColumns(ignoreAllNullColumns: ignoreAllNullColumns).ExecuteCommandAsync();
    }

    /// <summary>
    /// 根据主键，忽略更新指定列。
    /// </summary>
    /// <param name="model">包含更新数据的实体对象</param>
    /// <param name="columns">需要忽略的列表达式</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>更新操作影响的行数</returns>
    public async Task<int> UpdateByIdWithIgnoredColumnsAsync(T model, Expression<Func<T, object>> columns,
        bool ignoreAllNullColumns = false)
    {
        return await _db.Updateable(model).IgnoreColumns(columns)
            .IgnoreColumns(ignoreAllNullColumns: ignoreAllNullColumns).ExecuteCommandAsync();
    }


    /// <summary>
    /// 根据主键查询单条记录。
    /// </summary>
    /// <param name="id">主键值</param>
    /// <returns>查询到的实体对象，如果不存在则为 <c>null</c></returns>
    public async Task<T?> SelectByIdAsync(object id)
    {
        return await _db.Queryable<T>().InSingleAsync(id);
    }

    /// <summary>
    /// 根据条件查询单条记录。
    /// </summary>
    /// <param name="where">查询条件表达式</param>
    /// <returns>查询到的实体对象，如果不存在则为 <c>null</c></returns>
    public async Task<T?> SelectOneByConditionAsync(Expression<Func<T, bool>> where)
    {
        return await _db.Queryable<T>().Where(where).FirstAsync();
    }

    /// <summary>
    /// 判断是否存在符合指定条件的记录。
    /// </summary>
    /// <param name="where">查询条件表达式</param>
    /// <returns><c>true</c> 如果存在符合条件的记录；否则 <c>false</c></returns>
    public async Task<bool> SelectIsAnyByConditionAsync(Expression<Func<T, bool>> where)
    {
        return await _db.Queryable<T>().AnyAsync(where);
    }

    /// <summary>
    /// 分页条件查询
    /// </summary>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <param name="totalCount"></param>
    /// <param name="where"></param>
    /// <param name="order"></param>
    /// <returns></returns>
    public async Task<List<T>> SelectPageByConditionAsync(
        int pageIndex,
        int pageSize,
        RefAsync<int> totalCount,
        Expression<Func<T, bool>>? where = null,
        Expression<Func<T, object>>? order = null)
    {
        return await _db.Queryable<T>()
            .WhereIF(where != null, where)
            .OrderByIF(order != null, order)
            .ToPageListAsync(pageIndex, pageSize, totalCount);
    }

    /// <summary>
    /// 列表条件查询
    /// </summary>
    /// <param name="where"></param>
    /// <param name="order"></param>
    /// <returns></returns>
    public async Task<List<T>> SelectListByConditionAsync(Expression<Func<T, bool>> where,
        Expression<Func<T, object>>? order = null)
    {
        return await _db.Queryable<T>()
            .Where(where)
            .OrderByIF(order != null, order)
            .ToListAsync();
    }
}