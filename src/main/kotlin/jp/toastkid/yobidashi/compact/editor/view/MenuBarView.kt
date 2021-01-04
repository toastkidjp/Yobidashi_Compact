package jp.toastkid.yobidashi.compact.editor.view

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import jp.toastkid.yobidashi.compact.view.LookAndFeelMenuView
import kotlinx.coroutines.channels.Channel
import javax.swing.JFrame
import javax.swing.JMenuBar

class MenuBarView(private val channel: Channel<MenuCommand>) {

    operator fun invoke(frame: JFrame): JMenuBar {
        val menuBar = JMenuBar()

        menuBar.add(FileMenuView(channel).invoke())
        menuBar.add(EditMenuView(channel).invoke())
        menuBar.add(AppearanceMenuView(channel).invoke())
        menuBar.add(LookAndFeelMenuView { frame }())
        return menuBar
    }

}