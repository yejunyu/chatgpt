package com.thss.chat.domain.security.service

import com.thss.chat.domain.security.model.vo.JwtToken
import io.jsonwebtoken.io.IOException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.shiro.web.filter.AccessControlFilter
import org.slf4j.LoggerFactory

/**
 * @author yjy
 * @emial yyyejunyu@gmail.com
 * @date 2024/1/2
 * @description 自定义的Filter过滤器
 **/
class JwtFilter : AccessControlFilter() {
    private val logger = LoggerFactory.getLogger("JwtFilter")

    /**
     * 返回false 走onAccessDenied流程
     */
    override fun isAccessAllowed(p0: ServletRequest?, p1: ServletResponse?, p2: Any?): Boolean {
        return false
    }

    /**
     * 返回true时为通过
     */
    override fun onAccessDenied(p0: ServletRequest, p1: ServletResponse): Boolean {
        val request = p0 as HttpServletRequest
        val jwtToken = JwtToken(request.getParameter("token"))
        try {
            // 鉴权
            getSubject(p0, p1).login(jwtToken)
            return true
        } catch (e: Exception) {
            logger.error("鉴权失败", e)
            onLoginFail(p1)
            return false
        }
    }

    @Throws(IOException::class)
    private fun onLoginFail(response: ServletResponse) {
        val httpResponse = response as HttpServletResponse
        httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED
        httpResponse.writer.write("Auth Err!")
    }
}