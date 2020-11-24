package jp.toastkid.yobidashi.compact.editor.service

import jp.toastkid.yobidashi.compact.editor.EditorAreaView
import jp.toastkid.yobidashi.compact.editor.MenuCommand
import jp.toastkid.yobidashi.compact.editor.TableFormConverter
import jp.toastkid.yobidashi.compact.editor.finder.FindOrder
import jp.toastkid.yobidashi.compact.editor.finder.FinderFrame
import jp.toastkid.yobidashi.compact.editor.model.Editing
import jp.toastkid.yobidashi.compact.editor.text.BlockQuotation
import jp.toastkid.yobidashi.compact.editor.text.ListHeadAdder
import jp.toastkid.yobidashi.compact.editor.text.NumberedListHeadAdder
import jp.toastkid.yobidashi.compact.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.io.IOException
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import javax.swing.JOptionPane

class CommandReceiverService(
        private val channel: Channel<MenuCommand>,
        private val editorAreaView: EditorAreaView,
        private val currentArticle: () -> Article?,
        private val editing: Editing,
        private val resetFrameTitle: () -> Unit,
        private val close: () -> Unit
) {

    suspend operator fun invoke() {
        channel.receiveAsFlow().collect {
            when (it) {
                MenuCommand.SAVE -> {
                    val article = currentArticle() ?: return@collect
                    try {
                        withContext(Dispatchers.IO) { Files.write(article.path(), editorAreaView.getTextArray()) }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    editing.clear()
                    resetFrameTitle()
                }
                MenuCommand.CLOSE -> close()
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
                MenuCommand.TO_TABLE -> {
                    editorAreaView.replaceSelected { text ->
                        TableFormConverter().invoke(text)
                    }
                }
                MenuCommand.UNORDERED_LIST -> {
                    editorAreaView.replaceSelected { text ->
                        ListHeadAdder().invoke(text, "-") ?: text
                    }
                }
                MenuCommand.ORDERED_LIST -> {
                    editorAreaView.replaceSelected { text ->
                        NumberedListHeadAdder().invoke(text) ?: text
                    }
                }
                MenuCommand.TASK_LIST -> {
                    editorAreaView.replaceSelected { text ->
                        ListHeadAdder().invoke(text, "- [ ]") ?: text
                    }
                }
                MenuCommand.BLOCKQUOTE -> {
                    editorAreaView.replaceSelected { text ->
                        BlockQuotation().invoke(text) ?: text
                    }
                }
                MenuCommand.CODE_BLOCK -> {
                    editorAreaView.replaceSelected { text ->
                        "```\n$text```"
                    }
                }
                MenuCommand.ITALIC -> {
                    editorAreaView.replaceSelected { text ->
                        "*$text*"
                    }
                }
                MenuCommand.BOLD -> {
                    editorAreaView.replaceSelected { text ->
                        "**$text**"
                    }
                }
                MenuCommand.STRIKETHROUGH -> {
                    editorAreaView.replaceSelected { text ->
                        "~~$text~~"
                    }
                }
                MenuCommand.FONT_COLOR -> {
                    val color = ColorChooserService().invoke() ?: return@collect
                    editorAreaView.replaceSelected { text ->
                        "<font color='#${Integer.toHexString(color.rgb)}'>$text</font>"
                    }
                }
                MenuCommand.COUNT -> {
                    JOptionPane.showMessageDialog(
                            null,
                            "Count: ${editorAreaView.count()}"
                    )
                }
                MenuCommand.WEB_SEARCH -> {
                    val selectedText = editorAreaView.selectedText()
                    if (selectedText.isNullOrBlank()) {
                        return@collect
                    }
                    Desktop.getDesktop().browse(URI("https://search.yahoo.co.jp/search?p=${URLEncoder.encode(selectedText, StandardCharsets.UTF_8.name())}"))
                }
            }
        }
    }
}