package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.ArticleFileListModel
import jp.toastkid.yobidashi.compact.service.TodayFileTitleGenerator
import jp.toastkid.yobidashi.compact.model.Article
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.File
import javax.swing.*

/**
 * @author toastkidjp
 */
class MainFrame(title: String) : JFrame(title) {

    private val fileListModel = ArticleFileListModel()

    init {
        setTitle(title)

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val list = initializeList()

        val scrollPane = JScrollPane()
        scrollPane.viewport.view = list
        scrollPane.preferredSize = Dimension(1000, 800)

        val menubar = JMenuBar()
        val fileMenu = JMenu("File")
        val newFileMenuItem = JMenuItem("Make today")
        newFileMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, KeyEvent.VK_N)
        newFileMenuItem.addActionListener {
            fileListModel.add(Article.withTitle(TodayFileTitleGenerator().invoke(System.currentTimeMillis())))
            list.updateUI()
        }
        fileMenu.add(newFileMenuItem)
        menubar.add(fileMenu)

        val searchInput = JTextField()
        searchInput.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) = Unit

            override fun keyPressed(e: KeyEvent?) = Unit

            override fun keyReleased(e: KeyEvent?) {
                if (e == null) {
                    return
                }
                if (e.keyCode == KeyEvent.VK_ENTER) {
                    filter(searchInput, list)
                }
            }
        })
        searchInput.preferredSize = Dimension(600, 40)

        val panel = JPanel()
        panel.add(searchInput)
        panel.add(scrollPane)

        jMenuBar = menubar
        contentPane.add(panel, BorderLayout.CENTER)
    }

    private fun filter(searchInput: JTextField, list: JList<Article>) {
        fileListModel.filter(searchInput.text)
        list.updateUI()
    }

    private fun initializeList(): JList<Article> {
        fileListModel.addAll(File("C://Users/toastkidjp/Documents/Article2"))

        val list = JList(fileListModel)
        list.cellRenderer = ArticleCellRenderer()
        list.addListSelectionListener {
            list.selectedValue.open()
        }
        return list
    }
}