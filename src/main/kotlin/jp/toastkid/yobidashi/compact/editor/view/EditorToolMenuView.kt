package jp.toastkid.yobidashi.compact.editor.view

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.swing.JMenu
import javax.swing.JMenuItem

class EditorToolMenuView(private val channel: Channel<MenuCommand>) {

    operator fun invoke(): JMenu {
        val menu = JMenu("Tool(T)")
        menu.setMnemonic('T')

        val webSearchMenu = JMenuItem("Web search").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.WEB_SEARCH)
                }
            }
        }
        menu.add(webSearchMenu)

        val openUrlMenu = JMenuItem("Open URL").also {
            it.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(MenuCommand.OPEN_URL)
                }
            }
        }
        menu.add(openUrlMenu)

        return menu
    }

}