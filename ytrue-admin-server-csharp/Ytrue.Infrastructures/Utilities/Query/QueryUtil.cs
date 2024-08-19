using System.Collections;
using System.Linq.Expressions;
using System.Reflection;
using Microsoft.EntityFrameworkCore;

namespace Ytrue.Infrastructures.Utilities.Query;

public class QueryUtil
{
    public static IQueryable<TEntity> BuildWhereClauseFromQuery<TEntity>(IQueryable<TEntity> queryable, object query)
    {
        var type = query.GetType();
        // 循环所有的属性
        foreach (var propertyInfo in type.GetProperties())
        {
            // 获取QueryAttribute
            var customAttributes = propertyInfo.GetCustomAttributes(typeof(QueryAttribute));
            foreach (var customAttribute in customAttributes)
            {
                if (customAttribute is not QueryAttribute queryAttribute)
                {
                    continue;
                }

                var propertyValue = propertyInfo.GetValue(query);

                switch (propertyValue)
                {
                    case null:
                    // 如果是集合，但是元素为空不处理
                    case IEnumerable enumerable when !enumerable.Cast<object?>().Any():
                        continue;
                    default:
                    {
                        // 构建查询表达式
                        var lambda = BuildExpression<TEntity>(propertyInfo.Name, propertyInfo.GetValue(query),
                            queryAttribute!.Condition);

                        LoggerUtil<QueryUtil>.LogInformation("生成的 lambda： {lambda}", lambda.ToString());
                        // 应用查询条件
                        queryable = queryable.Where(lambda);
                        break;
                    }
                }
            }
        }

        return queryable;
    }


    /// <summary>
    /// 构建表达式数
    /// </summary>
    /// <param name="method"></param>
    /// <param name="propertyName"></param>
    /// <param name="propertyValue"></param>
    /// <typeparam name="TEntity"></typeparam>
    /// <returns></returns>
    /// <exception cref="SystemException"></exception>
    /// <exception cref="ArgumentException"></exception>
    // ReSharper disable once MemberCanBePrivate.Global
    public static Expression<Func<TEntity, bool>> BuildExpression<TEntity>(string propertyName,
        dynamic? propertyValue, QueryMethod method)
    {
        const string itemText = "item";
        var parameter = Expression.Parameter(typeof(TEntity), itemText);
        var property = Expression.Property(parameter, propertyName);

        // 验证数据
        ValidatePropertyValue(method, propertyValue);


        // ReSharper disable once ConvertSwitchStatementToSwitchExpression
        switch (method)
        {
            case QueryMethod.Eq:
                return BuildEqualExpression<TEntity>(property, propertyValue, parameter);

            case QueryMethod.Ne:
                return BuildNotEqualExpression<TEntity>(property, propertyValue, parameter);

            case QueryMethod.Like:
            case QueryMethod.LikeLeft:
            case QueryMethod.LikeRight:
                return BuildLikeExpression<TEntity>(method, property, propertyValue, parameter);

            case QueryMethod.Between:
            case QueryMethod.NotBetween:
                return BuildBetweenExpression<TEntity>(method, property, propertyValue, parameter);

            case QueryMethod.In:
            case QueryMethod.Notin:
                return BuildInExpression<TEntity>(method, property, propertyValue, parameter);

            case QueryMethod.Gt:
            case QueryMethod.Lt:
            case QueryMethod.Ge:
            case QueryMethod.Le:
                return BuildComparisonExpression<TEntity>(method, property, propertyValue,
                    parameter);

            default:
                throw new ArgumentException("Unsupported QueryMethod");
        }
    }


    /// <summary>
    /// 构建实体属性等于的条件表达式。
    /// </summary>
    /// <param name="property"></param>
    /// <param name="propertyValue"></param>
    /// <param name="parameter"></param>
    /// <typeparam name="TEntity"></typeparam>
    /// <returns></returns>
    private static Expression<Func<TEntity, bool>> BuildEqualExpression<TEntity>(MemberExpression property,
        dynamic propertyValue, ParameterExpression parameter)
    {
        var constant = GetConstantExpression(property.Type, propertyValue);
        var bodyEq = Expression.Equal(property, constant);
        return Expression.Lambda<Func<TEntity, bool>>(bodyEq, parameter);
    }

