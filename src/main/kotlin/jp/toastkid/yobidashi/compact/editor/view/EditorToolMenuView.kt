package jp.toastkid.yobidashi.compact.editor.view

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import kotlinx.coroutines.channels.Channel
import javax.swing.JMenu

class EditorToolMenuView(private val channel: Channel<MenuCommand>) {

    operator fun invoke(): JMenu {
        val menu = JMenu("Tool(T)")
        menu.setMnemonic('T')

        return menu
    }

}