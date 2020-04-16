package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.ArticleFileListModel
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.model.Sorting
import jp.toastkid.yobidashi.compact.service.ZipArchiver
import java.awt.Dimension
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
        val list = JList(fileListModel)
        list.cellRenderer = ArticleCellRenderer()
        return list
    }

    fun add(article: Article) {
        fileListModel.add(article)
    }

    fun addAll(articles: Collection<Article>) {
        fileListModel.addAll(articles)
    }

    fun filter(searchInput: JTextField) {
        fileListModel.filter(searchInput.text)
        view.updateUI()
    }

    fun updateUI() {
        view.updateUI()
    }

    fun openCurrentArticle() {
        view.selectedValue?.open()
    }

    fun currentArticle(): Article? = view.selectedValue

    fun counts(): String? {
        val selectedArticles = view.selectedValuesList ?: return null
        return selectedArticles
                .map { "${it.getTitle()}: ${it.count()}" }
                .reduce { base, item -> "$base${System.lineSeparator()}$item" }
    }

    fun isEmpty() = fileListModel.size == 0

    fun sortBy(sorting: Sorting) {
        fileListModel.sortBy(sorting)
        updateUI()
    }

    fun zip() {
        val paths = (view.selectedValuesList ?: fileListModel.all()).map { it.path() }
        ZipArchiver().invoke(paths)
    }
}