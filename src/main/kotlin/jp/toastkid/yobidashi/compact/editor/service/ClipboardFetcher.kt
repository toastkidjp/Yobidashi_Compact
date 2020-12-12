package jp.toastkid.yobidashi.compact.editor.service

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor

class ClipboardFetcher(private val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard) {

    operator fun invoke(): String? {
        val transferData = clipboard.getContents(this)
        return if (transferData?.isDataFlavorSupported(DataFlavor.stringFlavor) == false) null
        else transferData.getTransferData(DataFlavor.stringFlavor).toString()
    }
}