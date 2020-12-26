package jp.toastkid.yobidashi.compact.editor.view

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import jp.toastkid.yobidashi.compact.editor.service.ColorSettingService
import kotlinx.coroutines.channels.Channel
import javax.swing.JMenu
import javax.swing.JMenuItem

class AppearanceMenuView(private val channel: Channel<MenuCommand>) {

    operator fun invoke(): JMenu {
        val menu = JMenu("Appearance(A)")
        menu.setMnemonic('A')

        val findItem = JMenuItem("Color")
        findItem.addActionListener {
            ColorSettingService(channel).invoke()
        }
        menu.add(findItem)

        return menu
    }

}