package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.service.KeywordSearch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.awt.Dimension
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.KeyStroke

class SearchMenuView {

    operator fun invoke(): JMenu {
        val menu = JMenu("Search")
        menu.add(makeMenuItem())
        return menu
    }

    private fun makeMenuItem(): JMenuItem {
        val item = JMenuItem()
        item.hideActionText = true
        item.addActionListener {
            onAction()
        }
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK)
        item.text = "Search"
        return item
    }

    private fun onAction() {
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

        val articleListView = ArticleListView()
        CoroutineScope(Dispatchers.Swing).launch {
            withContext(Dispatchers.IO) {
                KeywordSearch().invoke(keyword, fileFilter.text)
                        .asSequence()
                        .filter { it.isNotBlank() }
                        .map { Article.withTitle(it) }
                        .forEach { articleListView.add(it) }
            }

            if (articleListView.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Article which contains '$keyword' is not found.")
                return@launch
            }
            SubjectPool.next(articleListView, "'$keyword' search result")
        }
    }

}