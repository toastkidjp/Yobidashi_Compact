package jp.toastkid.yobidashi.compact.editor.finder

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea

class FinderService(private val editorArea: RSyntaxTextArea) {

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
            return
        }
        lastFound = indexOf

        editorArea.selectionStart = indexOf
        editorArea.selectionEnd = indexOf + order.target.length
    }

}