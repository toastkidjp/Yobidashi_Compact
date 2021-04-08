package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.view.ArticleListView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField

class ArticleFinderService {

    operator fun invoke() {
        val (panel, fileFilter) = makeDialogContent()
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
            SubjectPool.sendSearchResult(articleListView, "'$keyword' find result ${articleListView.size()}")
        }
    }

    private fun makeDialogContent(): Pair<JPanel, JTextField> {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        val fileFilter = JTextField()
        fileFilter.preferredSize = Dimension(100, 24)
        panel.add(JLabel("Please would you input find query."))
        panel.add(JLabel("Title filter"))
        panel.add(fileFilter)
        panel.add(JLabel("Keyword"))
        return Pair(panel, fileFilter)
    }

}