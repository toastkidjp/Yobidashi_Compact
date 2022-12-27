package jp.toastkid.yobidashi.compact.editor.finder

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.channels.Channel
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FinderServiceTest {

    private lateinit var finderService: FinderService

    @MockK
    private lateinit var editorArea: RSyntaxTextArea

    @MockK
    private lateinit var messageChannel: Channel<String>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { messageChannel.send(any()) }.just(Runs)
        every { editorArea.setSelectionStart(any()) }.answers { Unit }
        every { editorArea.setSelectionEnd(any()) }.answers { Unit }

        finderService = FinderService(editorArea, messageChannel)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testFirstString() {
        every { editorArea.getText() }.returns("ACB news will go bankrupt.")

        finderService.invoke(FindOrder("ACB", "", caseSensitive = true))

        verify(exactly = 1) { editorArea.setSelectionStart(0) }
        verify(exactly = 1) { editorArea.setSelectionEnd(3) }
    }

    @Test
    fun test() {
        every { editorArea.getText() }.returns("ACB news will go bankrupt.")

        finderService.invoke(FindOrder("NEWS", "", caseSensitive = false))

        verify(exactly = 1) { editorArea.setSelectionStart(4) }
        verify(exactly = 1) { editorArea.setSelectionEnd(8) }
    }

    @Test
    fun testFirstStringUpper() {
        every { editorArea.getText() }.returns("ACB news will go bankrupt.")

        finderService.invoke(FindOrder("ACB", "", upper = true))

        verify(exactly = 1) { editorArea.setSelectionStart(0) }
        verify(exactly = 1) { editorArea.setSelectionEnd(3) }
    }

    @Test
    fun testReplace() {
        every { editorArea.getText() }.returns("ACB news will go bankrupt ACB.")
        every { editorArea.replaceRange(any(), any(), any()) }.answers { Unit }

        finderService.invoke(FindOrder("acb", "BXX", invokeReplace = true, caseSensitive = false))

        verify(exactly = 0) { editorArea.setSelectionStart(any()) }
        verify(exactly = 0) { editorArea.setSelectionEnd(any()) }
        verify(exactly = 2) { editorArea.replaceRange(any(), any(), any()) }
    }

    @Test
    fun testNotFoundCase() {
        every { editorArea.getText() }.returns("ACB news will go bankrupt ACB.")
        every { editorArea.replaceRange(any(), any(), any()) }.answers { Unit }

        finderService.invoke(FindOrder("axx", "BXX", invokeReplace = true, caseSensitive = false))

        verify(exactly = 0) { editorArea.setSelectionStart(any()) }
        verify(exactly = 0) { editorArea.setSelectionEnd(any()) }
        verify(exactly = 0) { editorArea.replaceRange(any(), any(), any()) }
    }

}