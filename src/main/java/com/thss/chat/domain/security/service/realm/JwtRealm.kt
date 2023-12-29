package com.thss.chat.domain.security.service.realm;

import cn.hutool.jwt.JWTUtil;
import com.thss.chat.domain.security.model.vo.JwtToken;
import com.thss.chat.domain.security.service.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Objects;

/**
 * @author yjy
 * @emial yyyejunyu@gmail.com
 * @date 2023/12/15
 * @description
 **/
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    private JwtUtil jwtUtil = new JwtUtil();

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwt = (String) token.getPrincipal();
        if (Objects.isNull(jwt)) {
            throw new NullPointerException("token is null");
        }
        if (!jwtUtil.isVerify(jwt)) {
            throw new UnknownAccountException();
        }
        String username = (String) jwtUtil.decode(jwt).get("username");
        log.info("鉴权用户 username：{}", username);
        return new SimpleAuthenticationInfo(jwt, jwt, "JwtRealm");
    }
}
