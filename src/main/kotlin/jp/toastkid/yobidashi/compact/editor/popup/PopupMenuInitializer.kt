package jp.toastkid.yobidashi.compact.editor.popup

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import java.awt.Desktop
import java.awt.event.ActionEvent
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.swing.AbstractAction
import javax.swing.JMenuItem

class PopupMenuInitializer(private val editorArea: RSyntaxTextArea, private val channel: Channel<MenuCommand>) {

    operator fun invoke() {
        val toTableMenu = JMenuItem()
        toTableMenu.action = object : AbstractAction("To table") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.TO_TABLE)
                }
            }
        }
        editorArea.popupMenu.add(toTableMenu)

        val blockQuotationMenu = JMenuItem()
        blockQuotationMenu.action = object : AbstractAction("Block quote") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.BLOCKQUOTE)
                }
            }
        }
        editorArea.popupMenu.add(blockQuotationMenu)

        val hyphenListMenu = JMenuItem()
        hyphenListMenu.action = object : AbstractAction("Unordered list") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.UNORDERED_LIST)
                }
            }
        }
        editorArea.popupMenu.add(hyphenListMenu)

        val numberedListMenu = JMenuItem()
        numberedListMenu.action = object : AbstractAction("Ordered list") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.ORDERED_LIST)
                }
            }
        }
        editorArea.popupMenu.add(numberedListMenu)

        val taskListMenu = JMenuItem()
        taskListMenu.action = object : AbstractAction("Task list") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.TASK_LIST)
                }
            }
        }
        editorArea.popupMenu.add(taskListMenu)

        val boldMenu = JMenuItem()
        boldMenu.action = object : AbstractAction("Bold") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.BOLD)
                }
            }
        }
        editorArea.popupMenu.add(boldMenu)

        val italicMenu = JMenuItem()
        italicMenu.action = object : AbstractAction("Italic") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.ITALIC)
                }
            }
        }
        editorArea.popupMenu.add(italicMenu)

        val strikethroughMenu = JMenuItem()
        strikethroughMenu.action = object : AbstractAction("Strikethrough") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.STRIKETHROUGH)
                }
            }
        }
        editorArea.popupMenu.add(strikethroughMenu)

        val codeBlockMenu = JMenuItem()
        codeBlockMenu.action = object : AbstractAction("Code block") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.CODE_BLOCK)
                }
            }
        }
        editorArea.popupMenu.add(codeBlockMenu)

        val fontColorMenu = JMenuItem()
        fontColorMenu.action = object : AbstractAction("Font color") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.FONT_COLOR)
                }
            }
        }
        editorArea.popupMenu.add(fontColorMenu)

        val countMenu = JMenuItem()
        countMenu.action = object : AbstractAction("Count") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.COUNT)
                }
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
}