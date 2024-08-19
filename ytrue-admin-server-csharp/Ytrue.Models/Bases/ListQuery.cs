using System.Linq.Expressions;

namespace Ytrue.Models.Bases;

public abstract class ListQuery<T> where T : class
{
    /// <summary>
    /// 获取查询表达式
    /// </summary>
    /// <typeparam name="T"></typeparam>
    /// <exception cref="NotImplementedException"></exception>
    /// <returns></returns>
    public virtual Expression<Func<T, bool>> GetQueryExpression()
    {
        throw new NotImplementedException();
    }
}