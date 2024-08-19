using System.Linq.Expressions;
using SqlSugar;

namespace Ytrue.Infrastructures.Bases;

/// <summary>
/// 基础服务实现，提供对仓储层的 CRUD 操作的统一封装。
/// </summary>
/// <typeparam name="T">实体类型</typeparam>
public abstract class BaseService<T> : IBaseService<T> where T : class, new()
{
    protected IBaseRepository<T> BaseRepository { get; }

    protected IAdo Ado { get; }

    /// <summary>
    /// 初始化一个新的 <see cref="BaseService{T}"/> 实例。
    /// </summary>
    /// <param name="repository">用于执行数据操作的仓储</param>
    protected BaseService(IBaseRepository<T> repository)
    {
        BaseRepository = repository;
        Ado = repository.GetAdo();
    }

    /// <summary>
    /// 增加单条数据到仓储。
    /// </summary>
    /// <param name="model">要添加的实体对象</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> AddAsync(T model)
    {
        return BaseRepository.InsertAsync(model);
    }

    /// <summary>
    /// 增加多条数据到仓储。
    /// </summary>
    /// <param name="list">要添加的实体集合</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> AddRangeAsync(List<T> list)
    {
        return BaseRepository.InsertRangeAsync(list);
    }

    /// <summary>
    /// 添加单条数据到仓储，并返回自增列的值。
    /// </summary>
    /// <param name="model">要添加的实体对象</param>
    /// <returns>操作结果的 Task 对象，返回自增列的值</returns>
    public Task<int> AddReturnIdentityAsync(T model)
    {
        return BaseRepository.InsertReturnIdentityAsync(model);
    }

    /// <summary>
    /// 增加单条数据到仓储，并返回实体对象。
    /// </summary>
    /// <param name="model">要添加的实体对象</param>
    /// <returns>操作结果的 Task 对象，返回添加后的实体对象</returns>
    public Task<T> AddReturnEntityAsync(T model)
    {
        return BaseRepository.InsertReturnEntityAsync(model);
    }

    /// <summary>
    /// 只添加指定列的数据到仓储。
    /// </summary>
    /// <param name="model">要添加的实体对象</param>
    /// <param name="columns">要添加的列名</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> AddColumnsAsync(T model,  string[] columns)
    {
        return BaseRepository.InsertColumnsAsync(model, columns);
    }

    /// <summary>
    /// 不插入指定列的数据到仓储。
    /// </summary>
    /// <param name="model">要添加的实体对象</param>
    /// <param name="ignoreColumns">要忽略的列名</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> AddColumnsByIgnoreColumnsAsync(T model,  string[] ignoreColumns)
    {
        return BaseRepository.InsertColumnsByIgnoreColumnsAsync(model, ignoreColumns);
    }

    /// <summary>
    /// 根据主键删除数据。
    /// </summary>
    /// <param name="key">主键值</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> RemoveByIdAsync(object key)
    {
        return BaseRepository.DeleteByIdAsync(key);
    }

    /// <summary>
    /// 根据多个主键删除数据。
    /// </summary>
    /// <typeparam name="TS">主键类型</typeparam>
    /// <param name="keys">主键值数组</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> RemoveByIdsAsync<TS>( TS[] keys)
    {
        return BaseRepository.DeleteByIdsAsync(keys);
    }

    /// <summary>
    /// 根据条件删除数据。
    /// </summary>
    /// <param name="where">删除条件</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> RemoveByConditionAsync(Expression<Func<T, bool>> where)
    {
        return BaseRepository.DeleteByConditionAsync(where);
    }

    /// <summary>
    /// 根据主键更新数据。
    /// </summary>
    /// <param name="model">要更新的实体对象</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> UpdateByIdAsync(T model, bool ignoreAllNullColumns = true)
    {
        return BaseRepository.UpdateByIdAsync(model, ignoreAllNullColumns);
    }

    /// <summary>
    /// 更新多个实体对象的数据。
    /// </summary>
    /// <param name="list">要更新的实体集合</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> UpdateRangeAsync(List<T> list, bool ignoreAllNullColumns = true)
    {
        return BaseRepository.UpdateRangeAsync(list, ignoreAllNullColumns);
    }

