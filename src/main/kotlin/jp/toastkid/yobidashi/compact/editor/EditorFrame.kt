package jp.toastkid.yobidashi.compact.editor

import jp.toastkid.yobidashi.compact.editor.finder.FindOrder
import jp.toastkid.yobidashi.compact.editor.finder.FinderAreaView
import jp.toastkid.yobidashi.compact.editor.model.Editing
import jp.toastkid.yobidashi.compact.editor.service.ArticleContentLoaderService
import jp.toastkid.yobidashi.compact.editor.service.CommandReceiverService
import jp.toastkid.yobidashi.compact.editor.service.DisposeActionService
import jp.toastkid.yobidashi.compact.editor.view.EditorAreaView
import jp.toastkid.yobidashi.compact.editor.view.MenuBarView
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
import java.awt.BorderLayout
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

class EditorFrame(
    private val frame: JFrame = JFrame("Editor"),
    private val editing: Editing = Editing(),
    private val statusLabel: JLabel = JLabel()
) {

    private val editorAreaView: EditorAreaView

    private var currentArticle: Article? = null

    init {
        frame.iconImage = ImageIO.read(javaClass.classLoader.getResourceAsStream("images/icon.png"))

        val panel = JPanel()
        panel.layout = BorderLayout()

        val channel = Channel<MenuCommand>()
        frame.jMenuBar = MenuBarView(channel).invoke(frame)
        frame.contentPane.add(panel, BorderLayout.CENTER)
        frame.setBounds(200, 80, 1200, 600)
        frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE

        Setting.lookAndFeel()?.let {
            UiUpdaterService().invoke(frame, it)
        }

        val messageChannel = Channel<String>()
        editorAreaView = EditorAreaView(channel = channel, messageChannel = messageChannel)

        panel.add(editorAreaView.view(), BorderLayout.CENTER)

        val footer = JPanel(BorderLayout())
        statusLabel.font = statusLabel.font.deriveFont(16f)
        footer.add(statusLabel, BorderLayout.EAST)
        panel.add(footer, BorderLayout.SOUTH)

        val finderChannel = Channel<FindOrder>()
        val finderView = FinderAreaView(finderChannel, messageChannel)

        panel.add(finderView.view(), BorderLayout.NORTH)
        CoroutineScope(Dispatchers.Default).launch {
            finderChannel.receiveAsFlow().collect {
                editorAreaView.find(it)
            }
        }

        editorAreaView.receiveStatus {
            setStatus("Character: $it")
            editing.setCurrentSize(it)
            resetFrameTitle()
        }

        val commandReceiverService = CommandReceiverService(
                channel,
                editorAreaView,
                { currentArticle },
                editing,
                this::resetFrameTitle,
                { finderView.switchVisibility() },
                this::dispose
        )
        CoroutineScope(Dispatchers.Swing).launch {
            commandReceiverService()
        }
    }

    fun load(article: Article) {
        currentArticle = article

        resetFrameTitle()

        val text = ArticleContentLoaderService().invoke(article)
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

    private fun dispose() {
        DisposeActionService(frame).invoke(editing.shouldShowIndicator().not())
    }

}