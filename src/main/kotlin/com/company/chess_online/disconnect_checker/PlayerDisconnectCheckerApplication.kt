package com.company.chess_online.disconnect_checker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@SpringBootApplication
@EnableScheduling
class PlayerDisconnectCheckerApplication

fun main(args: Array<String>) {
    runApplication<PlayerDisconnectCheckerApplication>(*args)
}


