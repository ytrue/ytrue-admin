using Ytrue.Infrastructures.Bases;
using Ytrue.Infrastructures.Response;

namespace Ytrue.Infrastructures.Utilities;

public class AssertUtil
{
    public static void NotNull(object? o, IServerResponseInfo responseCode)
    {
        if (o == null)
        {
            ReportInvalidArgument(responseCode);
        }
    }

    public static void IsNull(object? o, IServerResponseInfo responseCode)
    {
        if (o != null)
        {
            ReportInvalidArgument(responseCode);
        }
    }


    public static void IsTrue(bool b, IServerResponseInfo responseCode)
    {
        if (!b)
        {
            ReportInvalidArgument(responseCode);
        }
    }


    public static void IsFalse(bool b, IServerResponseInfo responseCode)
    {
        if (b)
        {
            ReportInvalidArgument(responseCode);
        }
    }


    public static void CollectionIsNotEmpty<T>(IEnumerable<T>? collection, IServerResponseInfo responseCode)
    {
        // 如果集合为 null 或者不包含任何元素
        if (collection == null || !collection.Any())
        {
            ReportInvalidArgument(responseCode);
        }
    }


    public static void CollectionIsEmpty<T>(IEnumerable<T>? collection, IServerResponseInfo responseCode)
    {
        // 如果集合为 null 或者不包含任何元素
        if (!(collection != null && collection.Any()))
        {
            ReportInvalidArgument(responseCode);
        }
    }


    private static void ReportInvalidArgument(IServerResponseInfo responseCode)
    {
        throw new AssertInvalidArgumentException(responseCode);
    }

    private class AssertInvalidArgumentException : BaseCodeException
    {
        public AssertInvalidArgumentException(IServerResponseInfo responseCode) : base(responseCode)
        {
        }

        public AssertInvalidArgumentException(string? message) : base(message)
        {
        }
    }
}