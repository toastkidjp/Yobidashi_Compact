package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.UiUpdaterService
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.nio.file.Files
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

class EditorFrame {

    private val frame = JFrame("Editor")

    private val editorArea = RSyntaxTextArea()

    private var currentArticle: Article? = null

    init {
        frame.iconImage = ImageIO.read(javaClass.classLoader.getResourceAsStream("images/icon.png"))

        val panel = JPanel()
        panel.layout = BorderLayout()

        frame.jMenuBar = MenuBarView().invoke(frame)
        frame.contentPane.add(panel, BorderLayout.CENTER)
        frame.setBounds(200, 100, 900, 600)

        Setting.lookAndFeel()?.let {
            UiUpdaterService().invoke(frame, it)
        }

        editorArea.paintTabLines = true
        editorArea.font = editorArea.font.deriveFont(DEFAULT_FONT_SIZE)

        val scrollArea = RTextScrollPane(editorArea)
        scrollArea.lineNumbersEnabled = true
        scrollArea.isIconRowHeaderEnabled = true
        scrollArea.gutter.lineNumberFont = editorArea.font.deriveFont(DEFAULT_FONT_SIZE)
        panel.add(scrollArea, BorderLayout.CENTER)
    }

    fun load(article: Article) {
        currentArticle = article

        frame.title = "${article.getTitle()} - Editor"
        val lineSeparator = System.lineSeparator()
        editorArea.text = Files.readAllLines(article.path()).reduce { base, item -> "$base$lineSeparator$item" }
        editorArea.caretPosition = 0
    }

    fun show() {
        frame.isVisible = true
    }

    companion object {
        private val DEFAULT_FONT_SIZE = 16f
    }
}