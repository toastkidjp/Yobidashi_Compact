package jp.toastkid.yobidashi.compact.editor

import jp.toastkid.yobidashi.compact.editor.finder.FindOrder
import jp.toastkid.yobidashi.compact.editor.finder.FinderFrame
import jp.toastkid.yobidashi.compact.editor.model.Editing
import jp.toastkid.yobidashi.compact.editor.text.BlockQuotation
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
import java.awt.BorderLayout
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.io.IOException
import java.nio.file.Files
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

class EditorFrame {

    private val frame = JFrame("Editor")

    private var currentArticle: Article? = null

    private val editorAreaView = EditorAreaView()

    private val editing = Editing()

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
        frame.setBounds(200, 80, 1200, 600)

        Setting.lookAndFeel()?.let {
            UiUpdaterService().invoke(frame, it)
        }

        panel.add(editorAreaView.view(), BorderLayout.CENTER)

        val footer = JPanel(BorderLayout())
        footer.add(statusLabel, BorderLayout.EAST)
        panel.add(footer, BorderLayout.SOUTH)

        editorAreaView.receiveStatus {
            setStatus("Character: $it")
            editing.setCurrentSize(it)
            resetFrameTitle()
        }
    }

    private suspend fun receiveCommand(channel: Channel<MenuCommand>) {
        channel.receiveAsFlow().collect {
            when (it) {
                MenuCommand.SAVE -> {
                    val article = currentArticle ?: return@collect
                    try {
                        withContext(Dispatchers.IO) { Files.write(article.path(), editorAreaView.getTextArray()) }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    editing.clear()
                    resetFrameTitle()
                }
                MenuCommand.CLOSE -> frame.dispose()
                MenuCommand.PASTE_AS_QUOTATION -> {
                    val text = withContext(Dispatchers.IO) {
                        val transferData = Toolkit.getDefaultToolkit()
                                .systemClipboard
                                .getContents(this)
                        if (transferData?.isDataFlavorSupported(DataFlavor.stringFlavor) == false) {
                            return@withContext null
                        }
                        transferData.getTransferData(DataFlavor.stringFlavor).toString()
                    } ?: return@collect
                    val quotedText = BlockQuotation().invoke(text) ?: return@collect
                    editorAreaView.insertText(quotedText)
                }
                MenuCommand.FIND -> {
                    val finderChannel = Channel<FindOrder>()
                    val finderFrame = FinderFrame(finderChannel)
                    finderFrame.show()
                    finderChannel.receiveAsFlow().collect {
                        editorAreaView.find(it)
                    }
                }
            }
        }
    }

    fun load(article: Article) {
        currentArticle = article

        resetFrameTitle()

        val text = ArticleContentLoaderUseCase().invoke(article)
        editorAreaView.setText(text)
        editing.setCurrentSize(text.length)
        setStatus("Character: ${text.length}")
    }

    private fun resetFrameTitle() {
        val editingIndicator = if (editing.shouldShowIndicator()) " *" else ""
        frame.title = "${currentArticle?.getTitle()}$editingIndicator - Editor"
    }

    private fun setStatus(status: String) {
        statusLabel.text = status
    }

    fun show() {
        frame.isVisible = true
    }

}