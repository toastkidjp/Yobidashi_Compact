package jp.toastkid.yobidashi.compact.editor

import jp.toastkid.yobidashi.compact.editor.text.BlockQuotation
import jp.toastkid.yobidashi.compact.editor.text.ListHeadAdder
import jp.toastkid.yobidashi.compact.editor.text.NumberedListHeadAdder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.Color
import java.awt.Desktop
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.swing.AbstractAction
import javax.swing.JComponent
import javax.swing.JMenuItem
import javax.swing.JOptionPane

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

        val hyphenListMenu = JMenuItem()
        hyphenListMenu.action = object : AbstractAction("Hyphen list") {
            override fun actionPerformed(e: ActionEvent?) {
                editorArea.selectedText.also { text ->
                    editorArea.replaceSelection(ListHeadAdder().invoke(text, "-"))
                }
            }
        }
        editorArea.popupMenu.add(hyphenListMenu)

        val numberedListMenu = JMenuItem()
        numberedListMenu.action = object : AbstractAction("Numbered list") {
            override fun actionPerformed(e: ActionEvent?) {
                editorArea.selectedText.also { text ->
                    editorArea.replaceSelection(NumberedListHeadAdder().invoke(text))
                }
            }
        }
        editorArea.popupMenu.add(numberedListMenu)

        val boldMenu = JMenuItem()
        boldMenu.action = object : AbstractAction("Bold") {
            override fun actionPerformed(e: ActionEvent?) {
                editorArea.selectedText.also { text ->
                    editorArea.replaceSelection("**$text**")
                }
            }
        }
        editorArea.popupMenu.add(boldMenu)

        val strikethroughMenu = JMenuItem()
        strikethroughMenu.action = object : AbstractAction("Strikethrough") {
            override fun actionPerformed(e: ActionEvent?) {
                editorArea.selectedText.also { text ->
                    editorArea.replaceSelection("~~$text~~")
                }
            }
        }
        editorArea.popupMenu.add(strikethroughMenu)

        val countMenu = JMenuItem()
        countMenu.action = object : AbstractAction("Count") {
            override fun actionPerformed(e: ActionEvent?) {
                JOptionPane.showMessageDialog(
                        null,
                        "Count: ${editorArea.selectedText.trim().codePoints().count()}"
                )
            }
        }
        editorArea.popupMenu.add(countMenu)

        val webSearchMenu = JMenuItem()
        webSearchMenu.action = object : AbstractAction("Web search") {
            override fun actionPerformed(e: ActionEvent?) {
                Desktop.getDesktop().browse(URI("https://search.yahoo.co.jp/search?p=${URLEncoder.encode(editorArea.selectedText, StandardCharsets.UTF_8.name())}"))
            }
        }
        editorArea.popupMenu.add(webSearchMenu)
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

    companion object {
        private const val DEFAULT_FONT_SIZE = 16f
    }

}