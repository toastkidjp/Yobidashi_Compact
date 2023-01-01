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
        every { anyConstructed<JLabel>().font }.answers { font }
        every { anyConstructed<JLabel>().font = any() }.just(Runs)
        every { anyConstructed<JLabel>().horizontalAlignment = any() }.just(Runs)
        every { anyConstructed<JLabel>().verticalAlignment = any() }.just(Runs)
        every { anyConstructed<JLabel>().preferredSize = any() }.just(Runs)
        every { anyConstructed<JLabel>().isOpaque = any() }.just(Runs)
        every { anyConstructed<JLabel>().border = any() }.just(Runs)
        every { anyConstructed<JLabel>().background = any() }.just(Runs)
        every { anyConstructed<JLabel>().foreground = any() }.just(Runs)
    }

    @Test
    fun testMondayCase() {
        dayOfWeekLabelFactory.invoke(DayOfWeek.MONDAY)
        verify (exactly = 1) { font.deriveFont(any<Float>()) }
        verify (exactly = 1) { anyConstructed<JLabel>().font }
        verify (exactly = 1) { anyConstructed<JLabel>().font = any() }
        verify (exactly = 1) { anyConstructed<JLabel>().horizontalAlignment = any() }
        verify (exactly = 1) { anyConstructed<JLabel>().verticalAlignment = any() }
        verify (exactly = 1) { anyConstructed<JLabel>().preferredSize = any() }
        verify (exactly = 1) { anyConstructed<JLabel>().isOpaque = any() }
        verify (exactly = 1) { anyConstructed<JLabel>().border = any() }
        verify (exactly = 1) { anyConstructed<JLabel>().background = any() }
        verify (exactly = 0) { anyConstructed<JLabel>().setForeground(any()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}