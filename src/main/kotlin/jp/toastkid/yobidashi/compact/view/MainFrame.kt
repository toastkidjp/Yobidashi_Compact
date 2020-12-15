package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.ArticleListTabs
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.CloserTabComponentFactoryService
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
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.JTextField
import javax.swing.SwingUtilities

/**
 * @author toastkidjp
 */
class MainFrame(title: String) {

    private val tabs = ArticleListTabs()

    private val frame = JFrame(title)

    fun initialize() {
        frame.iconImage = ImageIO.read(javaClass.classLoader.getResourceAsStream("images/icon.png"))

        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        Setting.lookAndFeel()?.let {
            UiUpdaterService().invoke(frame, it)
        }

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

        SubjectPool.observeNewSearchResult { component, title ->
            SwingUtilities.invokeLater {
                addNewTab(tabPane, component.view(), title)
            }
            tabs.add(component)
        }

        SubjectPool.observeAddNewTab {
            addNewTab(tabPane, it.first, it.second)
        }

        SubjectPool.observeCloseWindow {
            frame.dispose()
        }

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

        frame.jMenuBar = MenuBarView().invoke(frame)
        frame.contentPane.add(panel, BorderLayout.CENTER)
        frame.setBounds(200, 200, 400, 600)
    }

    private fun addNewTab(tabPane: JTabbedPane, component: JComponent, title: String) {
        tabPane.add(component)
        val indexOfComponent = tabPane.indexOfComponent(component)
        if (indexOfComponent == -1) {
            return
        }
        tabPane.setTabComponentAt(indexOfComponent, CloserTabComponentFactoryService(tabPane)(component, title))
        tabPane.selectedIndex = indexOfComponent
    }

    fun show() {
        initialize()
        frame.isVisible = true
    }

}