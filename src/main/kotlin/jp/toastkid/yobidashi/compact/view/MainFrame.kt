package jp.toastkid.yobidashi.compact.view

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.functions.Consumer
import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.ArticleListTabs
import jp.toastkid.yobidashi.compact.model.Setting
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import javax.swing.*

/**
 * @author toastkidjp
 */
class MainFrame(title: String) : JFrame(title) {

    private val tabs = ArticleListTabs()

    init {
        setTitle(title)

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val list = ArticleListView()
        list.addAll(Files.list(Paths.get(Setting.articleFolder())).map { Article(it) }.collect(Collectors.toList()))

        val tabPane = JTabbedPane()
        tabPane.add("Articles", list.view())
        tabs.add(list)

        SubjectPool.observeSort(Consumer {
            tabs.get(tabPane.selectedIndex).sortBy(it)
        })

        val menubar = JMenuBar()

        menubar.add(FileMenuView {
            list.add(it)
            list.updateUI()
        }.invoke())
        menubar.add(SearchMenuView().invoke())
        menubar.add(SortMenuView().invoke())
        menubar.add(LookAndFeelMenuView { this }())

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
        buttons.add(openButton)

        val countButton = JButton()
        countButton.action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                val selectedValue = tabs.get(tabPane.selectedIndex).currentArticle() ?: return
                JOptionPane.showConfirmDialog(
                        this@MainFrame,
                        "${selectedValue.getTitle()} - ${selectedValue.count()}"
                )
            }
        }
        countButton.text = "Count"
        countButton.preferredSize = Dimension(100, 40)
        buttons.add(countButton)
        buttons.preferredSize = Dimension(300, 60)

        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(searchInput, BorderLayout.NORTH)
        panel.add(tabPane, BorderLayout.CENTER)
        panel.add(buttons, BorderLayout.SOUTH)

        SubjectPool.observe(Consumer {
            SwingUtilities.invokeLater { tabPane.add("Search result", it.view()) }
            tabs.add(it)
        })

        SubjectPool.observeCloseWindow(Consumer {
            dispose()
        })

        jMenuBar = menubar
        contentPane.add(panel, BorderLayout.CENTER)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }

}