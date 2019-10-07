package com.company.chess_online.disconnect_checker.schedules

import com.company.chess_online.disconnect_checker.data.model.Game
import com.company.chess_online.disconnect_checker.data.model.GameStatus
import com.company.chess_online.disconnect_checker.data.model.User
import com.company.chess_online.disconnect_checker.data.repository.GameRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.amqp.rabbit.core.RabbitTemplate
import java.time.LocalDateTime

internal class PingCheckScheduleTest {

    companion object {
        val whiteId = 1L
        val whitePlayer = User(id = whiteId)

        val blackId = 2L
        val blackPlayer = User(id = blackId)

        val gameId = 3L

        val statusList = listOf(GameStatus.FINISHED, GameStatus.STOPPED)

        fun createTestGameEntity() = Game(
                id = gameId,
                status = GameStatus.WAITNG_TO_START,
                version = 1,
                created = LocalDateTime.now()
        )
    }

    @Mock
    lateinit var gameRepository: GameRepository
    @Mock
    lateinit var rabbitTemplate: RabbitTemplate

    @InjectMocks
    lateinit var pingCheckSchedule: PingCheckSchedule

    @BeforeEach
    internal fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    internal fun whiteDisconnectedBroadcastTest() {
        val game = createTestGameEntity()
        game.whitePlayer = whitePlayer
        game.whitePing = LocalDateTime.now().minusMinutes(1)

        val gameList = ArrayList<Game>()
        gameList.add(game)

        given(gameRepository.findByStatusNotIn(statusList)).willReturn(gameList)

        pingCheckSchedule.checkPings()

        val message = HashMap<String, Long>()
        message.put("playerId", whiteId)
        message.put("gameId", gameId)

        verify(gameRepository, times(1)).findByStatusNotIn(statusList)
        verifyNoMoreInteractions(gameRepository)

        verify(rabbitTemplate, times(1)).convertAndSend(DISCONNECT_QUEUE, message)
        verifyNoMoreInteractions(rabbitTemplate)
    }

    @Test
    internal fun blackDisconnectedBroadcastTest() {
        val game = createTestGameEntity()
        game.blackPlayer = blackPlayer
        game.blackPing = LocalDateTime.now().minusMinutes(1)

        val gameList = ArrayList<Game>()
        gameList.add(game)

        given(gameRepository.findByStatusNotIn(statusList)).willReturn(gameList)

        pingCheckSchedule.checkPings()

        val message = HashMap<String, Long>()
        message.put("playerId", blackId)
        message.put("gameId", gameId)

        verify(gameRepository, times(1)).findByStatusNotIn(statusList)
        verifyNoMoreInteractions(gameRepository)

        verify(rabbitTemplate, times(1)).convertAndSend(DISCONNECT_QUEUE, message)
        verifyNoMoreInteractions(rabbitTemplate)
    }

    @Test
    internal fun noBroadcatIfPingNotLateTest() {
        val game = createTestGameEntity()
        game.whitePlayer = whitePlayer
        game.whitePing = LocalDateTime.now()

        game.blackPlayer = blackPlayer
        game.blackPing = LocalDateTime.now()

        val gameList = ArrayList<Game>()
        gameList.add(game)

        given(gameRepository.findByStatusNotIn(statusList)).willReturn(gameList)

        pingCheckSchedule.checkPings()

        verify(gameRepository, times(1)).findByStatusNotIn(statusList)
        verifyNoMoreInteractions(gameRepository)

        Mockito.verifyZeroInteractions(rabbitTemplate)
    }

    @Test
    internal fun noBroadcastIfNoPlayerTest() {
        val game = createTestGameEntity()
        game.whitePing = LocalDateTime.now().minusMinutes(1)

        game.blackPing = LocalDateTime.now().minusMinutes(1)

        val gameList = ArrayList<Game>()
        gameList.add(game)

        given(gameRepository.findByStatusNotIn(statusList)).willReturn(gameList)

        pingCheckSchedule.checkPings()

        verify(gameRepository, times(1)).findByStatusNotIn(statusList)
        verifyNoMoreInteractions(gameRepository)

        Mockito.verifyZeroInteractions(rabbitTemplate)
    }
}