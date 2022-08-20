package jp.toastkid.yobidashi.compact.media

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.editor.service.ClipboardPutterService
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.nio.file.Path
import javax.swing.AbstractAction
import javax.swing.JList
import javax.swing.JOptionPane
import javax.swing.JPopupMenu
import javax.swing.JScrollPane
import kotlin.io.path.nameWithoutExtension

class MediaListView {

    private val fileListModel = MediaFileListModel()

    private val view: JList<Path>

    private val scrollPane = JScrollPane()

    private val mediaPlayerInvoker = MediaPlayerInvoker()

    init {
        view = initializeView()

        scrollPane.viewport.view = view
        scrollPane.preferredSize = Dimension(400, 800)
    }

    fun view() = scrollPane

    private fun initializeView(): JList<Path> {
        return JList(fileListModel).also {
            it.cellRenderer = MediaCellRenderer()
            it.componentPopupMenu = JPopupMenu()
            var currentFocused: Path? = null
            it.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    if (e?.clickCount ?: 0 >= 2) {
                        mediaPlayerInvoker.invoke(view.selectedValue)
                    }
                }

                override fun mousePressed(e: MouseEvent?) {
                    extractFocused(e)
                }

                override fun mouseReleased(e: MouseEvent?) {
                    extractFocused(e)
                }

                private fun extractFocused(e: MouseEvent?) {
                    if (e?.isPopupTrigger != false) {
                        return
                    }
                    currentFocused = it.model.getElementAt(it.locationToIndex(it.mousePosition))
                }
            })
            it.componentPopupMenu.add(object : AbstractAction("Open") {
                override fun actionPerformed(e: ActionEvent?) {
                    if (it.isSelectionEmpty) {
                        currentFocused?.let {
                            mediaPlayerInvoker.invoke(it)
                        }
                        return
                    }

                    it.selectedValuesList?.forEach {  mediaPlayerInvoker.invoke(it) }
                }
            })
            it.componentPopupMenu.add(object : AbstractAction("Count characters") {
                override fun actionPerformed(e: ActionEvent?) {
                    val message = if (it.isSelectionEmpty) {
                        "${currentFocused?.nameWithoutExtension}: ${currentFocused?.count()}"
                    } else {
                        val lineSeparator = System.lineSeparator()
                        it.selectedValuesList?.map { article -> "${article.nameWithoutExtension}: ${article.count()}" }
                                ?.reduce { base, item -> "$base$lineSeparator$item" }
                    } ?: return
                    JOptionPane.showMessageDialog(null, message, "Count", JOptionPane.INFORMATION_MESSAGE)
                }
            })
            it.componentPopupMenu.add(object : AbstractAction("Copy title") {
                override fun actionPerformed(e: ActionEvent?) {
                    if (it.isSelectionEmpty) {
                        ClipboardPutterService()(currentFocused?.nameWithoutExtension)
                        return
                    }

                    ClipboardPutterService()(
                            it.selectedValuesList?.map { article -> article.nameWithoutExtension }
                                    ?.reduce { base, item -> "$base, $item" }
                    )
                }
            })
            it.componentPopupMenu.add(object : AbstractAction("Copy title as internal link") {
                override fun actionPerformed(e: ActionEvent?) {
                    if (it.isSelectionEmpty) {
                        ClipboardPutterService()("[[${currentFocused?.nameWithoutExtension}]]")
                        return
                    }
                    ClipboardPutterService()(
                            it.selectedValuesList?.map { article -> "[[${article.nameWithoutExtension}]]" }
                                    ?.reduce { base, item -> "$base, $item" }
                    )
                }
            })
            it.addKeyListener(object : KeyAdapter() {
                override fun keyPressed(e: KeyEvent?) {
                    super.keyPressed(e)
                    if (e?.keyCode == KeyEvent.VK_ENTER) {
                        mediaPlayerInvoker.invoke(view.selectedValue)
                    }
                }
            })
        }
    }

    fun addAll(items: Collection<Path>) {
        fileListModel.addAll(items) { SubjectPool.refreshUi(view) }
    }

}