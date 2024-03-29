package jp.toastkid.yobidashi.compact.editor.view

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.editor.MenuCommand
import jp.toastkid.yobidashi.compact.editor.finder.FindOrder
import jp.toastkid.yobidashi.compact.editor.finder.FinderService
import jp.toastkid.yobidashi.compact.editor.popup.PopupMenuInitializer
import jp.toastkid.yobidashi.compact.model.Setting
import kotlinx.coroutines.channels.Channel
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.Gutter
import org.fife.ui.rtextarea.RTextScrollPane
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color
import java.awt.Font

internal class EditorAreaViewTest {

    private lateinit var editorAreaView: EditorAreaView

    private lateinit var editorArea: RSyntaxTextArea

    @MockK
    private lateinit var syntaxHighlightApplier: SyntaxHighlightApplier

    @MockK
    private lateinit var channel: Channel<MenuCommand>

    @MockK
    private lateinit var messageChannel: Channel<String>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        val font = mockk<Font>()

        editorArea = spyk(RSyntaxTextArea())
        every { editorArea.font }.returns(font)
        every { editorArea.addHyperlinkListener(any()) }.just(Runs)
        every { editorArea.paintTabLines = any() }.just(Runs)
        every { editorArea.addKeyListener(any()) }.just(Runs)
        every { editorArea.foreground = any() }.just(Runs)
        every { editorArea.background = any() }.just(Runs)
        every { editorArea.font = any() }.just(Runs)
        every { editorArea.popupMenu }.returns(mockk())

        mockkObject(Setting)
        every { Setting.editorForegroundColor() }.returns(Color.BLACK)
        every { Setting.editorBackgroundColor() }.returns(Color.WHITE)
        every { Setting.editorFontFamily() }.returns("normal")
        every { Setting.editorFontSize() }.returns(12)

        every { font.deriveFont(any<Float>()) }.answers { font }
        every { font.style }.returns(Font.PLAIN)

        mockkConstructor(RTextScrollPane::class)
        every { anyConstructed<RTextScrollPane>().lineNumbersEnabled = any() }.just(Runs)
        every { anyConstructed<RTextScrollPane>().isIconRowHeaderEnabled = any() }.just(Runs)

        val gutter = mockk<Gutter>()
        every { anyConstructed<RTextScrollPane>().gutter }.answers { gutter }
        every { gutter.lineNumberFont = any() }.just(Runs)

        mockkConstructor(PopupMenuInitializer::class)
        every { anyConstructed<PopupMenuInitializer>().invoke() }.just(Runs)

        editorAreaView = EditorAreaView(editorArea, syntaxHighlightApplier, channel, messageChannel)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun view() {
        assertNotNull(editorAreaView.view())
    }

    @Test
    fun testSetText() {
        editorAreaView.setText("test")

        assertEquals("test", editorArea.text)
    }

    @Test
    fun testGetTextArray() {
        every { editorArea.text }.returns("test")

        val textArray = editorAreaView.getTextArray()

        assertEquals("test", String(textArray))
        verify(exactly = 1) { editorArea.text }
    }

    @Test
    fun testReceiveStatus() {
        editorAreaView.receiveStatus {  }
    }

    @Test
    fun testInsertText() {
        every { editorArea.insert(any(), any()) }.just(Runs)
        every { editorArea.caretPosition }.returns(0)

        editorAreaView.insertText("insert")

        verify(exactly = 1) { editorArea.insert("insert", 0) }
    }

    @Test
    fun testFind() {
        mockkConstructor(FinderService::class)
        every { anyConstructed<FinderService>().invoke(any()) }.just(Runs)

        editorAreaView.find(FindOrder("test", "replaced"))

        verify(exactly = 1) { anyConstructed<FinderService>().invoke(any()) }
    }

    @Test
    fun testReplaceSelectedPassingNull() {
        every { editorArea.selectedText }.returns(null)
        every { editorArea.selectionStart }.returns(2)
        every { editorArea.selectionEnd }.returns(3)
        every { editorArea.replaceSelection(any()) }.just(Runs)

        editorAreaView.replaceSelected(false) { "replaced" }

        verify(exactly = 1) { editorArea.selectedText }
        verify(exactly = 0) { editorArea.selectionStart }
        verify(exactly = 0) { editorArea.selectionEnd }
        verify(exactly = 0) { editorArea.replaceSelection(any()) }
    }

    @Test
    fun testReplaceSelected() {
        every { editorArea.selectedText }.returns("test")
        every { editorArea.selectionStart }.returns(2)
        every { editorArea.selectionEnd }.returns(3)
        every { editorArea.replaceSelection(any()) }.just(Runs)

        editorAreaView.replaceSelected(false) { "replaced" }

        verify(exactly = 1) { editorArea.selectedText }
        verify(exactly = 1) { editorArea.selectionStart }
        verify(exactly = 1) { editorArea.selectionEnd }
        verify(exactly = 1) { editorArea.replaceSelection(any()) }
    }

    @Test
    fun testReplaceSelectedWithKeepSelection() {
        every { editorArea.selectedText }.returns("test")
        every { editorArea.selectionStart }.returns(2)
        every { editorArea.selectionEnd }.returns(3)
        every { editorArea.replaceSelection(any()) }.just(Runs)
        every { editorArea.selectionStart = any() }.just(Runs)
        every { editorArea.selectionEnd = any() }.just(Runs)

        editorAreaView.replaceSelected(true, { "replaced" })

        verify(exactly = 1) { editorArea.selectedText }
        verify(exactly = 1) { editorArea.selectionStart }
        verify(exactly = 1) { editorArea.selectionEnd }
        verify(exactly = 1) { editorArea.replaceSelection(any()) }
        verify(exactly = 1) { editorArea.selectionStart = 2 }
        verify(exactly = 1) { editorArea.selectionEnd = 3 }
    }

    @Test
    fun testCount() {
        every { editorArea.selectedText }.returns(" abc ")

        val count = editorAreaView.count()

        verify(exactly = 1) { editorArea.selectedText }
        assertEquals(3, count)
    }

    @Test
    fun testSelectedText() {
        every { editorArea.selectedText }.returns(" abc ")

        val selectedText = editorAreaView.selectedText()

        verify(exactly = 1) { editorArea.selectedText }
        assertEquals("abc", selectedText)
    }

    @Test
    fun testDuplicateLine() {
        every { editorArea.lineStartOffsetOfCurrentLine }.returns(0)
        every { editorArea.lineEndOffsetOfCurrentLine }.returns(7)
        every { editorArea.getText(any(), any()) }.returns("extracted")
        every { editorArea.insert(any(), any()) }.just(Runs)

        editorAreaView.duplicateLine()

        verify(exactly = 1) { editorArea.lineStartOffsetOfCurrentLine }
        verify(exactly = 1) { editorArea.lineEndOffsetOfCurrentLine }
        verify(exactly = 1) { editorArea.getText(0, 7) }
        verify(exactly = 1) { editorArea.insert("extracted", 7) }
    }

    @Test
    fun testSwitchEditable() {
        every { editorArea.isEditable }.returns(true)
        every { editorArea.isEditable = any() }.just(Runs)

        editorAreaView.switchEditable()

        verify(exactly = 1) { editorArea.isEditable }
        verify(exactly = 1) { editorArea.isEditable = false }
    }

}