    /// <summary>
    /// 构建实体属性不等于的条件表达式。
    /// </summary>
    /// <param name="property"></param>
    /// <param name="propertyValue"></param>
    /// <param name="parameter"></param>
    /// <typeparam name="TEntity"></typeparam>
    /// <returns></returns>
    private static Expression<Func<TEntity, bool>> BuildNotEqualExpression<TEntity>(MemberExpression property,
        dynamic propertyValue, ParameterExpression parameter)
    {
        var constant = GetConstantExpression(property.Type, propertyValue);
        var bodyNe = Expression.NotEqual(property, constant);
        return Expression.Lambda<Func<TEntity, bool>>(bodyNe, parameter);
    }


    /// <summary>
    /// 检查 PropertyValue
    /// </summary>
    /// <param name="method"></param>
    /// <param name="propertyValue"></param>
    /// <exception cref="ArgumentException"></exception>
    private static void ValidatePropertyValue(QueryMethod method, dynamic? propertyValue)
    {
        // 这里类型要排除掉
        List<QueryMethod> collectionMethods =
            [QueryMethod.In, QueryMethod.Notin, QueryMethod.Between, QueryMethod.NotBetween];

        // 如果不是上面的类型，就不能是集合类型
        if (!collectionMethods.Contains(method))
        {
            if (IsCollection(propertyValue))
            {
                // 抛出异常，不支持集合处理
                throw new ArgumentException($"Unsupported QueryMethod {method} for Collection propertyValue");
            }
        }
        else
        {
            if (!IsCollection(propertyValue))
            {
                // 抛出异常，必须是集合类型
                throw new ArgumentException(
                    $"Unsupported QueryMethod {method}. PropertyValue must be Collection type.");
            }
        }


        // -----------------------------------上面校验通过，就验证数据是否合法
        // 如果为null 抛出异常
        if (propertyValue == null)
        {
            throw new ArgumentNullException(nameof(propertyValue), "propertyValue cannot be null.");
        }

        // 如果是集合，但是元素为空抛出异常
        if (!IsCollection(propertyValue))
        {
            return;
        }

        if (propertyValue is not IEnumerable enumerable || !enumerable.Cast<object?>().Any())
        {
            throw new ArgumentException(
                "propertyValue must contain at least one element if it is a non-null collection.",
                nameof(propertyValue));
        }
    }


    /// <summary>
    /// 判断是否是集合，但是不包含string
    /// </summary>
    /// <param name="obj"></param>
    /// <returns></returns>
    private static bool IsCollection(dynamic? obj)
    {
        return obj is IEnumerable && !(obj is string);
    }


