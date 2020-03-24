package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.Setting
import java.awt.event.ActionEvent
import java.lang.Exception
import javax.swing.*

class LookAndFeelMenuView(private val frameSupplier: () -> JFrame) {

    operator fun invoke(): JMenu {
        val menu = JMenu(TITLE)

        val current = Setting.lookAndFeel() ?: UIManager.getLookAndFeel().javaClass.canonicalName
        updateLookAndFeelWith(current)

        val group = ButtonGroup()

        UIManager.getInstalledLookAndFeels()
                .map { toMenuItem(it, current, group) }
                .forEach {
                    group.add(it)
                    menu.add(it)
                }

        return menu
    }

    private fun toMenuItem(it: UIManager.LookAndFeelInfo, current: String?, group: ButtonGroup): JRadioButtonMenuItem {
        val item = JRadioButtonMenuItem()
        item.isSelected = it.className == current
        item.hideActionText = true
        item.action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                updateLookAndFeelWith(group.selection.actionCommand)
            }
        }
        item.text = it.name
        item.actionCommand = it.className
        return item
    }

    private fun updateLookAndFeelWith(command: String) {
        try {
            UIManager.setLookAndFeel(command)
            Setting.setLookAndFeel(command)
            SwingUtilities.updateComponentTreeUI(frameSupplier())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TITLE = "Look & Feel"
    }
}