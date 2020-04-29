package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.UiUpdaterService
import java.awt.event.ActionEvent
import javax.swing.*

class LookAndFeelMenuView(private val frameSupplier: () -> JFrame) {

    private val uiUpdaterService = UiUpdaterService()

    operator fun invoke(): JMenu {
        val menu = JMenu(TITLE)
        menu.setMnemonic('L')

        val current = Setting.lookAndFeel() ?: UIManager.getLookAndFeel().javaClass.canonicalName
        uiUpdaterService(frameSupplier(), current)

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
                uiUpdaterService(frameSupplier(), group.selection.actionCommand)
            }
        }
        item.text = it.name
        item.actionCommand = it.className
        return item
    }

    companion object {
        private const val TITLE = "Look & Feel(L)"
    }
}