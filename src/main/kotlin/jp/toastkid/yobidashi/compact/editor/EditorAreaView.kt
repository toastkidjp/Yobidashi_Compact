package jp.toastkid.yobidashi.compact.editor

import jp.toastkid.yobidashi.compact.editor.finder.FindOrder
import jp.toastkid.yobidashi.compact.editor.popup.PopupMenuInitializer
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

class EditorAreaView(private val editorArea: RSyntaxTextArea = RSyntaxTextArea()) {

    private val scrollArea: RTextScrollPane

    private val statusChannel: Channel<Int> = Channel()

    init {
        editorArea.background = Color(225, 225, 225, 255)
        editorArea.paintTabLines = true
        editorArea.font = editorArea.font.deriveFont(DEFAULT_FONT_SIZE)
        editorArea.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) = Unit

            override fun keyPressed(e: KeyEvent?) = Unit

            override fun keyReleased(e: KeyEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    statusChannel.send(editorArea.text.length)
                }
            }
        })

        PopupMenuInitializer(editorArea).invoke()

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

    private var lastFound = 0

    fun find(order: FindOrder) {
        if (order.invokeReplace) {
            // TODO
            return
        }

        val indexOf = if (order.upper) {
            editorArea.text.lastIndexOf(order.target, lastFound - 1)
        } else {
            editorArea.text.indexOf(order.target, lastFound + 1)
        }
        if (indexOf == -1) {
            return
        }
        lastFound = indexOf

        editorArea.selectionStart = indexOf
        editorArea.selectionEnd = indexOf + order.target.length
    }

    companion object {
        private const val DEFAULT_FONT_SIZE = 16f
    }

}