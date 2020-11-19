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

        val item = JMenuItem("Paste as quotation")
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK)
        item.addActionListener {
            CoroutineScope(Dispatchers.Default).launch {
                channel.send(MenuCommand.PASTE_AS_QUOTATION)
            }
        }
        menu.add(item)

        return menu
    }

}