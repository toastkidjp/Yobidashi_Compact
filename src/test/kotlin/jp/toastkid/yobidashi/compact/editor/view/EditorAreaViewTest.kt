package jp.toastkid.yobidashi.compact.editor.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import jp.toastkid.yobidashi.compact.editor.MenuCommand
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
    }

    @Test
    fun receiveStatus() {
    }

    @Test
    fun insertText() {
    }

    @Test
    fun find() {
    }

    @Test
    fun replaceSelected() {
    }

    @Test
    fun count() {
    }

    @Test
    fun selectedText() {
    }

    @Test
    fun duplicateLine() {
    }

    @Test
    fun switchEditable() {
    }

    @Test
    fun refresh() {
    }

}