package com.ytrue.serviceimpl.system;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysMenu;
import com.ytrue.bean.dataobject.system.SysRoleMenu;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.bean.enums.system.ComponentTypeEnum;
import com.ytrue.bean.enums.system.MenuTypeEnum;
import com.ytrue.bean.query.system.SysMenuListQuery;
import com.ytrue.bean.req.system.SysMenuAddReq;
import com.ytrue.bean.req.system.SysMenuUpdateReq;
import com.ytrue.bean.resp.system.SysMenuIdResp;
import com.ytrue.bean.resp.system.SysRouterResp;
import com.ytrue.dao.system.SysMenuDao;
import com.ytrue.dao.system.SysRoleMenuDao;
import com.ytrue.dao.system.SysUserDao;
import com.ytrue.infra.core.constant.StrPool;
import com.ytrue.infra.core.response.ServerResponseInfo;
import com.ytrue.infra.core.response.ServerResponseInfoEnum;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.core.util.BeanUtils;
import com.ytrue.infra.db.base.BaseServiceImpl;
import com.ytrue.infra.db.query.util.QueryHelp;
import com.ytrue.manager.RouterManager;
import com.ytrue.service.system.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ytrue
 * @description: SysMenuServiceImpl
 * @date 2022/12/7 14:11
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuDao, SysMenu> implements SysMenuService {

    private final SysUserDao sysUserDao;

    private final SysRoleMenuDao sysRoleMenuDao;

    private final RouterManager routerManager;

    @Override
    public List<SysMenu> listBySysMenuListQuery(SysMenuListQuery queryParam) {
        LambdaQueryWrapper<SysMenu> queryWrapper = QueryHelp.<SysMenu>builderlambdaQueryWrapper(queryParam).orderByAsc(SysMenu::getMenuSort).orderByDesc(SysMenu::getId);

        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addSysMenu(SysMenuAddReq requestParam) {
        // 转换bean
        SysMenu sysMenu = BeanUtils.copyProperties(requestParam, SysMenu::new);

        save(sysMenu);
        updateSubCnt(sysMenu.getPid());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSysMenu(SysMenuUpdateReq requestParam) {
        AssertUtil.numberNotEquals(requestParam.getId(), requestParam.getPid(), ServerResponseInfo.error("父级不能是自己"));

        SysMenu sysMenu = this.getById(requestParam.getId());
        AssertUtil.notNull(sysMenu, ServerResponseInfoEnum.ILLEGAL_OPERATION);

        // 旧的菜单
        Long oldPid = sysMenu.getPid();
        Long newPid = requestParam.getPid();

        // bean转换
        BeanUtils.copyProperties(requestParam, sysMenu);

        updateById(sysMenu);
        // 更新父节点中子节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
    }


    @Override
    public void removeBySysMenuIds(List<Long> ids) {
        for (Long id : ids) {
            // 校验是否存在子级
            SysMenu childMenu = lambdaQuery().eq(SysMenu::getPid, id).one();
            AssertUtil.isNull(childMenu, ServerResponseInfo.error("存在子级，请解除后再试"));

            // 校验角色绑定或者去解绑角色的关系
            SysRoleMenu sysRoleMenu = sysRoleMenuDao.selectOne(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getMenuId, id));
            AssertUtil.isNull(sysRoleMenu, ServerResponseInfo.error("存在角色关联，请解除后再试"));

            SysMenu menu = getById(id);
            removeById(id);
            updateSubCnt(menu.getPid());
        }
    }


    /**
     * 更新数量
     *
     * @param menuId
     */
    private void updateSubCnt(long menuId) {
        Long count = lambdaQuery().eq(SysMenu::getPid, menuId).count();
        // 更新
        lambdaUpdate().eq(SysMenu::getId, menuId).set(SysMenu::getSubCount, count).update();
    }

    @Override
    public List<SysRouterResp> listSysRouterBySysUserId(Long userId) {
        // 获取菜单
        List<SysMenu> menus = listBySysUserId(userId);
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        //转换器
        List<Tree<String>> treeList = TreeUtil.build(menus, "0", treeNodeConfig, (sysMenu, tree) -> {
            // id
            tree.setId(String.valueOf(sysMenu.getId()));
            // 父id
            tree.setParentId(String.valueOf(sysMenu.getPid()));
            // 路由名称
            tree.setName(sysMenu.getMenuName());
            // 整个塞进去
            tree.putExtra("menu", sysMenu);
        });

        return routerManager.buildSysRouters(treeList);
    }



    @Override
    public List<SysMenu> listBySysUserId(Long userId) {
        SysUser sysUser = sysUserDao.selectById(userId);
        AssertUtil.notNull(sysUser, ServerResponseInfoEnum.NOT_FOUND);

        // 平台账号,但是是超级管理员
        if (sysUser.getAdmin()) {
            return lambdaQuery().eq(SysMenu::getStatus, 1).in(SysMenu::getMenuType, MenuTypeEnum.DIRECTORY.getType(), MenuTypeEnum.MENU.getType()).orderByAsc(SysMenu::getPid).orderByAsc(SysMenu::getMenuSort).list();
        }

        // 不然就是普通的账号
        return baseMapper.selectMenusBySysUserId(userId);
    }

    @Override
    public SysMenuIdResp getBySysMenuId(Long id) {
        SysMenu data = this.getById(id);
        AssertUtil.notNull(data, ServerResponseInfoEnum.NOT_FOUND);
        return BeanUtils.copyProperties(data, SysMenuIdResp::new);
    }



}
