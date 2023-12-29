package com.thss.chat

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author yjy
 * @emial yyyejunyu@gmail.com
 * @date 2023/12/15
 * @description
 **/
@SpringBootApplication
@RestController
open class ChatApplication {
    private val logger = LoggerFactory.getLogger("ChatApplication")


    @GetMapping("/verify")
    fun verify(token: String): ResponseEntity<String> {
        logger.info("验证 token:{}", token)
        if ("success" == token) {
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.badRequest().build()
    }

    @GetMapping("/success")
    fun success(): String {
        return "test success"
    }
}

fun main(args: Array<String>) {
    runApplication<ChatApplication>(*args)
}





