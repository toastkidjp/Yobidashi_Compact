package jp.toastkid.yobidashi.compact.editor.view

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import jp.toastkid.yobidashi.compact.model.Setting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.awt.Event
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class FileMenuView(private val channel: Channel<MenuCommand>) {

    operator fun invoke(): JMenu {
        val fileMenu = JMenu("File(F)")
        fileMenu.setMnemonic('F')

        fileMenu.add(
                JMenuItem("Open folder").also {
                    it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK)
                    it.addActionListener {
                        Desktop.getDesktop().open(Setting.articleFolderFile())
                    }
                }
        )

        fileMenu.add(
                JMenuItem("Save").also {
                    it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK)
                    it.addActionListener { CoroutineScope(Dispatchers.Default).launch { channel.send(MenuCommand.SAVE) } }
                }
        )

        fileMenu.add(
                JMenuItem("Close").also {
                    it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK)
                    it.addActionListener { CoroutineScope(Dispatchers.Default).launch { channel.send(MenuCommand.CLOSE) } }
                }
        )

        return fileMenu
    }

}