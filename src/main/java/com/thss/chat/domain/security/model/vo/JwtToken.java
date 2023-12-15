package com.thss.chat.domain.security.model.vo;

import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author yjy
 * @emial yyyejunyu@gmail.com
 * @date 2023/12/15
 * @description
 **/
@AllArgsConstructor
public class JwtToken implements AuthenticationToken {
    private String jwt;

    @Override
    public Object getPrincipal() {
        return jwt;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }
}
