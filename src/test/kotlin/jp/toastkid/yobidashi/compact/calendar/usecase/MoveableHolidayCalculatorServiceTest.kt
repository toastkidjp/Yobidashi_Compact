package jp.toastkid.yobidashi.compact.calendar.usecase

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MoveableHolidayCalculatorServiceTest {

    private lateinit var moveableHolidayCalculatorService: MoveableHolidayCalculatorService

    @BeforeEach
    fun setUp() {
        moveableHolidayCalculatorService = MoveableHolidayCalculatorService()
    }

    @Test
    fun test() {
        assertFalse(moveableHolidayCalculatorService.invoke(2020, 1, 12))
        assertTrue(moveableHolidayCalculatorService.invoke(2020, 1, 13))
        assertFalse(moveableHolidayCalculatorService.invoke(2020, 1, 14))
        assertFalse(moveableHolidayCalculatorService.invoke(2020, 9, 20))
        assertTrue(moveableHolidayCalculatorService.invoke(2020, 9, 21))
        assertFalse(moveableHolidayCalculatorService.invoke(2020, 9, 22))
    }

}