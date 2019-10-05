package com.company.chess_online.disconnect_checker.schedules

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PingCheckSchedule {
    @Scheduled(fixedDelay = 5000)
    fun checkPings() {
        println("Checking...")
    }
}

