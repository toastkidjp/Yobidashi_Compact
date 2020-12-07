package jp.toastkid.yobidashi.compact.calendar.service

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpecialCaseOffDayCalculatorServiceTest {

    private lateinit var specialCaseOffDayCalculatorService: SpecialCaseOffDayCalculatorService

    @BeforeEach
    fun setUp() {
        specialCaseOffDayCalculatorService = SpecialCaseOffDayCalculatorService()
    }

    @Test
    fun testNormalDay() {
        val result = specialCaseOffDayCalculatorService.invoke(2019, 1, 2)

        assertFalse(result.first)
        assertFalse(result.second)
    }

    @Test
    fun test2019_4() {
        val (isOffDay, forceNormal) = specialCaseOffDayCalculatorService.invoke(2019, 4, 30)

        assertTrue(isOffDay)
        assertFalse(forceNormal)
    }

    @Test
    fun test2020_7_20() {
        val (isOffDay, forceNormal) = specialCaseOffDayCalculatorService.invoke(2020, 7, 20)

        assertFalse(isOffDay)
        assertTrue(forceNormal)
    }

    @Test
    fun test2020_8_11() {
        val (isOffDay, forceNormal) = specialCaseOffDayCalculatorService.invoke(2020, 8, 11)

        assertFalse(isOffDay)
        assertTrue(forceNormal)
    }

}