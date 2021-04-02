package jp.toastkid.yobidashi.compact.editor.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JFrame
import javax.swing.JOptionPane

internal class DisposeActionServiceTest {

    @InjectMockKs
    private lateinit var disposeActionService: DisposeActionService

    @MockK
    private lateinit var frame: JFrame

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        every { frame.dispose() }.answers { Unit }

        mockkStatic(JOptionPane::class)
        every { JOptionPane.showConfirmDialog(any(), any()) }.returns(JOptionPane.OK_OPTION)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun invoke() {
        disposeActionService.invoke(false)

        verify(exactly = 1) { frame.dispose() }
        verify(exactly = 1) { JOptionPane.showConfirmDialog(any(), any()) }
    }

}