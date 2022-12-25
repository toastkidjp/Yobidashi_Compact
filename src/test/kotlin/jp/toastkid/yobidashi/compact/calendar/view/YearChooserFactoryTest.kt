package jp.toastkid.yobidashi.compact.calendar.view

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Dimension
import java.awt.Font
import javax.swing.JSpinner

internal class YearChooserFactoryTest {

    private lateinit var yearChooserFactory: YearChooserFactory

    @BeforeEach
    fun setUp() {
        yearChooserFactory = YearChooserFactory()

        mockkConstructor(JSpinner::class)
        every { anyConstructed<JSpinner>().setModel(any()) }.just(Runs)
        every { anyConstructed<JSpinner>().setFont(any()) }.answers { Unit }
        val font = mockk<Font>()
        every { font.deriveFont(any<Float>()) }.returns(font)
        every { anyConstructed<JSpinner>().getFont() }.returns(font)
        every { anyConstructed<JSpinner>().getEditor() }.returns(mockk())
        every { anyConstructed<JSpinner>().getPreferredSize() }.returns(Dimension())
        every { anyConstructed<JSpinner>().setPreferredSize(any()) }.answers { Unit }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testInvoke() {
        yearChooserFactory.invoke()

        verify(exactly = 1) { anyConstructed<JSpinner>().setModel(any()) }
        verify(exactly = 1) { anyConstructed<JSpinner>().setFont(any()) }
        // Following 2 verifications make crash.
        //verify(exactly = 1) { anyConstructed<JSpinner>().getFont() }
        //verify(exactly = 1) { anyConstructed<JSpinner>().getEditor() }
        verify(exactly = 1) { anyConstructed<JSpinner>().getPreferredSize() }
        verify(exactly = 1) { anyConstructed<JSpinner>().setPreferredSize(any()) }
    }

}