    /// <summary>
    /// 构建Between
    /// </summary>
    /// <param name="method"></param>
    /// <param name="property"></param>
    /// <param name="propertyValue"></param>
    /// <param name="parameter"></param>
    /// <typeparam name="TEntity"></typeparam>
    /// <returns></returns>
    /// <exception cref="ArgumentException"></exception>
    private static Expression<Func<TEntity, bool>> BuildBetweenExpression<TEntity>(
        QueryMethod method,
        Expression property,
        dynamic propertyValue,
        ParameterExpression parameter
    )
    {
        AssertIsEnumerableButNotString(propertyValue);

        // 获取集合元素的类型
        // var elementType = property.Type.GetGenericArguments()[0];
        // var elementType = property.Type;
        // 将 propertyValue 转换为 List<elementType>
        var typedPropertyValue = ((IEnumerable)propertyValue)
            .Cast<object>()
            //.Select(i => Convert.ChangeType(i, elementType))
            .ToList();

        if (typedPropertyValue.Count != 2)
        {
            throw new ArgumentException(
                "propertyValue must contain exactly two elements for Between/NotBetween comparison");
        }

        var min = typedPropertyValue.ElementAt(0);
        var max = typedPropertyValue.ElementAt(1);

        // 创建一个常量表达式，表示开始和结束日期
        var startConstant = Expression.Constant(min);
        var endConstant = Expression.Constant(max);


        // https://stackoverflow.com/questions/2088231/expression-greaterthan-fails-if-one-operand-is-nullable-type-other-is-non-nulla
        // 解决可为空无法构建
        var tuple1 = EnsureCompatibleNullableTypes(property, startConstant);
        var tuple2 = EnsureCompatibleNullableTypes(property, endConstant);


        if (method == QueryMethod.Between)
        {
            var bodyBetween = Expression.AndAlso(
                Expression.GreaterThanOrEqual(tuple1.Item1, tuple1.Item2),
                Expression.LessThanOrEqual(tuple2.Item1, tuple2.Item2)
            );

            return Expression.Lambda<Func<TEntity, bool>>(bodyBetween, parameter);
        }

        if (method == QueryMethod.NotBetween)
        {
            var bodyNotBetween = Expression.OrElse(
                Expression.LessThan(tuple1.Item1, tuple1.Item2),
                Expression.GreaterThan(tuple2.Item1, tuple2.Item2)
            );

            return Expression.Lambda<Func<TEntity, bool>>(bodyNotBetween, parameter);
        }

        throw new ArgumentException("Unsupported QueryMethod for Between/NotBetween");
    }


    /// <summary>
    /// 确保兼容的可为null的类型
    /// </summary>
    /// <param name="e1"></param>
    /// <param name="e2"></param>
    /// <returns></returns>
    private static (Expression, Expression) EnsureCompatibleNullableTypes(Expression e1, Expression e2)
    {
        if (IsNullableType(e1.Type) && !IsNullableType(e2.Type))
        {
            e2 = Expression.Convert(e2, e1.Type);
        }
        else if (!IsNullableType(e1.Type) && IsNullableType(e2.Type))
        {
            e1 = Expression.Convert(e1, e2.Type);
        }

        return (e1, e2);
    }

    private static bool IsNullableType(Type t)
    {
        return t.IsGenericType && t.GetGenericTypeDefinition() == typeof(Nullable<>);
    }


    /// <summary>
    /// 构建用于查询属性值是否匹配值集合的 LINQ 表达式。
    /// </summary>
    /// <typeparam name="TEntity">实体类型，表示查询操作的对象类型。</typeparam>
    /// <param name="method">要应用的查询方法（In 或 Notin）。</param>
    /// <param name="property">要比较的属性的表达式。</param>
    /// <param name="propertyValue">要比较的值集合。</param>
    /// <param name="parameter">表示查询中实体的参数表达式。</param>
    /// <returns>表示查询的表达式。</returns>
    /// <exception cref="ArgumentException">当提供了不支持的 QueryMethod 或者 propertyValue 不是有效的非空可枚举类型时抛出。</exception>
    private static Expression<Func<TEntity, bool>> BuildInExpression<TEntity>(
        QueryMethod method,
        MemberExpression property,
        dynamic propertyValue,
        ParameterExpression parameter)
    {
        // 断言 propertyValue 是有效的可枚举类型
        AssertIsEnumerableButNotString(propertyValue);

        // 获取集合元素的类型
        //var elementType = property.Type.GetGenericArguments()[0];

        // 将 propertyValue 转换为 List<elementType>
        var typedPropertyValue = ((IEnumerable)propertyValue)
            .Cast<object>()
            //.Select(item => Convert.ChangeType(item, elementType))
            .ToList();


        // 创建 List<elementType> 类型的常量表达式
        var constantExpression = Expression.Constant(typedPropertyValue);

        // 获取 Enumerable.Contains 方法
        var containsMethod = typeof(Enumerable)
            .GetMethods()
            .First(m => m.Name == nameof(Enumerable.Contains) && m.GetParameters().Length == 2)
            .MakeGenericMethod(typeof(object));


        // 将 property 表达式转换为 object 类型的表达式
        Expression objectProperty = Expression.Convert(property, typeof(object));

        // 创建方法调用表达式 Enumerable.Contains(typedPropertyValue, property)
        var containsExpression = Expression.Call(
            containsMethod,
            constantExpression, // 使用常量表达式作为参数
            objectProperty
        );

        // 处理
        return method switch
        {
            // 根据查询方法返回 Lambda 表达式
            QueryMethod.In => Expression.Lambda<Func<TEntity, bool>>(containsExpression, parameter),
            // 创建一个 Not 表达式，表示对 containsExpression 取反
            QueryMethod.Notin => Expression.Lambda<Func<TEntity, bool>>(Expression.Not(containsExpression), parameter),
            _ => throw new ArgumentException("不支持的 QueryMethod")
        };
    }

