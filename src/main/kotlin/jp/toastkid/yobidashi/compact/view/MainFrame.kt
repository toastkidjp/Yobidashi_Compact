package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.ArticleListTabs
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.UiUpdaterService
import jp.toastkid.yobidashi.compact.viewmodel.ZipViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import javax.imageio.ImageIO
import javax.swing.*

/**
 * @author toastkidjp
 */
class MainFrame(title: String) {

    private val tabs = ArticleListTabs()

    private val frame = JFrame(title)

    init {
        frame.setTitle(title)

        frame.iconImage = ImageIO.read(javaClass.classLoader.getResourceAsStream("images/icon.png"))

        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val list = ArticleListView()
        CoroutineScope(Dispatchers.Swing).launch {
            val articles = withContext(Dispatchers.IO) {
                Files.list(Paths.get(Setting.articleFolder()))
                        .map { Article(it) }
                        .collect(Collectors.toList())
            }
            list.addAll(articles)
        }

        val tabPane = JTabbedPane()
        tabPane.add("Articles", list.view())
        tabs.add(list)

        SubjectPool.observeSort({
            tabs.get(tabPane.selectedIndex).sortBy(it)
        })

        val searchInput = JTextField()
        searchInput.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) = Unit

            override fun keyPressed(e: KeyEvent?) = Unit

            override fun keyReleased(e: KeyEvent?) {
                if (e == null) {
                    return
                }
                if (e.keyCode == KeyEvent.VK_ENTER) {
                    list.filter(searchInput)
                }
            }
        })
        searchInput.preferredSize = Dimension(600, 40)

        val buttons = JPanel()
        val openButton = JButton()
        openButton.action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                tabs.get(tabPane.selectedIndex).openCurrentArticle()
            }
        }
        openButton.text = "Open"
        openButton.preferredSize = Dimension(100, 40)
        openButton.setMnemonic(KeyEvent.VK_O)
        buttons.add(openButton)

        val countButton = JButton()
        countButton.action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                tabs.get(tabPane.selectedIndex).counts()?.also {
                    JOptionPane.showConfirmDialog(frame, it)
                }
            }
        }
        countButton.text = "Count"
        countButton.preferredSize = Dimension(100, 40)
        countButton.setMnemonic(KeyEvent.VK_C)
        buttons.add(countButton)
        buttons.preferredSize = Dimension(300, 60)

        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(searchInput, BorderLayout.NORTH)
        panel.add(tabPane, BorderLayout.CENTER)
        panel.add(buttons, BorderLayout.SOUTH)

        SubjectPool.observe {
            SwingUtilities.invokeLater { tabPane.add("Search result", it.view()) }
            tabs.add(it)
        }

        SubjectPool.observeCloseWindow {
            frame.dispose()
        }

        SubjectPool.observeAddToList {
            list.add(it)
            list.sortBy(Setting.sorting())
        }

        ZipViewModel.observe {
            tabs.get(tabPane.selectedIndex).zip()
        }

        frame.jMenuBar = MenuBarView().invoke(frame)
        frame.contentPane.add(panel, BorderLayout.CENTER)
        frame.setBounds(200, 200, 400, 600)

        Setting.lookAndFeel()?.let {
            UiUpdaterService().invoke(frame, it)
        }
    }

    fun show() {
        frame.isVisible = true
    }
}