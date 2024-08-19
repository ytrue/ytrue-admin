namespace Ytrue.Infrastructures.Utilities.Query;

/// <summary>
/// 表示不同的查询方法，用于比较或筛选。
/// </summary>
public enum QueryMethod
{
    /// <summary>
    /// 等于 (==)
    /// </summary>
    Eq,

    /// <summary>
    /// 不等于 (!=)
    /// </summary>
    Ne,

    /// <summary>
    /// 模糊匹配（支持通配符）
    /// </summary>
    Like,

    /// <summary>
    /// 左侧模糊匹配（以...开头）
    /// </summary>
    LikeLeft,

    /// <summary>
    /// 右侧模糊匹配（以...结尾）
    /// </summary>
    LikeRight,

    /// <summary>
    /// 范围查询 [开始, 结束]
    /// </summary>
    Between,

    /// <summary>
    /// 非范围查询
    /// </summary>
    NotBetween,

    /// <summary>
    /// 集合包含比较
    /// </summary>
    In,

    /// <summary>
    /// 集合不包含比较
    /// </summary>
    Notin,

    /// <summary>
    /// 大于比较 (>)
    /// </summary>
    Gt,

    /// <summary>
    /// 小于比较 (<)
    /// </summary>
    Lt,

    /// <summary>
    /// 大于等于比较 (>=)
    /// </summary>
    Ge,

    /// <summary>
    /// 小于等于比较 (<=)
    /// </summary>
    Le
}
