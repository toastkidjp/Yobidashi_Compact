package jp.toastkid.yobidashi.compact.calendar.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DayOfWeekTest {

    @Test
    fun testGetName_minus() {
        assertTrue(DayOfWeek.getName(-1).isEmpty())
    }

    @Test
    fun testGetName() {
        assertEquals("WED", DayOfWeek.getName(3))
    }

    @Test
    fun testGetName_over() {
        assertTrue(DayOfWeek.getName(7).isEmpty())
    }

}