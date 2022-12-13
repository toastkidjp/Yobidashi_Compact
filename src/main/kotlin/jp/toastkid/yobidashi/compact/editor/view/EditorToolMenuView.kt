package jp.toastkid.yobidashi.compact.editor.view

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.awt.Event
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class EditorToolMenuView(private val channel: Channel<MenuCommand>) {

    operator fun invoke(): JMenu {
        val menu = JMenu("Tool(T)")
        menu.setMnemonic('T')

        val webSearchMenu = JMenuItem("Web search").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.WEB_SEARCH)
                }
            }
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.SHIFT_MASK or KeyEvent.CTRL_DOWN_MASK)
        }
        menu.add(webSearchMenu)

        menu.add(
            JMenuItem("Open URL").also {
                it.addActionListener {
                    CoroutineScope(Dispatchers.Default).launch {
                        channel.send(MenuCommand.OPEN_URL)
                    }
                }
                it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.SHIFT_MASK or KeyEvent.CTRL_DOWN_MASK)
            }
        )

        return menu
    }

}