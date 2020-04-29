package jp.toastkid.yobidashi.compact.view

import javax.swing.JFrame
import javax.swing.JMenuBar

class MenuBarView {

    operator fun invoke(frame: JFrame): JMenuBar {
        val menubar = JMenuBar()

        menubar.add(FileMenuView().invoke())
        menubar.add(SearchMenuView().invoke())
        menubar.add(AggregationMenuView().invoke())
        menubar.add(SortMenuView().invoke())
        menubar.add(LookAndFeelMenuView { frame }())
        return menubar
    }
}