package jp.toastkid.yobidashi.compact.editor.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color

internal class ContrastRatioCalculatorServiceTest {

    private lateinit var contrastRatioCalculatorService: ContrastRatioCalculatorService

    @BeforeEach
    fun setUp() {
        contrastRatioCalculatorService = ContrastRatioCalculatorService()
    }

    @Test
    fun test() {
        assertEquals(1.0738392f, contrastRatioCalculatorService.invoke(Color.WHITE, Color.YELLOW))
        assertEquals(8.592471f, contrastRatioCalculatorService.invoke(Color.WHITE, Color.BLUE))
    }

}