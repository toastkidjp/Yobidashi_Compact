package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.media.MediaFileFinder
import jp.toastkid.yobidashi.compact.media.MediaListView
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.ArticleListTabs
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.ArticleFilterViewFactoryService
import jp.toastkid.yobidashi.compact.service.ArticlesReaderService
import jp.toastkid.yobidashi.compact.service.CloseActionService
import jp.toastkid.yobidashi.compact.service.TabAdderService
import jp.toastkid.yobidashi.compact.service.UiUpdaterService
import jp.toastkid.yobidashi.compact.viewmodel.ZipViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.awt.BorderLayout
import java.util.stream.Collectors
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.SwingUtilities

/**
 * @author toastkidjp
 */
class MainFrame(
    title: String,
    private val tabs: ArticleListTabs = ArticleListTabs(),
    private val frame: JFrame = JFrame(title)
) {

    private fun initialize() {
        frame.iconImage = ImageIO.read(javaClass.classLoader.getResourceAsStream("images/icon.png"))

        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        Setting.lookAndFeel()?.let {
            UiUpdaterService().invoke(frame, it)
        }

        val list = ArticleListView()
        loadArticles(list)

        val tabPane = JTabbedPane()
        tabPane.add("Articles", list.view())
        tabs.add(list)

        val mediaListView = MediaListView()
        tabPane.add("Media", mediaListView.view())
        CoroutineScope(Dispatchers.IO).launch {
            val mediaList = MediaFileFinder().invoke(Setting.mediaFolderPath())
            mediaListView.addAll(mediaList)
        }

        val searchInput = ArticleFilterViewFactoryService().invoke { list.filter(it) }

        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(searchInput, BorderLayout.NORTH)
        panel.add(tabPane, BorderLayout.CENTER)

        observe(
            tabPane,
            list,
            CloseActionService(
                    { tabPane.tabCount - if (tabPane.indexOfTab("Media") != -1) 1 else 0 },
                { tabPane.removeTabAt(tabPane.tabCount - 1) },
                frame::dispose
            )
        )

        frame.jMenuBar = MenuBarView().invoke(frame)
        frame.contentPane.add(panel, BorderLayout.CENTER)
        frame.setBounds(200, 200, 400, 600)
    }

    private fun loadArticles(list: ArticleListView) {
        CoroutineScope(Dispatchers.Swing).launch {
            val articles = withContext(Dispatchers.IO) {
                ArticlesReaderService().invoke()
                    .map { Article(it) }
                    .collect(Collectors.toList())
            }
            list.addAll(articles)
        }
    }

    private fun observe(tabPane: JTabbedPane, list: ArticleListView, closeActionService: CloseActionService) {
        SubjectPool.observeSort {
            tabs.get(tabPane.selectedIndex).sortBy(it)
        }

        val tabAdderService = TabAdderService(tabPane)

        SubjectPool.observeNewSearchResult { component, title ->
            tabAdderService(component.view(), title)
            tabs.add(component)
        }

        SubjectPool.observeAddNewTab {
            tabAdderService(it.first, it.second)
        }

        SubjectPool.observeCloseWindow(closeActionService::invoke)

        SubjectPool.observeAddToList {
            list.add(it)
            list.sortBy(Setting.sorting())
        }

        SubjectPool.observeRefreshUi {
            SwingUtilities.updateComponentTreeUI(it)
        }

        ZipViewModel.observe {
            tabs.get(tabPane.selectedIndex).zip()
        }
    }

    fun show() {
        initialize()
        frame.isVisible = true
    }

}