    /// <summary>
    /// 方法用于构建比较表达式的通用实现
    /// </summary>
    /// <param name="method"></param>
    /// <param name="property"></param>
    /// <param name="propertyValue"></param>
    /// <param name="parameter"></param>
    /// <typeparam name="TEntity"></typeparam>
    /// <returns></returns>
    /// <exception cref="ArgumentException"></exception>
    private static Expression<Func<TEntity, bool>> BuildComparisonExpression<TEntity>(
        QueryMethod method,
        MemberExpression property,
        dynamic propertyValue,
        ParameterExpression parameter
    )
    {
        // 处理下数据
        var constant = GetConstantExpression(property.Type, propertyValue);

        var comparisonType = method switch
        {
            QueryMethod.Gt => ExpressionType.GreaterThan,
            QueryMethod.Lt => ExpressionType.LessThan,
            QueryMethod.Ge => ExpressionType.GreaterThanOrEqual,
            QueryMethod.Le => ExpressionType.LessThanOrEqual,
            _ => throw new ArgumentException($"Unsupported QueryMethod: {method}")
        };
        AssertNumericType(property.Type); // 断言类型为数值类型
        var body = Expression.MakeBinary(comparisonType, property, constant);
        return Expression.Lambda<Func<TEntity, bool>>(body, parameter);
    }

    /// <summary>
    /// 构建通用的 LIKE 表达式树
    /// </summary>
    /// <param name="method"></param>
    /// <param name="property"></param>
    /// <param name="propertyValue"></param>
    /// <param name="parameter"></param>
    /// <typeparam name="TEntity"></typeparam>
    /// <returns></returns>
    /// <exception cref="ArgumentException"></exception>
    private static Expression<Func<TEntity, bool>> BuildLikeExpression<TEntity>(
        QueryMethod method,
        MemberExpression property,
        dynamic propertyValue,
        ParameterExpression parameter
    )
    {
        // 处理下数据
        var constant = GetConstantExpression(property.Type, propertyValue);

        if (property.Type == typeof(string))
        {
            // 获取字符串的匹配方法
            var containsMethod = method switch
            {
                QueryMethod.Like => typeof(string).GetMethod(nameof(string.Contains), [typeof(string)]),
                QueryMethod.LikeLeft => typeof(string).GetMethod(nameof(string.EndsWith), [typeof(string)]),
                QueryMethod.LikeRight => typeof(string).GetMethod(nameof(string.StartsWith), [typeof(string)]),
                _ => throw new ArgumentException(
                    $"Unsupported QueryMethod: {method} is not supported for string operations.")
            };
            // 构建方法调用表达式
            var bodyLike = Expression.Call(property, containsMethod!, constant);
            return Expression.Lambda<Func<TEntity, bool>>(bodyLike, parameter);
        }


        AssertNumericType(property.Type);
        // 对于数值类型，转换为字符串后使用 EF.Functions.Like 方法
        var toStringMethod = typeof(object).GetMethod(nameof(object.ToString));
        var toStringCall = Expression.Call(property, toStringMethod!);
        // 构建模糊查询模式
        var patternConstant = method switch
        {
            QueryMethod.Like => Expression.Constant($"%{constant.Value}%"),
            QueryMethod.LikeLeft => Expression.Constant($"{constant.Value}%"),
            QueryMethod.LikeRight => Expression.Constant($"%{constant.Value}"),
            _ => throw new ArgumentException($"Unsupported QueryMethod: {method} is not supported for LIKE query.")
        };

        var likeMethod = typeof(DbFunctionsExtensions).GetMethod(nameof(DbFunctionsExtensions.Like),
            [typeof(DbFunctions), typeof(string), typeof(string)]);
        var dbFunctionsParameter = Expression.Constant(EF.Functions);

        // 构建 EF.Functions.Like 方法调用表达式
        var eFFunctionsLike = Expression.Call(likeMethod!, dbFunctionsParameter, toStringCall, patternConstant);
        return Expression.Lambda<Func<TEntity, bool>>(eFFunctionsLike, parameter);
    }


