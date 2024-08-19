using SqlSugar;

namespace Ytrue.Models.Bases;

public class BaseIdEntity
{
    /// <summary>
    /// ID
    /// </summary>
    [SugarColumn(IsPrimaryKey = true, IsIdentity = true)]
    public long Id { get; set; }
}