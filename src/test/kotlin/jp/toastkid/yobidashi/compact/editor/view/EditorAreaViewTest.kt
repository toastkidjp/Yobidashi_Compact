package jp.toastkid.yobidashi.compact.editor.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
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
    private lateinit var channel: Channel<MenuCommand>

    @MockK
    private lateinit var messageChannel: Channel<String>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        val font = mockk<Font>()

        editorArea = spyk(RSyntaxTextArea())
        every { editorArea.getFont() }.returns(font)
        every { editorArea.addHyperlinkListener(any()) }.answers { Unit }
        every { editorArea.setPaintTabLines(any()) }.answers { Unit }
        every { editorArea.addKeyListener(any()) }.answers { Unit }
        every { editorArea.setForeground(any()) }.answers { Unit }
        every { editorArea.setBackground(any()) }.answers { Unit }
        every { editorArea.setFont(any()) }.answers { Unit }
        every { editorArea.getPopupMenu() }.returns(mockk())

        mockkObject(Setting)
        every { Setting.editorForegroundColor() }.returns(Color.BLACK)
        every { Setting.editorBackgroundColor() }.returns(Color.WHITE)
        every { Setting.editorFontFamily() }.returns("normal")
        every { Setting.editorFontSize() }.returns(12)

        every { font.deriveFont(any<Float>()) }.answers { font }
        every { font.getStyle() }.returns(Font.PLAIN)

        mockkConstructor(RTextScrollPane::class)
        every { anyConstructed<RTextScrollPane>().setLineNumbersEnabled(any()) }.answers { Unit }
        every { anyConstructed<RTextScrollPane>().setIconRowHeaderEnabled(any()) }.answers { Unit }

        val gutter = mockk<Gutter>()
        every { anyConstructed<RTextScrollPane>().getGutter() }.answers { gutter }
        every { gutter.setLineNumberFont(any()) }.answers { Unit }

        mockkConstructor(PopupMenuInitializer::class)
        every { anyConstructed<PopupMenuInitializer>().invoke() }.answers { Unit }

        editorAreaView = EditorAreaView(editorArea, channel, messageChannel)
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
    fun setText() {
        editorAreaView.setText("test")

        assertEquals("test", editorArea.text)
    }

    @Test
    fun getTextArray() {
        every { editorArea.getText() }.returns("test")

        val textArray = editorAreaView.getTextArray()

        assertEquals("test", String(textArray))
        verify(exactly = 1) { editorArea.getText() }
    }

    @Test
    fun receiveStatus() {
        editorAreaView.receiveStatus {  }
    }

    @Test
    fun insertText() {
        every { editorArea.insert(any(), any()) }.answers { Unit }
        every { editorArea.getCaretPosition() }.returns(0)

        editorAreaView.insertText("insert")

        verify(exactly = 1) { editorArea.insert("insert", 0) }
    }

    @Test
    fun find() {
        mockkConstructor(FinderService::class)
        every { anyConstructed<FinderService>().invoke(any()) }.answers { Unit }

        editorAreaView.find(FindOrder("test", "replaced"))

        verify(exactly = 1) { anyConstructed<FinderService>().invoke(any()) }
    }

    @Test
    fun replaceSelectedPassingNull() {
        every { editorArea.getSelectedText() }.returns(null)
        every { editorArea.getSelectionStart() }.returns(2)
        every { editorArea.getSelectionEnd() }.returns(3)
        every { editorArea.replaceSelection(any()) }.answers { Unit }

        editorAreaView.replaceSelected(false, { "replaced" })

        verify(exactly = 1) { editorArea.getSelectedText() }
        verify(exactly = 0) { editorArea.getSelectionStart() }
        verify(exactly = 0) { editorArea.getSelectionEnd() }
        verify(exactly = 0) { editorArea.replaceSelection(any()) }
    }

    @Test
    fun count() {
        every { editorArea.getSelectedText() }.returns(" abc ")

        val count = editorAreaView.count()

        verify(exactly = 1) { editorArea.getSelectedText() }
        assertEquals(3, count)
    }

    @Test
    fun selectedText() {
        every { editorArea.getSelectedText() }.returns(" abc ")

        val selectedText = editorAreaView.selectedText()

        verify(exactly = 1) { editorArea.getSelectedText() }
        assertEquals("abc", selectedText)
    }

    @Test
    fun duplicateLine() {
        every { editorArea.getLineStartOffsetOfCurrentLine() }.returns(0)
        every { editorArea.getLineEndOffsetOfCurrentLine() }.returns(7)
        every { editorArea.getText(any(), any()) }.returns("extracted")
        every { editorArea.insert(any(), any()) }.answers { Unit }

        editorAreaView.duplicateLine()

        verify(exactly = 1) { editorArea.getLineStartOffsetOfCurrentLine() }
        verify(exactly = 1) { editorArea.getLineEndOffsetOfCurrentLine() }
        verify(exactly = 1) { editorArea.getText(0, 7) }
        verify(exactly = 1) { editorArea.insert("extracted", 7) }
    }

    @Test
    fun switchEditable() {
        every { editorArea.isEditable() }.returns(true)
        every { editorArea.setEditable(any()) }.answers { Unit }

        editorAreaView.switchEditable()

        verify(exactly = 1) { editorArea.isEditable() }
        verify(exactly = 1) { editorArea.setEditable(false) }
    }

}