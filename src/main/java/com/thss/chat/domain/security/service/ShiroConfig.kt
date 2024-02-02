package com.thss.chat.domain.security.service

import com.thss.chat.domain.security.service.realm.JwtRealm
import jakarta.servlet.Filter
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator
import org.apache.shiro.mgt.DefaultSubjectDAO
import org.apache.shiro.mgt.SubjectFactory
import org.apache.shiro.realm.Realm
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.subject.Subject
import org.apache.shiro.subject.SubjectContext
import org.apache.shiro.web.filter.authc.AnonymousFilter
import org.apache.shiro.web.filter.authc.LogoutFilter
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * @author yjy
 * @emial yyyejunyu@gmail.com
 * @date 2024/1/2
 * @description
 **/
@Configuration
class ShiroConfig {

    @Bean
    fun subjectFactory(): SubjectFactory {
        class JwtDefaultSubjectFactory : DefaultWebSubjectFactory() {
            override fun createSubject(context: SubjectContext): Subject {
                context.isSessionCreationEnabled = false
                return super.createSubject(context)
            }
        }
        return JwtDefaultSubjectFactory()
    }

    @Bean
    fun realm(): Realm {
        return JwtRealm()
    }

    @Bean
    fun securityManager(): DefaultWebSecurityManager {
        val securityManager = DefaultWebSecurityManager()
        securityManager.setRealm(realm())
        // 关闭 ShiroDAO 功能
        val subjectDAO = DefaultSubjectDAO()
        val defaultSessionStorageEvaluator = DefaultSessionStorageEvaluator()
        // 不需要将 Shiro Session 中的东西存到任何地方（包括 Http Session 中）
        defaultSessionStorageEvaluator.isSessionStorageEnabled = false
        subjectDAO.sessionStorageEvaluator = defaultSessionStorageEvaluator
        securityManager.subjectDAO = subjectDAO
        // 禁止Subject的getSession方法
        securityManager.subjectFactory = subjectFactory()
        return securityManager
    }

    @Bean
    fun shiroFilterFactoryBean(): ShiroFilterFactoryBean {
        val shiroFilter = ShiroFilterFactoryBean()
        shiroFilter.securityManager = securityManager()
        shiroFilter.loginUrl = "/unauthenticated"
        shiroFilter.unauthorizedUrl = "/unauthorized"
        // 添加jwt过滤器
        val filterMap: MutableMap<String, Filter> = HashMap()
        // 设置过滤器【anon\logout可以不设置】
        filterMap["anon"] = AnonymousFilter()
        filterMap["jwt"] = JwtFilter()
        filterMap["logout"] = LogoutFilter()
        shiroFilter.filters = filterMap

        // 拦截器，指定方法走哪个拦截器 【login->anon】【logout->logout】【verify->jwt】
        val filterRuleMap: MutableMap<String, String> = LinkedHashMap()
        filterRuleMap["/login"] = "anon"
        filterRuleMap["/logout"] = "logout"
        filterRuleMap["/verify"] = "jwt"
        shiroFilter.filterChainDefinitionMap = filterRuleMap

        return shiroFilter
    }

}