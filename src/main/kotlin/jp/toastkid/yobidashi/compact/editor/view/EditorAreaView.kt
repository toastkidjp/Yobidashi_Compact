package jp.toastkid.yobidashi.compact.editor.view

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import jp.toastkid.yobidashi.compact.editor.finder.FindOrder
import jp.toastkid.yobidashi.compact.editor.finder.FinderService
import jp.toastkid.yobidashi.compact.editor.popup.PopupMenuInitializer
import jp.toastkid.yobidashi.compact.editor.service.ClipboardPutterService
import jp.toastkid.yobidashi.compact.editor.service.KeyboardShortcutService
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.UrlOpenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.AbstractAction
import javax.swing.JComponent
import javax.swing.KeyStroke
import javax.swing.event.HyperlinkEvent

class EditorAreaView(
        private val editorArea: RSyntaxTextArea = RSyntaxTextArea(),
        private val syntaxHighlightApplier: SyntaxHighlightApplier = SyntaxHighlightApplier(),
        private val channel: Channel<MenuCommand>,
        private val messageChannel: Channel<String>
) {

    private val scrollArea: RTextScrollPane

    private val statusChannel: Channel<Int> = Channel()

    private val finderService by lazy { FinderService(editorArea, messageChannel) }

    init {
        refresh()

        editorArea.eolMarkersVisible = true
        editorArea.isWhitespaceVisible = true
        editorArea.addHyperlinkListener {
            if (it.eventType != HyperlinkEvent.EventType.ACTIVATED) {
                return@addHyperlinkListener
            }

            it.url?.toURI()?.let { uri ->
                UrlOpenerService().invoke(uri)
            }
        }
        editorArea.paintTabLines = true
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

        editorArea.inputMap.also {
            it.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK), "dupe")
            it.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK), "extract")
        }

        editorArea.actionMap.also {
            it.put("dupe", object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent?) {
                    duplicateLine()
                }
            })
            it.put("extract", object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent?) {
                    extractLine()
                }
            })
        }

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
        CoroutineScope(Dispatchers.Default).launch {
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

    fun replaceSelected(keepSelection: Boolean = false, action: (String) -> String) {
        editorArea.selectedText?.also { text ->
            val selectionStart = editorArea.selectionStart
            val selectionEnd = editorArea.selectionEnd

            editorArea.replaceSelection(action(text))

            if (keepSelection) {
                editorArea.selectionStart = selectionStart
                editorArea.selectionEnd = selectionEnd
            }
        }
    }

    fun count(): Long {
        return editorArea.selectedText.trim().codePoints().count()
    }

    fun selectedText(): String {
        return editorArea.selectedText.trim()
    }

    fun duplicateLine() {
        if (editorArea.selectedText?.isNotEmpty() == true) {
            editorArea.insert(editorArea.selectedText, editorArea.caretPosition)
            return
        }
        val start = editorArea.lineStartOffsetOfCurrentLine
        val end = editorArea.lineEndOffsetOfCurrentLine
        val currentLineText = editorArea.getText(start, end - start)
        editorArea.insert(currentLineText, end)
    }

    fun extractLine() {
        if (editorArea.selectedText?.isNotEmpty() == true) {
            editorArea.cut()
            return
        }
        val start = editorArea.lineStartOffsetOfCurrentLine
        val end = editorArea.lineEndOffsetOfCurrentLine
        val currentLineText = editorArea.getText(start, end - start)
        ClipboardPutterService().invoke(currentLineText)
        editorArea.replaceRange("", start, end)
    }

    fun switchEditable() {
        editorArea.isEditable = editorArea.isEditable.not()
    }

    fun isEditable(): Boolean {
        return editorArea.isEditable
    }

    fun toTop() {
        editorArea.caretPosition = 0
    }

    fun toBottom() {
        editorArea.caretPosition = editorArea.document.length
    }

    fun setStyle(extension: String) {
        syntaxHighlightApplier(editorArea, extension)
    }

    fun refresh() {
        editorArea.lineWrap = Setting.wrapLine()
        editorArea.foreground = Setting.editorForegroundColor()
        editorArea.background = Setting.editorBackgroundColor()
        val editorFontFamily = Setting.editorFontFamily() ?: return
        val fontSize = Setting.editorFontSize()
        val font = try {
            Font(editorFontFamily, editorArea.font.style, fontSize)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } ?: return
        editorArea.font = font
    }

    companion object {

        private const val DEFAULT_FONT_SIZE = 14f

    }

}