package jp.toastkid.yobidashi.compact.editor.finder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea

class FinderService(
        private val editorArea: RSyntaxTextArea,
        private val messageChannel: Channel<String>
) {

    private var lastFound = 0

    operator fun invoke(order: FindOrder) {
        if (order.invokeReplace) {
            replace(order)
            return
        }

        find(order)
    }

    private fun replace(order: FindOrder) {
        var indexOf = editorArea.text.indexOf(order.target, 0, order.caseSensitive.not())

        if (indexOf == -1) {
            showMessage("'${order.target}' is not found.")
            return
        }
        showMessage("")

        while (indexOf != -1) {
            editorArea.replaceRange(order.replace, indexOf, indexOf + order.target.length)
            indexOf = editorArea.text.indexOf(order.target, indexOf + 1, order.caseSensitive.not())
        }
    }

    private fun find(order: FindOrder) {
        val indexOf = if (order.upper) {
            editorArea.text.lastIndexOf(order.target, lastFound - 1, order.caseSensitive.not())
        } else {
            editorArea.text.indexOf(order.target, lastFound + 1, order.caseSensitive.not())
        }
        if (indexOf == -1) {
            showMessage("'${order.target}' is not found.")
            return
        }
        showMessage("")
        lastFound = indexOf

        editorArea.selectionStart = indexOf
        editorArea.selectionEnd = indexOf + order.target.length
    }

    private fun showMessage(message: String) {
        CoroutineScope(Dispatchers.Default).launch { messageChannel.send(message) }
    }

}