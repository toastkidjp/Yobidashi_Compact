package jp.toastkid.yobidashi.compact.editor.service

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

class ClipboardFetcher {

    operator fun invoke(): String? {
        val transferData = Toolkit.getDefaultToolkit()
                .systemClipboard
                .getContents(this)
        if (transferData?.isDataFlavorSupported(DataFlavor.stringFlavor) == false) {
            return null
        }
        return transferData.getTransferData(DataFlavor.stringFlavor).toString()
    }
}