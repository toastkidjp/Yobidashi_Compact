package jp.toastkid.yobidashi.compact.view

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.service.ExtensionRemover
import jp.toastkid.yobidashi.compact.service.KeywordSearch
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.InputEvent
import javax.swing.*
import javax.swing.BoxLayout

class SearchMenuView {

    private val extensionRemover = ExtensionRemover()

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
                onAction()
            }
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
        Single.fromCallable {
            KeywordSearch().invoke(keyword, fileFilter.text)
        }.subscribeOn(Schedulers.io())
                .flatMapObservable { it.toObservable() }
                .map { extensionRemover(it) ?: "" }
                .filter { it.isNotBlank() }
                .map { Article.withTitle(it) }
                .subscribe(
                        { articleListView.add(it) },
                        { it.printStackTrace() },
                        {
                            if (articleListView.isEmpty()) {
                                return@subscribe
                            }
                            SubjectPool.next(articleListView)
                        }
                )
    }

}