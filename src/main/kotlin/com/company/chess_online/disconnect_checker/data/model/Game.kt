package com.company.chess_online.disconnect_checker.data.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

enum class GameStatus { WAITNG_TO_START, STARTED, FINISHED, STOPPED }

@Entity
data class Game(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Version
        val version: Long,

        @Enumerated(EnumType.STRING)
        val status: GameStatus,

        @CreationTimestamp
        val created: LocalDateTime,

        @UpdateTimestamp
        val updated: LocalDateTime,

        val whitePing: LocalDateTime?,
        val blackPing: LocalDateTime?
)