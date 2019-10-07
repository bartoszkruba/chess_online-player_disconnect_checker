package com.company.chess_online.disconnect_checker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class PlayerDisconnectCheckerApplication

fun main(args: Array<String>) {
    runApplication<PlayerDisconnectCheckerApplication>(*args)
}


