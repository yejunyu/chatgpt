package com.thss.chat.interfaces

import cn.hutool.http.HttpStatus
import cn.hutool.jwt.JWTUtil
import com.thss.chat.infrastructure.common.Constants
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author yjy
 * @emial yyyejunyu@gmail.com
 * @date 2024/1/2
 * @description Api访问准入管理
 **/
@RestController
class ApiAccessController {
    val logger = LoggerFactory.getLogger("ApiAccessController")

    @RequestMapping("/authorize")
    fun authorize(username: String, password: String): ResponseEntity<Map<String, String>> {
        var map = mutableMapOf<String, String>()
        // 模拟账号登录
        if ("yejunyu" != username || "yejunyu" != password) {
            map["msg"] = "用户名和密码错误"
            return ResponseEntity.ok(map)
        }
        // 登录后生成token
        val chaim = mapOf("username" to username)
        var jwtToken = JWTUtil.createToken(chaim, Constants.KEY.encodeToByteArray())
        map["msg"] = "授权成功"
        map["token"] = jwtToken
        return ResponseEntity.ok(map)
    }

    @GetMapping("/verify")
    fun verify(token: String?): ResponseEntity<String> {
        logger.info("验证 token：{}", token)
        return ResponseEntity.status(HttpStatus.HTTP_OK).body("verify success!")
    }


    @GetMapping("/success")
    fun success(): String {
        return "test success"
    }

}