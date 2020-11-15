package jp.toastkid.yobidashi.compact.editor

import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.UiUpdaterService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.swing.SwingDispatcher
import kotlinx.coroutines.withContext
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.io.IOException
import java.nio.file.Files
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel

class EditorFrame {

    private val frame = JFrame("Editor")

    private val editorArea = RSyntaxTextArea()

    private var currentArticle: Article? = null

    init {
        frame.iconImage = ImageIO.read(javaClass.classLoader.getResourceAsStream("images/icon.png"))

        val panel = JPanel()
        panel.layout = BorderLayout()

        val channel = Channel<MenuCommand>()
        frame.jMenuBar = MenubarView(channel).invoke(frame)
        CoroutineScope(Dispatchers.Swing).launch {
            channel.receiveAsFlow().collect {
                when (it) {
                    MenuCommand.SAVE -> {
                        val article = currentArticle ?: return@collect
                        try {
                            withContext(Dispatchers.IO) { Files.write(article.path(), editorArea.text.toByteArray()) }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    MenuCommand.CLOSE -> frame.dispose()
                }
            }
        }

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