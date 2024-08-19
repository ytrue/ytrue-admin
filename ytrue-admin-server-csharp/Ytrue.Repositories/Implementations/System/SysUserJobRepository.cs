using SqlSugar;
using Ytrue.Infrastructures.Bases;
using Ytrue.Models.Entities.System;
using Ytrue.Repositories.Interfaces.System;

namespace Ytrue.Repositories.Implementations.System;

/// <summary>
/// 系统用户岗位关联持久层
/// </summary>
public class SysUserJobRepository : BaseRepository<SysUserJob>, ISysUserJobRepository
{
    public SysUserJobRepository(ISqlSugarClient db) : base(db)
    {
    }
}