package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.calendar.view.CalendarPanel
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane

class ToolMenuView {

    operator fun invoke(): JMenu {
        val menu = JMenu("Tool")
        menu.add(JMenuItem("Calendar").also {
            it.addActionListener {
                JOptionPane.showMessageDialog(null, CalendarPanel())
            }
        })
        return menu
    }

}
