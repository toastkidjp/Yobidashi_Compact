package jp.toastkid.yobidashi.compact.editor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.AbstractAction
import javax.swing.JComponent
import javax.swing.JMenuItem

class EditorAreaView(private val editorArea: RSyntaxTextArea = RSyntaxTextArea()) {

    private val scrollArea: RTextScrollPane

    private val statusChannel: Channel<Int> = Channel()

    init {
        editorArea.background = Color(220, 220, 220, 220)
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

        initializePopupMenu()

        scrollArea = RTextScrollPane(editorArea)
        scrollArea.lineNumbersEnabled = true
        scrollArea.isIconRowHeaderEnabled = true
        scrollArea.gutter.lineNumberFont = editorArea.font.deriveFont(DEFAULT_FONT_SIZE)
    }

    private fun initializePopupMenu() {
        val toTableMenu = JMenuItem()
        toTableMenu.action = object : AbstractAction("To table") {
            override fun actionPerformed(e: ActionEvent?) {
                editorArea.selectedText.also { text ->
                    editorArea.replaceSelection(TableFormConverter().invoke(text))
                }
            }
        }
        editorArea.popupMenu.add(toTableMenu)

        val blockQuotationMenu = JMenuItem()
        blockQuotationMenu.action = object : AbstractAction("Block quote") {
            override fun actionPerformed(e: ActionEvent?) {
                editorArea.selectedText.also { text ->
                    editorArea.replaceSelection(BlockQuotation().invoke(text))
                }
            }
        }
        editorArea.popupMenu.add(blockQuotationMenu)
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
            receiver(editorArea.text.length)
        }
    }

    companion object {
        private const val DEFAULT_FONT_SIZE = 16f
    }

}