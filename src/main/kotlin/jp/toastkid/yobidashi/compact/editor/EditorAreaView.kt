package jp.toastkid.yobidashi.compact.editor

import jp.toastkid.yobidashi.compact.editor.finder.FindOrder
import jp.toastkid.yobidashi.compact.editor.finder.FinderService
import jp.toastkid.yobidashi.compact.editor.popup.PopupMenuInitializer
import jp.toastkid.yobidashi.compact.editor.service.KeyboardShortcutService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.Color
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JComponent

class EditorAreaView(
        private val editorArea: RSyntaxTextArea = RSyntaxTextArea(),
        private val channel: Channel<MenuCommand>
) {

    private val scrollArea: RTextScrollPane

    private val statusChannel: Channel<Int> = Channel()

    private val finderService by lazy { FinderService(editorArea) }

    init {
        editorArea.background = Color(225, 225, 225, 255)
        editorArea.paintTabLines = true
        editorArea.font = editorArea.font.deriveFont(DEFAULT_FONT_SIZE)
        editorArea.addKeyListener(object : KeyListener {
            private val keyboardShortcutService = KeyboardShortcutService(channel)

            override fun keyTyped(e: KeyEvent?) = Unit

            override fun keyPressed(e: KeyEvent?) {
                if (e == null) {
                    return
                }

                keyboardShortcutService(e)
            }

            override fun keyReleased(e: KeyEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    statusChannel.send(editorArea.text.length)
                }
            }
        })

        PopupMenuInitializer(editorArea.popupMenu, channel).invoke()

        scrollArea = RTextScrollPane(editorArea)
        scrollArea.lineNumbersEnabled = true
        scrollArea.isIconRowHeaderEnabled = true
        scrollArea.gutter.lineNumberFont = editorArea.font.deriveFont(DEFAULT_FONT_SIZE)
    }

    fun view(): JComponent {
        return scrollArea
    }

    fun setText(text: String) {
        editorArea.text = text
        editorArea.caretPosition = 0
    }

    fun getTextArray(): ByteArray {
        return editorArea.text.toByteArray()
    }

    fun receiveStatus(receiver: (Int) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            statusChannel.receiveAsFlow().collect {
                receiver(editorArea.text.length)
            }
        }
    }

    fun insertText(text: String) {
        editorArea.insert(text, editorArea.caretPosition)
    }

    fun find(order: FindOrder) {
        finderService.invoke(order)
    }

    fun replaceSelected(action: (String) -> String) {
        editorArea.selectedText?.also { text ->
            editorArea.replaceSelection(action(text))
        }
    }

    fun count(): Long {
        return editorArea.selectedText.trim().codePoints().count()
    }

    fun selectedText(): String? {
        return editorArea.selectedText.trim()
    }

    companion object {
        private const val DEFAULT_FONT_SIZE = 14f
    }

}