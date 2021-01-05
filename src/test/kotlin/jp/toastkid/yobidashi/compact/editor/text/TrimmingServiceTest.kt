package jp.toastkid.yobidashi.compact.editor.text

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TrimmingServiceTest {

    private lateinit var trimmingService: TrimmingService

    @BeforeEach
    fun setUp() {
        trimmingService = TrimmingService()
    }

    @Test
    fun test() {
        assertEquals("aaa", trimmingService.invoke("  aaa   "))
        val lineSeparator = "\n"
        assertEquals(
                "john${lineSeparator}aaa${lineSeparator}trimmed",
                trimmingService.invoke(listOf("  john", " aaa   ", "trimmed  ").joinToString(lineSeparator))
        )
    }

}