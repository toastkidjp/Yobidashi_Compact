package jp.toastkid.yobidashi.compact.editor.finder

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea

class FinderService(private val editorArea: RSyntaxTextArea) {

    private var lastFound = 0

    operator fun invoke(order: FindOrder) {
        if (order.invokeReplace) {
            var indexOf = editorArea.text.indexOf(order.target)
            while (indexOf != -1) {
                editorArea.replaceRange(order.replace, indexOf, indexOf + order.replace.length)
                indexOf = editorArea.text.indexOf(order.target, indexOf + 1)
            }
            return
        }

        val indexOf = if (order.upper) {
            editorArea.text.lastIndexOf(order.target, lastFound - 1)
        } else {
            editorArea.text.indexOf(order.target, lastFound + 1)
        }
        if (indexOf == -1) {
            return
        }
        lastFound = indexOf

        editorArea.selectionStart = indexOf
        editorArea.selectionEnd = indexOf + order.target.length
    }

}