package jp.toastkid.yobidashi.compact.calendar.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JComponent
import javax.swing.JPanel

internal class DayPanelFactoryTest {

    @InjectMockKs
    private lateinit var dayPanelFactory: DayPanelFactory

    @MockK
    private lateinit var dayOfWeekLabelFactory: DayOfWeekLabelFactory

    @MockK
    private lateinit var dayLabelFactory: DayLabelFactory

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        every { dayOfWeekLabelFactory.invoke(any()) }.returns(mockk())
        every { dayLabelFactory.invoke(any()) }.returns(mockk())

        mockkConstructor(JPanel::class)
        every { anyConstructed<JPanel>().add(any<JComponent>()) }.returns(mockk())
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testInvoke() {
        dayPanelFactory.invoke()

        verify(atLeast = 1) { dayOfWeekLabelFactory.invoke(any()) }
        verify(atLeast = 1) { dayLabelFactory.invoke(any()) }
        verify(atLeast = 1) { anyConstructed<JPanel>().add(any<JComponent>()) }
    }

}