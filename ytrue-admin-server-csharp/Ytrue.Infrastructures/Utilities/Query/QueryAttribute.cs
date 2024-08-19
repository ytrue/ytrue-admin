namespace Ytrue.Infrastructures.Utilities.Query;

[AttributeUsage(AttributeTargets.Property)]
public class QueryAttribute : System.Attribute
{
    public string? Column { get; set; }

    public QueryMethod Condition { get; set; } = QueryMethod.Eq;

    public string? Alias { get; set; }

    public string Operator { get; set; } = "and";
}