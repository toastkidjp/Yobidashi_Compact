package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.ArticleFileListModel
import jp.toastkid.yobidashi.compact.model.Setting
import java.awt.Dimension
import java.nio.file.Paths
import javax.swing.JList
import javax.swing.JScrollPane
import javax.swing.JTextField

class ArticleListView {

    private val fileListModel = ArticleFileListModel()

    private val view: JList<Article>

    private val scrollPane = JScrollPane()

    init {
        view = initializeView()
        scrollPane.viewport.view = view
        scrollPane.preferredSize = Dimension(400, 800)
    }

    fun view() = scrollPane

    private fun initializeView(): JList<Article> {
        fileListModel.addAll(Paths.get(Setting.articleFolder()))

        val list = JList(fileListModel)
        list.cellRenderer = ArticleCellRenderer()
        return list
    }

    fun add(article: Article) {
        fileListModel.add(article)
    }

    fun filter(searchInput: JTextField) {
        fileListModel.filter(searchInput.text)
        view.updateUI()
    }

    fun updateUI() {
        view.updateUI()
    }

    fun openCurrentArticle() {
        view.selectedValue.open()
    }

    fun currentArticle(): Article? = view.selectedValue
}