package com.ytrue.tools.security.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ytrue
 * @date 2022/4/26 11:09
 * @description LoginUser
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = -1977241509435137545L;

    private User user;

    /**
     * 获取权限
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (user.getAuthorities() != null) {
            if (!user.getAuthorities().isEmpty()) {
                List<GrantedAuthority> authorities;
                //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
                authorities = user.getAuthorities().stream().
                        map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                return authorities;
            }
        }
        return null;
    }

    /**
     * 获取密码
     *
     * @return
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 获取用户名
     *
     * @return
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 帐户未过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 帐户未锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证未过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 已启用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
