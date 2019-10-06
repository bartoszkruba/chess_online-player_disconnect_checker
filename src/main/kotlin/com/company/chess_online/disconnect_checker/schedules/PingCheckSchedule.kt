package com.company.chess_online.disconnect_checker.schedules

import com.company.chess_online.disconnect_checker.data.model.GameStatus
import com.company.chess_online.disconnect_checker.data.repository.GameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PingCheckSchedule @Autowired constructor(
        private val gameRepository: GameRepository
) {
    @Scheduled(fixedDelay = 5000)
    fun checkPings() = gameRepository.findByStatusNot(GameStatus.FINISHED).forEach { g ->
        println("whitePing: ${g.whitePing}, blackPing: ${g.blackPing}, status: ${g.status}")
    }
}

