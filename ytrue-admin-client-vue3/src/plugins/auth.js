import useUserStore from '@/store/modules/user.js'

/**
 * @param permission
 * @returns {*|boolean}
 */
function authPermission(permission) {
    // 所有权限的通配符
    const all_permission = "*:*:*"
    // 从用户存储中获取权限列表
    const permissions = useUserStore().permissions

    if (permission && permission.length > 0) {
        // 检查用户权限是否包含指定权限或通配符权限
        return permissions.some(v => {
            return all_permission === v || v === permission
        })
    } else {
        return false
    }
}

/**
 * 验证用户是否具备某角色
 * @param role
 * @returns {*|boolean}
 */
function authRole(role) {
    // 超级管理员角色
    const super_admin = "admin"
    // 从用户存储中获取角色列表
    const roles = useUserStore().roles

    if (role && role.length > 0) {
        // 检查用户角色是否包含指定角色或超级管理员角色
        return roles.some(v => {
            return super_admin === v || v === role
        })
    } else {
        return false
    }
}

export default {
    /**
     * 验证用户是否具备某权限
     * @param permission
     * @returns {*|boolean}
     */
    hasPermission(permission) {
        return authPermission(permission)
    },
    /**
     * 验证用户是否含有指定权限，只需包含其中一个
     * @param permissions
     * @returns {*}
     */
    hasPermissionOr(permissions) {
        return permissions.some(item => {
            return authPermission(item)
        })
    },
    /**
     * 验证用户是否含有指定权限，必须全部拥有
     * @param permissions
     * @returns {*}
     */
    hasPermissionAnd(permissions) {
        return permissions.every(item => {
            return authPermission(item)
        })
    },
    /**
     *
     * @param role
     * @returns {*|boolean}
     */
    hasRole(role) {
        return authRole(role)
    },
    /**
     * 验证用户是否含有指定角色，只需包含其中一个
     * @param roles
     * @returns {*}
     */
    hasRoleOr(roles) {
        return roles.some(item => {
            return authRole(item)
        })
    },
    /**
     * 验证用户是否含有指定角色，必须全部拥有
     * @param roles
     * @returns {*}
     */
    hasRoleAnd(roles) {
        return roles.every(item => {
            return authRole(item)
        })
    }
}
