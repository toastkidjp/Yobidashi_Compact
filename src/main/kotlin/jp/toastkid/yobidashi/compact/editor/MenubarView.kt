package jp.toastkid.yobidashi.compact.editor

import jp.toastkid.yobidashi.compact.view.LookAndFeelMenuView
import javax.swing.JFrame
import javax.swing.JMenuBar

class MenubarView {

    operator fun invoke(frame: JFrame, function: () -> Unit, closeFunction: () -> Unit): JMenuBar {
        val menubar = JMenuBar()

        menubar.add(FileMenuView().invoke(function, closeFunction))
        menubar.add(LookAndFeelMenuView { frame }())
        return menubar
    }

}