    /// <summary>
    /// 根据指定列条件更新数据。
    /// </summary>
    /// <param name="model">要更新的实体对象</param>
    /// <param name="expression">列条件表达式</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> UpdateByConditionAsync(T model, Expression<Func<T, object>> expression,
        bool ignoreAllNullColumns = true)
    {
        return BaseRepository.UpdateByConditionAsync(model, expression, ignoreAllNullColumns);
    }

    /// <summary>
    /// 根据主键更新指定列的数据。
    /// </summary>
    /// <param name="model">要更新的实体对象</param>
    /// <param name="columns">要更新的列名</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> UpdateColumnsByIdAsync(T model, string[] columns, bool ignoreAllNullColumns = false)
    {
        return BaseRepository.UpdateColumnsByIdAsync(model, columns, ignoreAllNullColumns);
    }

    /// <summary>
    /// 根据主键更新指定列的数据。
    /// </summary>
    /// <param name="model">要更新的实体对象</param>
    /// <param name="columns">列条件表达式</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> UpdateColumnsByIdAsync(T model, Expression<Func<T, object>> columns,
        bool ignoreAllNullColumns = false)
    {
        return BaseRepository.UpdateColumnsByIdAsync(model, columns, ignoreAllNullColumns);
    }

    /// <summary>
    /// 根据主键更新数据，并忽略指定列。
    /// </summary>
    /// <param name="model">要更新的实体对象</param>
    /// <param name="columns">要忽略的列名</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> UpdateByIdWithIgnoredColumnsAsync(T model, string[] columns,
        bool ignoreAllNullColumns = false)
    {
        return BaseRepository.UpdateByIdWithIgnoredColumnsAsync(model, columns, ignoreAllNullColumns);
    }

    /// <summary>
    /// 根据主键更新数据，并忽略指定列。
    /// </summary>
    /// <param name="model">要更新的实体对象</param>
    /// <param name="columns">列条件表达式</param>
    /// <param name="ignoreAllNullColumns">是否忽略所有空值列</param>
    /// <returns>操作结果的 Task 对象，返回影响的行数</returns>
    public Task<int> UpdateByIdWithIgnoredColumnsAsync(T model, Expression<Func<T, object>> columns,
        bool ignoreAllNullColumns = false)
    {
        return BaseRepository.UpdateByIdWithIgnoredColumnsAsync(model, columns, ignoreAllNullColumns);
    }

    /// <summary>
    /// 根据主键获取单条数据。
    /// </summary>
    /// <param name="id">主键值</param>
    /// <returns>操作结果的 Task 对象，返回匹配的实体对象，如果不存在则为 null</returns>
    public Task<T?> GetByIdAsync(object id)
    {
        return BaseRepository.SelectByIdAsync(id);
    }

    /// <summary>
    /// 根据条件获取单条数据。
    /// </summary>
    /// <param name="where">查询条件</param>
    /// <returns>操作结果的 Task 对象，返回匹配的实体对象，如果不存在则为 null</returns>
    public Task<T?> GetByConditionAsync(Expression<Func<T, bool>> where)
    {
        return BaseRepository.SelectOneByConditionAsync(where);
    }

    /// <summary>
    /// 检查是否存在满足条件的记录。
    /// </summary>
    /// <param name="where">查询条件</param>
    /// <returns>操作结果的 Task 对象，返回是否存在满足条件的记录</returns>
    public Task<bool> IsAnyByConditionAsync(Expression<Func<T, bool>> where)
    {
        return BaseRepository.SelectIsAnyByConditionAsync(where);
    }

    /// <summary>
    /// 分页查询
    /// </summary>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <param name="totalCount"></param>
    /// <param name="where"></param>
    /// <param name="order"></param>
    /// <returns></returns>
    /// <exception cref="NotImplementedException"></exception>
    public async Task<List<T>> ListPageByConditionAsync(int pageIndex, int pageSize, RefAsync<int> totalCount,
        Expression<Func<T, bool>>? where = null,
        Expression<Func<T, object>>? order = null)
    {
        return await BaseRepository.SelectPageByConditionAsync(pageIndex, pageSize, totalCount, where, order);
    }

    /// <summary>
    /// 列表条件查询
    /// </summary>
    /// <param name="where"></param>
    /// <param name="order"></param>
    /// <returns></returns>
    /// <exception cref="NotImplementedException"></exception>
    public async Task<List<T>> ListByConditionAsync(Expression<Func<T, bool>> where,
        Expression<Func<T, object>>? order = null)
    {
        return await BaseRepository.SelectListByConditionAsync(where, order);
    }
}