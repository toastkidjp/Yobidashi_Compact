package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.ArticleFileListModel
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.TodayFileTitleGenerator
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.nio.file.Files
import java.nio.file.Paths
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
        scrollPane.preferredSize = Dimension(400, 800)

        val menubar = JMenuBar()
        val fileMenu = JMenu("File")

        val todayFileMenuItem = JMenuItem("Make today")
        todayFileMenuItem.addActionListener {
            fileListModel.add(Article.withTitle(TodayFileTitleGenerator().invoke(System.currentTimeMillis())))
            list.updateUI()
        }
        fileMenu.add(todayFileMenuItem)

        val newFileMenuItem = JMenuItem("Make new")
        newFileMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, KeyEvent.VK_N)
        newFileMenuItem.addActionListener {
            val dialog = JOptionPane.showInputDialog(this, "Please input new article name.")
            if (dialog.isNullOrBlank()) {
                return@addActionListener
            }
            fileListModel.add(Article.withTitle(dialog))
            list.updateUI()
        }
        fileMenu.add(newFileMenuItem)

        menubar.add(fileMenu)
        menubar.add(SearchMenuView().invoke())
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
                    filter(searchInput, list)
                }
            }
        })
        searchInput.preferredSize = Dimension(600, 40)

        val buttons = JPanel()
        val openButton = JButton()
        openButton.action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                list.selectedValue.open()
            }
        }
        openButton.text = "Open"
        openButton.preferredSize = Dimension(100, 40)
        buttons.add(openButton)

        val countButton = JButton()
        countButton.action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                val selectedValue = list.selectedValue ?: return
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
        panel.add(scrollPane, BorderLayout.CENTER)
        panel.add(buttons, BorderLayout.SOUTH)

        jMenuBar = menubar
        contentPane.add(panel, BorderLayout.CENTER)
    }

    private fun filter(searchInput: JTextField, list: JList<Article>) {
        fileListModel.filter(searchInput.text)
        list.updateUI()
    }

    private fun initializeList(): JList<Article> {
        fileListModel.addAll(Paths.get(Setting.articleFolder()))

        val list = JList(fileListModel)
        list.cellRenderer = ArticleCellRenderer()
        return list
    }
}