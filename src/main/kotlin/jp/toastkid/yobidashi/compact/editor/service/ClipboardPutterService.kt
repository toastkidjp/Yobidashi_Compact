package jp.toastkid.yobidashi.compact.editor.service

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection

class ClipboardPutterService(private val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard) {

    operator fun invoke(string: String?) {
        clipboard.setContents(StringSelection(string)) { _, _ -> }
    }

}