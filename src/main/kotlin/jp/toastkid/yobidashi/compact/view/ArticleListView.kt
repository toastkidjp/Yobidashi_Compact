package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.editor.service.ClipboardPutterService
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.ArticleFileListModel
import jp.toastkid.yobidashi.compact.model.Sorting
import jp.toastkid.yobidashi.compact.service.ZipArchiver
import java.awt.Desktop
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import javax.swing.AbstractAction
import javax.swing.JList
import javax.swing.JOptionPane
import javax.swing.JPopupMenu
import javax.swing.JScrollPane
import javax.swing.JTextField

class ArticleListView {

    private val fileListModel = ArticleFileListModel()

    private val view: JList<Article>

    private val scrollPane = JScrollPane()

    init {
        view = initializeView()

        view.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                super.keyPressed(e)
                if (e?.keyCode == KeyEvent.VK_ENTER) {
                    view.selectedValue?.open()
                }
            }
        })

        scrollPane.viewport.view = view
        scrollPane.preferredSize = Dimension(400, 800)
    }

    fun view() = scrollPane

    private fun initializeView(): JList<Article> {
        return JList(fileListModel).also {
            it.cellRenderer = ArticleCellRenderer()
            it.componentPopupMenu = JPopupMenu()
            var currentFocused: Article? = null
            it.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    if (e?.clickCount ?: 0 >= 2) {
                        view.selectedValue?.open()
                    }
                }

                override fun mousePressed(e: MouseEvent?) {
                    extractFocused(e)
                }

                override fun mouseReleased(e: MouseEvent?) {
                    extractFocused(e)
                }

                private fun extractFocused(e: MouseEvent?) {
                    if (e?.isPopupTrigger != false) {
                        return
                    }
                    currentFocused = it.model.getElementAt(it.locationToIndex(it.mousePosition))
                }
            })
            it.componentPopupMenu.add(object : AbstractAction("Open") {
                override fun actionPerformed(e: ActionEvent?) {
                    if (it.isSelectionEmpty) {
                        currentFocused?.open()
                        return
                    }

                    it.selectedValuesList?.forEach { article -> article.open() }
                }
            })
            it.componentPopupMenu.add(object : AbstractAction("Count characters") {
                override fun actionPerformed(e: ActionEvent?) {
                    val message = if (it.isSelectionEmpty) {
                        "${currentFocused?.getTitle()}: ${currentFocused?.count()}"
                    } else {
                        val lineSeparator = System.lineSeparator()
                        it.selectedValuesList?.map { article -> "${article.getTitle()}: ${article.count()}" }
                                ?.reduce { base, item -> "$base$lineSeparator$item" }
                    } ?: return
                    JOptionPane.showMessageDialog(null, message, "Count", JOptionPane.INFORMATION_MESSAGE)
                }
            })
            it.componentPopupMenu.add(object : AbstractAction("Copy title") {
                override fun actionPerformed(e: ActionEvent?) {
                    if (it.isSelectionEmpty) {
                        ClipboardPutterService()(currentFocused?.getTitle())
                        return
                    }

                    ClipboardPutterService()(
                            it.selectedValuesList?.map { article -> article.getTitle() }
                                    ?.reduce { base, item -> "$base, $item" }
                    )
                }
            })
            it.componentPopupMenu.add(object : AbstractAction("Copy title as internal link") {
                override fun actionPerformed(e: ActionEvent?) {
                    if (it.isSelectionEmpty) {
                        ClipboardPutterService()("[[${currentFocused?.getTitle()}]]")
                        return
                    }
                    ClipboardPutterService()(
                            it.selectedValuesList?.map { article -> "[[${article.getTitle()}]]" }
                                    ?.reduce { base, item -> "$base, $item" }
                    )
                }
            })
        }
    }

    fun add(article: Article) {
        fileListModel.add(article)
    }

    fun addAll(articles: Collection<Article>) {
        fileListModel.addAll(articles) { SubjectPool.refreshUi(view) }
    }

    fun filter(searchInput: JTextField) {
        fileListModel.filter(searchInput.text)
        view.updateUI()
    }

    fun updateUI() {
        view.updateUI()
    }

    fun isEmpty() = fileListModel.size == 0

    fun sortBy(sorting: Sorting) {
        fileListModel.sortBy(sorting)
        updateUI()
    }

    fun zip() {
        val paths = (view.selectedValuesList ?: fileListModel.all()).map { it.path() }
        ZipArchiver().invoke(paths)
        Desktop.getDesktop().open(File("."))
    }
}