    /// <summary>
    /// 断言是 Enumerable
    /// </summary>
    /// <param name="propertyValue"></param>
    /// <exception cref="ArgumentException"></exception>
    private static void AssertIsEnumerableButNotString(dynamic? propertyValue)
    {
        if (!IsCollection(propertyValue))
        {
            throw new ArgumentException(
                "The propertyValue must be a non-null Collection with at least one element.");
        }
    }


    /// <summary>
    /// 断言是数字类型
    /// </summary>
    /// <param name="type"></param>
    /// <exception cref="ArgumentException"></exception>
    private static void AssertNumericType(Type type)
    {
        if (!IsNumericType(type))
        {
            throw new ArgumentException($"Unsupported numeric type required for comparison operation: {type.FullName}");
        }
    }


    /// <summary>
    /// 检查是否为数值类型
    /// </summary>
    /// <param name="type"></param>
    /// <returns></returns>
    private static bool IsNumericType(Type type)
    {
        if (type.IsGenericType && type.GetGenericTypeDefinition() == typeof(Nullable<>))
        {
            type = Nullable.GetUnderlyingType(type) ?? throw new InvalidOperationException();
        }

        switch (Type.GetTypeCode(type))
        {
            case TypeCode.Byte:
            case TypeCode.SByte:
            case TypeCode.UInt16:
            case TypeCode.Int16:
            case TypeCode.UInt32:
            case TypeCode.Int32:
            case TypeCode.UInt64:
            case TypeCode.Int64:
            case TypeCode.Decimal:
            case TypeCode.Single:
            case TypeCode.Double:
                return true;
            case TypeCode.Empty:
            case TypeCode.Object:
            case TypeCode.DBNull:
            case TypeCode.Boolean:
            case TypeCode.Char:
            case TypeCode.DateTime:
            case TypeCode.String:
            default:
                return false;
        }
    }


    /// <summary>
    /// 辅助方法：根据类型获取常量表达式
    /// </summary>
    /// <param name="propertyType"></param>
    /// <param name="propertyValue"></param>
    /// <returns></returns>
    /// <exception cref="ArgumentException"></exception>
    private static ConstantExpression GetConstantExpression(Type propertyType, dynamic propertyValue)
    {
        try
        {
            if (propertyValue == null)
            {
                return Expression.Constant(null, propertyType); // Handle null value
            }

            // For other value types, handle type conversion
            if (propertyValue.GetType() == propertyType)
            {
                return Expression.Constant(propertyValue, propertyType);
            }

            // 尝试转换
            return Expression.Constant(Convert.ChangeType(propertyValue, propertyType), propertyType);
        }
        catch (Exception ex)
        {
            throw new ArgumentException($"Cannot convert {propertyValue.GetType()} to {propertyType}.", ex);
        }
    }
}