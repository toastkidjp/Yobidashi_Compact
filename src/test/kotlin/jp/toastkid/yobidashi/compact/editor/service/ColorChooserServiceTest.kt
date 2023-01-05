package jp.toastkid.yobidashi.compact.editor.service

import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color
import javax.swing.JColorChooser
import javax.swing.JOptionPane

internal class ColorChooserServiceTest {

    private lateinit var colorChooserService: ColorChooserService

    @BeforeEach
    fun setUp() {
        colorChooserService = ColorChooserService()

        mockkStatic(JOptionPane::class)
        every { JOptionPane.showConfirmDialog(any(), any()) }.returns(JOptionPane.OK_OPTION)

        mockkConstructor(JColorChooser::class)
        every { anyConstructed<JColorChooser>().getColor() }.returns(Color.BLACK)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testInvoke() {
        colorChooserService.invoke()

        verify(exactly = 1) { JOptionPane.showConfirmDialog(any(), any()) }
        verify(exactly = 1) { anyConstructed<JColorChooser>().color }
    }

    @Test
    fun testCancelCase() {
        every { JOptionPane.showConfirmDialog(any(), any()) }.returns(JOptionPane.CANCEL_OPTION)

        colorChooserService.invoke()

        verify(exactly = 1) { JOptionPane.showConfirmDialog(any(), any()) }
        verify(exactly = 0) { anyConstructed<JColorChooser>().color }
    }

}