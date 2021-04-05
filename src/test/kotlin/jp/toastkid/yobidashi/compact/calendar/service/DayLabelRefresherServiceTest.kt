package jp.toastkid.yobidashi.compact.calendar.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Month
import javax.swing.JLabel

internal class DayLabelRefresherServiceTest {

    private lateinit var dayLabelRefresherService: DayLabelRefresherService

    @BeforeEach
    fun setUp() {
        dayLabelRefresherService = DayLabelRefresherService(Array(6) { arrayOfNulls<JLabel>(7) })
    }

    @Test
    fun invoke() {
        dayLabelRefresherService.invoke(2021, Month.FEBRUARY)
    }

}