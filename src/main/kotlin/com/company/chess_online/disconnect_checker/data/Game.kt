package com.company.chess_online.disconnect_checker.data

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Game(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Version
        val version: Long,

        @CreationTimestamp
        val created: LocalDateTime,

        @UpdateTimestamp
        val updated: LocalDateTime,

        val whitePing: LocalDateTime,
        val blackPing: LocalDateTime
)