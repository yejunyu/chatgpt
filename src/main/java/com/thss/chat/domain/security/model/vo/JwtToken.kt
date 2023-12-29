package com.thss.chat.domain.security.model.vo

import org.apache.shiro.authc.AuthenticationToken

/**
 * @author yjy
 * @emial yyyejunyu@gmail.com
 * @date 2023/12/15
 * @description Token对象存用户id
 **/
data class JwtToken(private val jwt: String) : AuthenticationToken {
    override fun getPrincipal(): Any {
        return jwt
    }

    override fun getCredentials(): Any {
        return jwt
    }

}
