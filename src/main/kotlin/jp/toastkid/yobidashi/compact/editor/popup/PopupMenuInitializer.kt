package jp.toastkid.yobidashi.compact.editor.popup

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

class PopupMenuInitializer(private val popupMenu: JPopupMenu, private val channel: Channel<MenuCommand>) {

    operator fun invoke() {
        val toTableMenu = JMenuItem("To table")
        toTableMenu.addActionListener {
            CoroutineScope(Dispatchers.Default).launch {
                channel.send(MenuCommand.TO_TABLE)
            }
        }
        popupMenu.add(toTableMenu)

        val blockQuotationMenu = JMenuItem()
        blockQuotationMenu.action = object : AbstractAction("Block quote") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.BLOCKQUOTE)
                }
            }
        }
        popupMenu.add(blockQuotationMenu)

        val hyphenListMenu = JMenuItem()
        hyphenListMenu.action = object : AbstractAction("Unordered list") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.UNORDERED_LIST)
                }
            }
        }
        popupMenu.add(hyphenListMenu)

        val numberedListMenu = JMenuItem()
        numberedListMenu.action = object : AbstractAction("Ordered list") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.ORDERED_LIST)
                }
            }
        }
        popupMenu.add(numberedListMenu)

        val taskListMenu = JMenuItem()
        taskListMenu.action = object : AbstractAction("Task list") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.TASK_LIST)
                }
            }
        }
        popupMenu.add(taskListMenu)

        val boldMenu = JMenuItem()
        boldMenu.action = object : AbstractAction("Bold") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.BOLD)
                }
            }
        }
        popupMenu.add(boldMenu)

        val italicMenu = JMenuItem()
        italicMenu.action = object : AbstractAction("Italic") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.ITALIC)
                }
            }
        }
        popupMenu.add(italicMenu)

        val strikethroughMenu = JMenuItem()
        strikethroughMenu.action = object : AbstractAction("Strikethrough") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.STRIKETHROUGH)
                }
            }
        }
        popupMenu.add(strikethroughMenu)

        val codeBlockMenu = JMenuItem()
        codeBlockMenu.action = object : AbstractAction("Code block") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.CODE_BLOCK)
                }
            }
        }
        popupMenu.add(codeBlockMenu)

        val fontColorMenu = JMenuItem()
        fontColorMenu.action = object : AbstractAction("Font color") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.FONT_COLOR)
                }
            }
        }
        popupMenu.add(fontColorMenu)

        val countMenu = JMenuItem()
        countMenu.action = object : AbstractAction("Count") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.COUNT)
                }
            }
        }
        popupMenu.add(countMenu)

        val webSearchMenu = JMenuItem()
        webSearchMenu.action = object : AbstractAction("Web search") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.WEB_SEARCH)
                }
            }
        }
        popupMenu.add(webSearchMenu)

        val translateMenu = JMenuItem()
        translateMenu.action = object : AbstractAction("Translate to English") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.TRANSLATION_TO_ENGLISH)
                }
            }
        }
        popupMenu.add(translateMenu)
    }
}