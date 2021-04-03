package jp.toastkid.yobidashi.compact.calendar.view

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Font
import javax.swing.JSpinner

internal class MonthChooserFactoryTest {

    private lateinit var monthChooserFactory: MonthChooserFactory

    @BeforeEach
    fun setUp() {
        monthChooserFactory = MonthChooserFactory()

        mockkConstructor(JSpinner::class)
        every { anyConstructed<JSpinner>().setModel(any()) }.answers { Unit }
        every { anyConstructed<JSpinner>().setFont(any()) }.answers { Unit }
        val font = mockk<Font>()
        every { font.deriveFont(any<Float>()) }.returns(font)
        every { anyConstructed<JSpinner>().getFont() }.returns(font)
        every { anyConstructed<JSpinner>().getEditor() }.returns(mockk())
        every { anyConstructed<JSpinner>().setPreferredSize(any()) }.answers { Unit }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun invoke() {
        monthChooserFactory.invoke()

        verify(exactly = 1) { anyConstructed<JSpinner>().setModel(any()) }
        verify(exactly = 1) { anyConstructed<JSpinner>().setFont(any()) }
        verify(exactly = 1) { anyConstructed<JSpinner>().getFont() }
        verify(exactly = 1) { anyConstructed<JSpinner>().getEditor() }
        verify(exactly = 1) { anyConstructed<JSpinner>().setPreferredSize(any()) }
    }

}