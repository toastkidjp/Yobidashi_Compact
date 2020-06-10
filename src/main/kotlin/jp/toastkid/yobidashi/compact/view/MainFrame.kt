package jp.toastkid.yobidashi.compact.view

import io.reactivex.rxjava3.functions.Consumer
import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.ArticleListTabs
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.UiUpdaterService
import jp.toastkid.yobidashi.compact.viewmodel.ZipViewModel
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
        list.addAll(Files.list(Paths.get(Setting.articleFolder())).map { Article(it) }.collect(Collectors.toList()))

        val tabPane = JTabbedPane()
        tabPane.add("Articles", list.view())
        tabs.add(list)

        SubjectPool.observeSort(Consumer {
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

        SubjectPool.observe(Consumer {
            SwingUtilities.invokeLater { tabPane.add("Search result", it.view()) }
            tabs.add(it)
        })

        SubjectPool.observeCloseWindow(Consumer {
            frame.dispose()
        })

        SubjectPool.observeAddToList(Consumer {
            list.add(it)
            list.sortBy(Setting.sorting())
        })

        ZipViewModel.observe(Consumer {
            tabs.get(tabPane.selectedIndex).zip()
        })

        frame.jMenuBar = MenuBarView().invoke(frame)
        frame.contentPane.add(panel, BorderLayout.CENTER)
        frame.setBounds(200, 200, 400, 800)

        Setting.lookAndFeel()?.let {
            UiUpdaterService().invoke(frame, it)
        }
    }

    fun show() {
        frame.isVisible = true
    }
}