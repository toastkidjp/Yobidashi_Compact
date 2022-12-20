package jp.toastkid.yobidashi.compact.calendar.view

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.calendar.service.DayLabelRefresherService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import javax.swing.JLabel
import javax.swing.JPanel

internal class CalendarPanelTest {

    @BeforeEach
    fun setUp() {
        mockkConstructor(DayPanelFactory::class)
        every { anyConstructed<DayPanelFactory>().invoke() }
            .returns(JPanel() to Array(6) { arrayOfNulls<JLabel>(7) })
        mockkConstructor(DayLabelRefresherService::class)
        every { anyConstructed<DayLabelRefresherService>().invoke(any<Int>(), any()) }.returns(mockk())
        every { anyConstructed<DayLabelRefresherService>().invoke(any<LocalDate>(), any()) }.returns(mockk())
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun test() {
        assertNotNull(CalendarPanel())

        verify(exactly = 1) { anyConstructed<DayPanelFactory>().invoke() }
        verify(atLeast = 1) { anyConstructed<DayLabelRefresherService>().invoke(any<Int>(), any()) }
        verify(atLeast = 1) { anyConstructed<DayLabelRefresherService>().invoke(any<LocalDate>(), any()) }
    }

}