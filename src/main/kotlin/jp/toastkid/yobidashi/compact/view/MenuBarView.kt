package jp.toastkid.yobidashi.compact.view

import javax.swing.JFrame
import javax.swing.JMenuBar

class MenuBarView {

    operator fun invoke(frame: JFrame): JMenuBar {
        val menuBar = JMenuBar()

        menuBar.add(FileMenuView().invoke())
        menuBar.add(FindMenuView().invoke())
        menuBar.add(AggregationMenuView().invoke())
        menuBar.add(SortMenuView().invoke())
        menuBar.add(ToolMenuView().invoke())
        menuBar.add(LookAndFeelMenuView { frame }())
        return menuBar
    }
}