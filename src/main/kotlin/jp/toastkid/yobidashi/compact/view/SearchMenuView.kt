package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.service.KeywordSearch
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.*
import javax.swing.BoxLayout

class SearchMenuView {

    operator fun invoke(): JMenu {
        val menu = JMenu("Search")
        menu.add(makeMenuItem())
        return menu
    }

    private fun makeMenuItem(): JMenuItem {
        val item = JMenuItem()
        item.hideActionText = true
        item.action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                val panel = JPanel()
                panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
                val fileFilter = JTextField()
                fileFilter.preferredSize = Dimension(100, 36)
                panel.add(JLabel("Please would you input search query."))
                panel.add(JLabel("Title filter"))
                panel.add(fileFilter)
                panel.add(JLabel("Keyword"))
                val keyword = JOptionPane.showInputDialog(null, panel)
                if (keyword.isNullOrBlank() && fileFilter.text.isNullOrBlank()) {
                    return
                }

                println(KeywordSearch().invoke(keyword, fileFilter.text))
            }
        }
        item.text = "Search"
        return item
    }

}