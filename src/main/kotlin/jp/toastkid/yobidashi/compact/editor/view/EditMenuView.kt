package jp.toastkid.yobidashi.compact.editor.view

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class EditMenuView(private val channel: Channel<MenuCommand>) {

    operator fun invoke(): JMenu {
        val menu = JMenu("Edit(E)")
        menu.setMnemonic('E')

        val findItem = JMenuItem("Find")
        findItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK)
        findItem.addActionListener {
            CoroutineScope(Dispatchers.Default).launch {
                channel.send(MenuCommand.FIND)
            }
        }
        menu.add(findItem)

        menu.addSeparator()

        val item = JMenuItem("Paste as quotation")
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK)
        item.addActionListener {
            CoroutineScope(Dispatchers.Default).launch {
                channel.send(MenuCommand.PASTE_AS_QUOTATION)
            }
        }
        menu.add(item)

        menu.add(
                JMenuItem("Paste link with title").also {
                    it.addActionListener {
                        CoroutineScope(Dispatchers.Default).launch {
                            channel.send(MenuCommand.PASTE_LINK_WITH_TITLE)
                        }
                    }
                }
        )

        menu.addSeparator()

        menu.add(
            JMenuItem("Duplicate line").also {
                it.addActionListener {
                    CoroutineScope(Dispatchers.Default).launch {
                        channel.send(MenuCommand.DUPLICATE_LINE)
                    }
                }
                it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.SHIFT_DOWN_MASK or KeyEvent.CTRL_DOWN_MASK)
            }
        )

        menu.add(
            JMenuItem("Extract line").also {
                it.addActionListener {
                    CoroutineScope(Dispatchers.Default).launch {
                        channel.send(MenuCommand.EXTRACT_LINE)
                    }
                }
                it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK)
            }
        )

        menu.addSeparator()

        menu.add(JMenuItem("Switch editable").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.SWITCH_EDITABLE)
                }
            }
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.SHIFT_DOWN_MASK or KeyEvent.CTRL_DOWN_MASK)
        })

        return menu
    }

}