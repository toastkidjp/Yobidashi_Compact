package jp.toastkid.yobidashi.compact.calendar.view

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Dimension
import java.awt.Font
import java.time.DayOfWeek
import javax.swing.JLabel

internal class DayOfWeekLabelFactoryTest {

    private lateinit var dayOfWeekLabelFactory: DayOfWeekLabelFactory

    @MockK
    private lateinit var font: Font

    @BeforeEach
    fun setUp() {
        dayOfWeekLabelFactory = DayOfWeekLabelFactory(Dimension(200, 200))

        MockKAnnotations.init(this)
        every { font.deriveFont(any<Float>()) }.answers { font }
        mockkConstructor(JLabel::class)
        every { anyConstructed<JLabel>().getFont() }.answers { font }
        every { anyConstructed<JLabel>().setFont(any()) }.just(Runs)
        every { anyConstructed<JLabel>().setHorizontalAlignment(any()) }.just(Runs)
        every { anyConstructed<JLabel>().setVerticalAlignment(any()) }.just(Runs)
        every { anyConstructed<JLabel>().setPreferredSize(any()) }.just(Runs)
        every { anyConstructed<JLabel>().setOpaque(any()) }.just(Runs)
        every { anyConstructed<JLabel>().setBorder(any()) }.answers { Unit }
        every { anyConstructed<JLabel>().setBackground(any()) }.answers { Unit }
        every { anyConstructed<JLabel>().setForeground(any()) }.answers { Unit }
    }
/*
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            dayOfWeekLabel.foreground = Color.RED
        } else if (dayOfWeek == DayOfWeek.SATURDAY) {
            dayOfWeekLabel.foreground = Color.BLUE
 */
    @Test
    fun testMondayCase() {
        dayOfWeekLabelFactory.invoke(DayOfWeek.MONDAY)
        verify (exactly = 1) { font.deriveFont(any<Float>()) }
        verify (exactly = 1) { anyConstructed<JLabel>().getFont() }
        verify (exactly = 1) { anyConstructed<JLabel>().setFont(any()) }
        verify (exactly = 1) { anyConstructed<JLabel>().setHorizontalAlignment(any()) }
        verify (exactly = 1) { anyConstructed<JLabel>().setVerticalAlignment(any()) }
        verify (exactly = 1) { anyConstructed<JLabel>().setPreferredSize(any()) }
        verify (exactly = 1) { anyConstructed<JLabel>().setOpaque(any()) }
        verify (exactly = 1) { anyConstructed<JLabel>().setBorder(any()) }
        verify (exactly = 1) { anyConstructed<JLabel>().setBackground(any()) }
        verify (exactly = 0) { anyConstructed<JLabel>().setForeground(any()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}