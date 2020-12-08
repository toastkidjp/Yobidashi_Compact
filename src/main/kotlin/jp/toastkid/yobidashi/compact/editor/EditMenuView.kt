package jp.toastkid.yobidashi.compact.editor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class EditMenuView(private val channel: Channel<MenuCommand>) {

    operator fun invoke(): JMenu {
        val menu = JMenu("Edit(E)")
        menu.setMnemonic('E')

        val findItem = JMenuItem("Find")
        findItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK)
        findItem.addActionListener {
            CoroutineScope(Dispatchers.Default).launch {
                channel.send(MenuCommand.FIND)
            }
        }
        menu.add(findItem)

        val item = JMenuItem("Paste as quotation")
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK)
        item.addActionListener {
            CoroutineScope(Dispatchers.Default).launch {
                channel.send(MenuCommand.PASTE_AS_QUOTATION)
            }
        }
        menu.add(item)

        menu.add(
                JMenuItem("Duplicate line").also {
                    it.addActionListener {
                        CoroutineScope(Dispatchers.Default).launch {
                            channel.send(MenuCommand.DUPLICATE_LINE)
                        }
                    }
                    it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.SHIFT_MASK or InputEvent.CTRL_MASK)
                }
        )

        return menu
    }

}