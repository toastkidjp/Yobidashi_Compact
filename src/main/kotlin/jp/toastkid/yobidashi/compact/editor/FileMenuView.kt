package jp.toastkid.yobidashi.compact.editor

import jp.toastkid.yobidashi.compact.model.Setting
import java.awt.Desktop
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class FileMenuView {

    operator fun invoke(function: () -> Unit, closeFunction: () -> Unit): JMenu {
        val fileMenu = JMenu("File(F)")
        fileMenu.setMnemonic('F')

        val item = JMenuItem("Open folder")
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK)
        item.addActionListener {
            Desktop.getDesktop().open(Setting.articleFolderFile())
        }
        fileMenu.add(item)

        val saveItem = JMenuItem("Save")
        saveItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK)
        saveItem.addActionListener { function() }
        fileMenu.add(saveItem)

        val closeItem = JMenuItem("Close")
        closeItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK)
        closeItem.addActionListener { closeFunction() }
        fileMenu.add(closeItem)

        return fileMenu
    }

}