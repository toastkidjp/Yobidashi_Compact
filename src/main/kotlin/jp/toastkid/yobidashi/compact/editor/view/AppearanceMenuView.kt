package jp.toastkid.yobidashi.compact.editor.view

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import jp.toastkid.yobidashi.compact.editor.service.AppearanceSettingService
import jp.toastkid.yobidashi.compact.model.Setting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.awt.event.KeyEvent
import javax.swing.JCheckBoxMenuItem
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class AppearanceMenuView(private val channel: Channel<MenuCommand>) {

    operator fun invoke(): JMenu {
        val menu = JMenu("Appearance(A)")
        menu.setMnemonic('A')

        val findItem = JMenuItem("Color & Font")
        findItem.addActionListener {
            AppearanceSettingService(channel).invoke()
        }
        menu.add(findItem)

        val wrapLineSwitchItem = JCheckBoxMenuItem("Wrap line", Setting.wrapLine())
        wrapLineSwitchItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.SHIFT_DOWN_MASK or KeyEvent.CTRL_DOWN_MASK)
        wrapLineSwitchItem.addActionListener {
            CoroutineScope(Dispatchers.IO).launch {
                channel.send(MenuCommand.SWITCH_WRAP_LINE)
            }
        }
        menu.add(wrapLineSwitchItem)

        return menu
    }

}