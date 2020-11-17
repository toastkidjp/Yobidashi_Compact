package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.model.Sorting
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.ButtonGroup
import javax.swing.JMenu
import javax.swing.JRadioButtonMenuItem

class SortMenuView {

    operator fun invoke(): JMenu {
        val menu = JMenu(TITLE)
        menu.setMnemonic('S')

        val current = Setting.sorting()
        updateSorting(current)

        val group = ButtonGroup()

        Sorting.values()
                .map { toMenuItem(it, current, group) }
                .forEach {
                    group.add(it)
                    menu.add(it)
                }

        return menu
    }

    private fun toMenuItem(it: Sorting, current: Sorting, group: ButtonGroup): JRadioButtonMenuItem {
        val item = JRadioButtonMenuItem()
        item.isSelected = it == current
        item.hideActionText = true
        item.action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                updateSorting(Sorting.findByName(group.selection.actionCommand))
            }
        }
        item.text = it.text
        item.actionCommand = it.name
        return item
    }

    private fun updateSorting(sort: Sorting) {
        Setting.setSorting(sort)
        SubjectPool.nextSorting(sort)
    }

    companion object {
        private const val TITLE = "Sort(S)"
    }
}