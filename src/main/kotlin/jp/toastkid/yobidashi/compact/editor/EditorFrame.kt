package jp.toastkid.yobidashi.compact.editor

import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.UiUpdaterService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.IOException
import java.nio.file.Files
import javax.imageio.ImageIO
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenuItem
import javax.swing.JPanel

class EditorFrame {

    private val frame = JFrame("Editor")

    private val editorArea = RSyntaxTextArea()

    private var currentArticle: Article? = null

    private val statusLabel = JLabel()

    init {
        frame.iconImage = ImageIO.read(javaClass.classLoader.getResourceAsStream("images/icon.png"))

        val panel = JPanel()
        panel.layout = BorderLayout()

        val channel = Channel<MenuCommand>()
        frame.jMenuBar = MenubarView(channel).invoke(frame)

        CoroutineScope(Dispatchers.Swing).launch {
            receiveCommand(channel)
        }

        frame.contentPane.add(panel, BorderLayout.CENTER)
        frame.setBounds(200, 100, 900, 600)

        Setting.lookAndFeel()?.let {
            UiUpdaterService().invoke(frame, it)
        }

        editorArea.background = Color(220, 220, 220, 220)
        editorArea.paintTabLines = true
        editorArea.font = editorArea.font.deriveFont(DEFAULT_FONT_SIZE)
        editorArea.addKeyListener(object : KeyListener{
            override fun keyTyped(e: KeyEvent?) = Unit

            override fun keyPressed(e: KeyEvent?) = Unit

            override fun keyReleased(e: KeyEvent?) {
                statusLabel.text = "Character: ${editorArea.text.length}"
            }
        })

        val toTableMenu = JMenuItem("To table")
        toTableMenu.action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                editorArea.selectedText.also { text ->
                    editorArea.replaceSelection(TableFormConverter().invoke(text))
                }
            }
        }
        editorArea.popupMenu.add(toTableMenu)

        val scrollArea = RTextScrollPane(editorArea)
        scrollArea.lineNumbersEnabled = true
        scrollArea.isIconRowHeaderEnabled = true
        scrollArea.gutter.lineNumberFont = editorArea.font.deriveFont(DEFAULT_FONT_SIZE)
        panel.add(scrollArea, BorderLayout.CENTER)

        val footer = JPanel(BorderLayout())
        footer.add(statusLabel, BorderLayout.EAST)
        panel.add(footer, BorderLayout.SOUTH)
    }

    private suspend fun receiveCommand(channel: Channel<MenuCommand>) {
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

    fun load(article: Article) {
        currentArticle = article

        frame.title = "${article.getTitle()} - Editor"
        val lineSeparator = System.lineSeparator()
        editorArea.text = Files.readAllLines(article.path()).reduce { base, item -> "$base$lineSeparator$item" }
        editorArea.caretPosition = 0
        statusLabel.text = "Character: ${editorArea.text.length}"
    }

    fun show() {
        frame.isVisible = true
    }

    companion object {
        private const val DEFAULT_FONT_SIZE = 16f
    }
}