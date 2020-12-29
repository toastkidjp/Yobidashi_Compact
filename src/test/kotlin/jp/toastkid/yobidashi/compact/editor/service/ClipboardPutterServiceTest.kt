package jp.toastkid.yobidashi.compact.editor.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.datatransfer.Clipboard

internal class ClipboardPutterServiceTest {

    private lateinit var clipboardPutterService: ClipboardPutterService

    @MockK
    private lateinit var clipboard: Clipboard

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        clipboardPutterService = ClipboardPutterService(clipboard)

        every { clipboard.setContents(any(), any()) }.answers { Unit }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testInvoke() {
        clipboardPutterService("test")

        verify(exactly = 1) { clipboard.setContents(any(), any()) }
    }

}