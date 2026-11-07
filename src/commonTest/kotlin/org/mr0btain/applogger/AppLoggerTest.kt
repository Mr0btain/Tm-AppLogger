package org.mr0btain.applogger

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppLoggerTest {
    private enum class TestMessage(override val message: String) : LogMessage {
        SAVED("Saved")
    }

    @BeforeTest
    fun setUp() {
        LogBook.reset()
        AppLogger.debug = false
    }

    @AfterTest
    fun tearDown() {
        LogBook.reset()
        AppLogger.debug = false
        LogBook.capacity = LogBook.DEFAULT_CAPACITY
    }

    @Test
    fun eachLevelIsRecorded() {
        AppLogger.e("Test:E", "failed", IllegalStateException("boom"))
        AppLogger.w("Test:W", "odd")
        AppLogger.i("Test:I", TestMessage.SAVED)
        AppLogger.m("Test:M", "general")

        val lines = LogBook.since(0).lines
        assertEquals(listOf(LogLevel.ERROR, LogLevel.WARN, LogLevel.INFO, LogLevel.INFO), lines.map { it.level })
        assertEquals(listOf("Test:E", "Test:W", "Test:I", "Test:M"), lines.map { it.tag })
        assertTrue(lines[0].message.startsWith("failed\n"))
        assertTrue(lines[0].message.contains("boom"))
        assertEquals("Saved", lines[2].message)
        assertEquals(4L, LogBook.last.value)
    }

    @Test
    fun debugOnlyWhenTurnedOn() {
        AppLogger.d("Test", "hidden")
        assertTrue(LogBook.since(0).lines.isEmpty())

        AppLogger.debug = true
        AppLogger.d("Test", "shown")
        assertEquals(listOf("shown"), LogBook.since(0).lines.map { it.message })
    }

    @Test
    fun keepsTheNewestLinesUpToCapacity() {
        LogBook.capacity = 3
        repeat(5) { AppLogger.i("Test", "line $it") }

        val tail = LogBook.since(0)
        assertEquals(listOf("line 2", "line 3", "line 4"), tail.lines.map { it.message })
        assertEquals(5L, tail.last)
        assertEquals(listOf("line 4"), LogBook.since(4).lines.map { it.message })
    }

    @Test
    fun longMessagesAreCut() {
        AppLogger.i("Test", "x".repeat(LogBook.MAX_MESSAGE + 100))
        val message = LogBook.since(0).lines.single().message
        assertEquals(LogBook.MAX_MESSAGE + 4, message.length)
        assertTrue(message.endsWith(" ..."))
    }
}
