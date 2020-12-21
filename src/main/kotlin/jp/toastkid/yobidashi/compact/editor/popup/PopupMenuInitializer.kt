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

        val blockQuotationMenu = JMenuItem("Block quote").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.BLOCKQUOTE)
                }
            }
        }
        popupMenu.add(blockQuotationMenu)

        val hyphenListMenu = JMenuItem("Unordered list").also {
            it.addActionListener {
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

        val italicMenu = JMenuItem("Italic").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.ITALIC)
                }
            }
        }
        popupMenu.add(italicMenu)

        val strikethroughMenu = JMenuItem("Strikethrough").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.STRIKETHROUGH)
                }
            }
        }
        popupMenu.add(strikethroughMenu)

        val codeBlockMenu = JMenuItem("Code block")
        codeBlockMenu.addActionListener {
            CoroutineScope(Dispatchers.Default).launch {
                channel.send(MenuCommand.CODE_BLOCK)
            }
        }
        popupMenu.add(codeBlockMenu)

        val fontColorMenu = JMenuItem("Font color").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.FONT_COLOR)
                }
            }
        }
        popupMenu.add(fontColorMenu)

        popupMenu.add(
                JMenuItem("To hyperlink").also {
                    it.addActionListener {
                        CoroutineScope(Dispatchers.Default).launch {
                            channel.send(MenuCommand.TO_HYPERLINK)
                        }
                    }
                }
        )

        val countMenu = JMenuItem("Count").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.COUNT)
                }
            }
        }
        popupMenu.add(countMenu)

        val webSearchMenu = JMenuItem("Web search").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.WEB_SEARCH)
                }
            }
        }
        popupMenu.add(webSearchMenu)

        val dictionaryMenu = JMenuItem("Dictionary").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.DICTIONARY_SEARCH)
                }
            }
        }
        popupMenu.add(dictionaryMenu)

        val translateMenu = JMenuItem("Translate to English").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.TRANSLATION_TO_ENGLISH)
                }
            }
        }
        popupMenu.add(translateMenu)

        val horizontalRuleMenu = JMenuItem("Horizontal rule").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.HORIZONTAL_RULE)
                }
            }
        }
        popupMenu.add(horizontalRuleMenu)
    }
}