package jp.toastkid.yobidashi.compact.editor.service

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

class ClipboardFetcher {

    operator fun invoke(): String? {
        val transferData = Toolkit.getDefaultToolkit()
                .systemClipboard
                .getContents(this)
        return if (transferData?.isDataFlavorSupported(DataFlavor.stringFlavor) == false) null
        else transferData.getTransferData(DataFlavor.stringFlavor).toString()
    }
}