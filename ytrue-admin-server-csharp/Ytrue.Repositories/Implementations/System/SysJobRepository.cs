using SqlSugar;
using Ytrue.Infrastructures.Bases;
using Ytrue.Models.Entities.System;
using Ytrue.Repositories.Interfaces.System;

namespace Ytrue.Repositories.Implementations.System;

/// <summary>
/// 系统岗位持久层
/// </summary>
public class SysJobRepository : BaseRepository<SysJob>, ISysJobRepository
{
    public SysJobRepository(ISqlSugarClient db) : base(db)
    {
    }
}