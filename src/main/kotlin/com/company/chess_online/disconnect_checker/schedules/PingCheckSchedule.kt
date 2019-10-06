package com.company.chess_online.disconnect_checker.schedules

import com.company.chess_online.disconnect_checker.data.model.GameStatus
import com.company.chess_online.disconnect_checker.data.repository.GameRepository
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

val DISCONNECT_QUEUE = "disconnectQueue"

@Configuration
class Queues {
    @Bean
    fun disconnectQueue() = Queue(DISCONNECT_QUEUE, false)

}

@Component
class PingCheckSchedule @Autowired constructor(
        private val gameRepository: GameRepository,
        private val rabbitTemplate: RabbitTemplate
) {
    @Scheduled(fixedDelay = 5000)
    fun checkPings() {
        val checkTimestamp = LocalDateTime.now().minusSeconds(30)

        gameRepository.findByStatusNot(GameStatus.FINISHED).forEach { g ->
            println(g)

            g.blackPing?.let {
                if (it.isBefore(checkTimestamp)) g.blackPlayer?.let { broadcastDisconnect(it.id, g.id) }
            }

            g.whitePing?.let {
                if (it.isBefore(checkTimestamp)) g.whitePlayer?.let { broadcastDisconnect(it.id, g.id) }
            }
        }
    }

    fun broadcastDisconnect(playerId: Long, gameId: Long) {
        val message = HashMap<String, Long>()
        message.put("playerId", playerId)
        message.put("gameId", gameId)
        rabbitTemplate.convertAndSend(DISCONNECT_QUEUE, message)
    }
}

