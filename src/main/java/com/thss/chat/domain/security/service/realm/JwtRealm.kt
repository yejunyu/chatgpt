package com.thss.chat.domain.security.service.realm;

import cn.hutool.jwt.JWTUtil
import com.thss.chat.domain.security.model.vo.JwtToken
import com.thss.chat.infrastructure.common.Constants
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.SimpleAuthenticationInfo
import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection

/**
 * @author yjy
 * @emial yyyejunyu@gmail.com
 * @date 2023/12/15
 * @description 自定义的jwt验证服务
 **/
class JwtRealm : AuthorizingRealm() {


    override fun supports(token: AuthenticationToken?): Boolean {
        return token is JwtToken
    }

    override fun doGetAuthenticationInfo(p0: AuthenticationToken?): AuthenticationInfo {
        val jwt = p0!!.principal as String
        if (jwt.isEmpty()) {
            throw NullPointerException()
        }
        if (!JWTUtil.verify(jwt, Constants.KEY.encodeToByteArray())) {
            throw UnknownAccountException()
        }
        val username = JWTUtil.parseToken(jwt).getPayload("username")
        return SimpleAuthenticationInfo(jwt, jwt, "JwtRealm")
    }

    override fun doGetAuthorizationInfo(p0: PrincipalCollection?): AuthorizationInfo? {
        return null
    }

}
