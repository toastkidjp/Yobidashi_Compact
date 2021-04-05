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
import java.time.Month
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSpinner

internal class ChooserPanelFactoryTest {

    @InjectMockKs
    private lateinit var chooserPanelFactory: ChooserPanelFactory

    @MockK
    private lateinit var refreshDayLabels: (Int, Month) -> Unit

    @MockK
    private lateinit var monthChooserFactory: MonthChooserFactory

    @MockK
    private lateinit var yearChooserFactory: YearChooserFactory

    @MockK
    private lateinit var mockSpinner: JSpinner

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        every { refreshDayLabels.invoke(any(), any()) }.answers { Unit }
        every { monthChooserFactory.invoke() }.returns(mockSpinner)
        every { yearChooserFactory.invoke() }.returns(mockSpinner)
        every { mockSpinner.setValue(any()) }.answers { Unit }
        every { mockSpinner.addChangeListener(any()) }.answers { Unit }

        mockkConstructor(JPanel::class)
        every { anyConstructed<JPanel>().add(any<JComponent>()) }.returns(mockk())
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testInvoke() {
        chooserPanelFactory.invoke()

        verify(exactly = 1) { monthChooserFactory.invoke() }
        verify(exactly = 1) { yearChooserFactory.invoke() }
        verify(atLeast = 1) { mockSpinner.setValue(any()) }
        verify(atLeast = 1) { mockSpinner.addChangeListener(any()) }
        verify(atLeast = 1) { anyConstructed<JPanel>().add(any<JComponent>()) }
    }

}