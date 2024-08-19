package com.ytrue.service.system;

import cn.hutool.core.lang.tree.Tree;
import com.ytrue.bean.query.system.SysMenuListQuery;
import com.ytrue.bean.req.system.SysMenuAddReq;
import com.ytrue.bean.req.system.SysMenuUpdateReq;
import com.ytrue.bean.resp.system.SysMenuIdResp;
import com.ytrue.bean.resp.system.SysRouterResp;
import com.ytrue.infra.db.base.IBaseService;
import com.ytrue.bean.dataobject.system.SysMenu;

import java.util.List;

/**
 * @author ytrue
 * @description: SysMenuService
 * @date 2022/12/7 14:11
 */
public interface SysMenuService extends IBaseService<SysMenu> {

    List<SysMenu> listBySysMenuListQuery(SysMenuListQuery queryParam);

    /**
     * 新增菜单
     *
     * @param requestParam
     */
    void addMenu(SysMenuAddReq requestParam);

    /**
     * 修改菜单
     *
     * @param requestParam
     */
    void updateMenu(SysMenuUpdateReq requestParam);
    /**
     * 删除菜单
     *
     * @param ids
     */
    void removeBySysMenuIds(List<Long> ids);



    /**
     * 根据用户id获取菜单列表
     *
     * @param userId
     * @return
     */
    List<SysMenu> listBySysUserId(Long userId);


    /**
     * 根据id获取
     * @param id
     * @return
     */
    SysMenuIdResp getBySysMenuId(Long id);

    /**
     * 获取路由信息
     * @param userId
     * @return
     */
    List<SysRouterResp> listSysRouterBySysUserId(Long userId);
}
