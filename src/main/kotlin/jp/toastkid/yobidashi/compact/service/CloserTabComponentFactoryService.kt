package jp.toastkid.yobidashi.compact.service

import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTabbedPane

class CloserTabComponentFactoryService(private val tabPane: JTabbedPane) {

    operator fun invoke(newContent: JComponent, title: String): JComponent {
        val panel = JPanel(BoxLayout(panel, BoxLayout.X_AXIS))
        panel.add(JLabel(title))
        panel.add(
                JButton(LABEL_CLOSE_BUTTON).also {
                    it.addActionListener { tabPane.removeTabAt(tabPane.indexOfComponent(newContent)) }
                }
        )
        return panel
    }

    companion object {

        private const val LABEL_CLOSE_BUTTON = "x"

    }

}