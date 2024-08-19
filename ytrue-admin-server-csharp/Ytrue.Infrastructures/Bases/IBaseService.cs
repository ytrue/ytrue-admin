using System.Linq.Expressions;
using SqlSugar;

namespace Ytrue.Infrastructures.Bases
{
    /// <summary>
    /// 基础服务接口，提供常见的 CRUD 操作方法。
    /// </summary>
    /// <typeparam name="T">实体类型</typeparam>
    public interface IBaseService<T> where T : class, new()
    {
        /// <summary>
        /// 添加单条数据。
        /// </summary>
        /// <param name="model">要添加的实体对象</param>
        /// <returns>操作影响的行数</returns>
        Task<int> AddAsync(T model);

        /// <summary>
        /// 批量添加数据。
        /// </summary>
        /// <param name="list">要添加的实体对象列表</param>
        /// <returns>操作影响的行数</returns>
        Task<int> AddRangeAsync(List<T> list);

        /// <summary>
        /// 添加单条数据，并返回生成的自增主键值。
        /// </summary>
        /// <param name="model">要添加的实体对象</param>
        /// <returns>生成的自增主键值</returns>
        Task<int> AddReturnIdentityAsync(T model);

        /// <summary>
        /// 添加单条数据，并返回插入后的实体对象。
        /// </summary>
        /// <param name="model">要添加的实体对象</param>
        /// <returns>插入后的实体对象</returns>
        Task<T> AddReturnEntityAsync(T model);

        /// <summary>
        /// 添加单条数据，仅包含指定列。
        /// </summary>
        /// <param name="model">要添加的实体对象</param>
        /// <param name="columns">要添加的列名</param>
        /// <returns>操作影响的行数</returns>
        Task<int> AddColumnsAsync(T model, string[] columns);

        /// <summary>
        /// 添加单条数据，但忽略指定列。
        /// </summary>
        /// <param name="model">要添加的实体对象</param>
        /// <param name="ignoreColumns">要忽略的列名</param>
        /// <returns>操作影响的行数</returns>
        Task<int> AddColumnsByIgnoreColumnsAsync(T model, string[] ignoreColumns);

        /// <summary>
        /// 根据主键删除记录。
        /// </summary>
        /// <param name="key">主键值</param>
        /// <returns>操作影响的行数</returns>
        Task<int> RemoveByIdAsync(object key);

        /// <summary>
        /// 根据主键批量删除记录。
        /// </summary>
        /// <typeparam name="TS">主键类型</typeparam>
        /// <param name="keys">主键值列表</param>
        /// <returns>操作影响的行数</returns>
        Task<int> RemoveByIdsAsync<TS>(TS[] keys);

        /// <summary>
        /// 根据条件删除记录。
        /// </summary>
        /// <param name="where">删除条件表达式</param>
        /// <returns>操作影响的行数</returns>
        Task<int> RemoveByConditionAsync(Expression<Func<T, bool>> where);

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
        /// <param name="expression">更新条件表达式，例如 t => t.Id > 5</param>
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
        Task<T?> GetByIdAsync(object id);

        /// <summary>
        /// 根据条件查询单条记录。
        /// </summary>
        /// <param name="where">查询条件表达式</param>
        /// <returns>查询到的实体对象，如果不存在则为 <c>null</c></returns>
        Task<T?> GetByConditionAsync(Expression<Func<T, bool>> where);

        /// <summary>
        /// 判断是否存在符合指定条件的记录。
        /// </summary>
        /// <param name="where">查询条件表达式</param>
        /// <returns><c>true</c> 如果存在符合条件的记录；否则 <c>false</c></returns>
        Task<bool> IsAnyByConditionAsync(Expression<Func<T, bool>> where);

        /// <summary>
        /// 分页查询
        /// </summary>
        /// <param name="pageIndex"></param>
        /// <param name="pageSize"></param>
        /// <param name="totalCount"></param>
        /// <param name="where"></param>
        /// <param name="order"></param>
        /// <returns></returns>
        Task<List<T>> ListPageByConditionAsync(
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
        Task<List<T>> ListByConditionAsync(Expression<Func<T, bool>> where,
            Expression<Func<T, object>>? order = null);
    }
}