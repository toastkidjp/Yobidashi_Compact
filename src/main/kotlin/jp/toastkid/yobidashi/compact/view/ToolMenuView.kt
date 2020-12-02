package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.calendar.view.CalendarPanel
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.KeyStroke

class ToolMenuView {

    operator fun invoke(): JMenu {
        val menu = JMenu("Tool")
        menu.add(JMenuItem("Calendar").also {
            it.addActionListener {
                JOptionPane.showMessageDialog(null, CalendarPanel())
            }
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK)
        })
        return menu
    }

}
