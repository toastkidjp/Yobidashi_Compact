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
        var indexOf = editorArea.text.indexOf(order.target)

        if (indexOf == -1) {
            CoroutineScope(Dispatchers.Default).launch { messageChannel.send("'${order.target}' is not found.") }
            return
        }
        CoroutineScope(Dispatchers.Default).launch { messageChannel.send("") }

        while (indexOf != -1) {
            editorArea.replaceRange(order.replace, indexOf, indexOf + order.target.length)
            indexOf = editorArea.text.indexOf(order.target, indexOf + 1)
        }
    }

    private fun find(order: FindOrder) {
        val indexOf = if (order.upper) {
            editorArea.text.lastIndexOf(order.target, lastFound - 1)
        } else {
            editorArea.text.indexOf(order.target, lastFound + 1)
        }
        if (indexOf == -1) {
            CoroutineScope(Dispatchers.Default).launch { messageChannel.send("'${order.target}' is not found.") }
            return
        }
        CoroutineScope(Dispatchers.Default).launch { messageChannel.send("") }
        lastFound = indexOf

        editorArea.selectionStart = indexOf
        editorArea.selectionEnd = indexOf + order.target.length
    }

}