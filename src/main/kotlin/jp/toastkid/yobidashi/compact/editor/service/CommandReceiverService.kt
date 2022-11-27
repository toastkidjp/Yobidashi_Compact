package jp.toastkid.yobidashi.compact.editor.service

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import jp.toastkid.yobidashi.compact.editor.model.Editing
import jp.toastkid.yobidashi.compact.editor.text.BlockQuotation
import jp.toastkid.yobidashi.compact.editor.text.ListHeadAdder
import jp.toastkid.yobidashi.compact.editor.text.NumberedListHeadAdder
import jp.toastkid.yobidashi.compact.editor.text.TableFormConverter
import jp.toastkid.yobidashi.compact.editor.text.TrimmingService
import jp.toastkid.yobidashi.compact.editor.view.EditorAreaView
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.service.UrlOpenerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import java.io.IOException
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
        private val switchFinder: () -> Unit,
        private val close: () -> Unit,
        private val urlOpenerService: UrlOpenerService = UrlOpenerService()
) {

    suspend operator fun invoke() {
        channel.receiveAsFlow().collect { command ->
            when (command) {
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
                    val text = withContext(Dispatchers.IO) { ClipboardFetcher().invoke() } ?: return@collect
                    val quotedText = BlockQuotation().invoke(text) ?: return@collect
                    editorAreaView.insertText(quotedText)
                }
                MenuCommand.PASTE_LINK_WITH_TITLE -> {
                    val text = withContext(Dispatchers.IO) { ClipboardFetcher().invoke() } ?: return@collect
                    val decorated = LinkDecoratorService().invoke(text)
                    editorAreaView.insertText(decorated)
                }
                MenuCommand.FIND -> {
                    switchFinder()
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
                MenuCommand.TRIMMING -> {
                    editorAreaView.replaceSelected { text ->
                        TrimmingService().invoke(text) ?: text
                    }
                }
                MenuCommand.CODE_BLOCK -> editorAreaView.replaceSelected { "```\n$it```" }
                MenuCommand.ITALIC -> editorAreaView.replaceSelected { "*$it*" }
                MenuCommand.BOLD -> editorAreaView.replaceSelected { "**$it**" }
                MenuCommand.STRIKETHROUGH -> editorAreaView.replaceSelected { "~~$it~~" }
                MenuCommand.FONT_COLOR -> {
                    val color = ColorChooserService().invoke() ?: return@collect
                    editorAreaView.replaceSelected { text ->
                        "<font color='#${Integer.toHexString(color.rgb)}'>$text</font>"
                    }
                }
                MenuCommand.TO_HYPERLINK -> {
                    editorAreaView.replaceSelected { text ->
                        LinkDecoratorService().invoke(text)
                    }
                }
                MenuCommand.HORIZONTAL_RULE -> {
                    editorAreaView.insertText("---")
                }
                MenuCommand.REVERSE_CASE -> {
                    editorAreaView.replaceSelected(true) {
                        if (it.isEmpty()) return@replaceSelected it
                        return@replaceSelected if (it.toCharArray()[0].isUpperCase()) it.lowercase() else it.toUpperCase()
                    }
                }
                MenuCommand.DUPLICATE_LINE -> {
                    editorAreaView.duplicateLine()
                }
                MenuCommand.SWITCH_EDITABLE -> {
                    editorAreaView.switchEditable()
                }
                MenuCommand.COUNT -> {
                    JOptionPane.showMessageDialog(
                            null,
                            "Count: ${editorAreaView.count()}"
                    )
                }
                MenuCommand.WEB_SEARCH -> {
                    val selectedText = editorAreaView.selectedText()
                    if (selectedText.isBlank()) {
                        return@collect
                    }
                    urlOpenerService("https://search.yahoo.co.jp/search?p=${encodeUtf8(selectedText)}")
                }
                MenuCommand.DICTIONARY_SEARCH -> {
                    val selectedText = editorAreaView.selectedText()
                    if (selectedText.isBlank()) {
                        return@collect
                    }
                    urlOpenerService("https://ejje.weblio.jp/content/${encodeUtf8(selectedText)}")
                }
                MenuCommand.TRANSLATION_TO_ENGLISH -> {
                    val selectedText = editorAreaView.selectedText()
                    if (selectedText.isBlank()) {
                        return@collect
                    }
                    urlOpenerService("https://translate.google.co.jp/?hl=en&sl=auto&tl=en&text=${encodeUtf8(selectedText)}&op=translate")
                }
                MenuCommand.REFRESH -> {
                    editorAreaView.refresh()
                }
            }
        }
    }

    private fun encodeUtf8(selectedText: String) = URLEncoder.encode(selectedText, StandardCharsets.UTF_8.name())
}