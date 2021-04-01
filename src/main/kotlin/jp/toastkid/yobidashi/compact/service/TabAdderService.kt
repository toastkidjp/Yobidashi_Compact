package jp.toastkid.yobidashi.compact.service

import javax.swing.JComponent
import javax.swing.JTabbedPane

class TabAdderService(
    private val tabPane: JTabbedPane,
    private val closerTabComponentFactoryService: CloserTabComponentFactoryService = CloserTabComponentFactoryService(tabPane)
) {

    operator fun invoke(component: JComponent, title: String) {
        tabPane.add(component)
        val indexOfComponent = tabPane.indexOfComponent(component)
        if (indexOfComponent == -1) {
            return
        }
        tabPane.setTabComponentAt(indexOfComponent, closerTabComponentFactoryService(component, title))
        tabPane.selectedIndex = indexOfComponent
    }

}