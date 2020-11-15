package jp.toastkid.yobidashi.compact.editor

import jp.toastkid.yobidashi.compact.view.LookAndFeelMenuView
import kotlinx.coroutines.channels.Channel
import javax.swing.JFrame
import javax.swing.JMenuBar

class MenubarView(private val channel: Channel<MenuCommand>) {

    operator fun invoke(frame: JFrame): JMenuBar {
        val menubar = JMenuBar()

        menubar.add(FileMenuView(channel).invoke())
        menubar.add(LookAndFeelMenuView { frame }())
        return menubar
    }

}