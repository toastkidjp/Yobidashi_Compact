package jp.toastkid.yobidashi.compact.calendar.service

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserOffDayServiceTest {

    private lateinit var userOffDayService: UserOffDayService

    @BeforeEach
    fun setUp() {
        userOffDayService = UserOffDayService(listOf(12 to 29))
    }

    @Test
    fun testInvoke() {
        assertFalse(userOffDayService(12, 3))
        assertFalse(userOffDayService(11, 29))
        assertTrue(userOffDayService(12, 29))
    }

}