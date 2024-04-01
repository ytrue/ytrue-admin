package com.ytrue.tools.security.util;

import com.ytrue.tools.security.user.LoginUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author ytrue
 * @description: SecurityUtils
 * @date 2022/12/8 15:18
 */
public class SecurityUtils {

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息异常", e);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null != authentication && authentication.isAuthenticated()
               && !(authentication instanceof AnonymousAuthenticationToken);
    }

    /**
     * 获取 LoginUser User#extend key对于的value，如果没有返回null
     *
     * @param key
     * @return
     */
    public static String getLoginUserExtendValue(String key) {
        return getLoginUser().getUser().getExtend().get(key);
    }
}
