package com.company.chess_online.disconnect_checker.data.repository

import com.company.chess_online.disconnect_checker.data.model.Game
import com.company.chess_online.disconnect_checker.data.model.GameStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : JpaRepository<Game, Long> {
    fun findByStatusNot(status: GameStatus): Iterable<Game>
}