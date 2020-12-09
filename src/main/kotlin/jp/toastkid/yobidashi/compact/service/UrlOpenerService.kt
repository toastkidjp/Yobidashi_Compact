package jp.toastkid.yobidashi.compact.service

import java.awt.Desktop
import java.net.URI

class UrlOpenerService(private val desktop: Desktop = Desktop.getDesktop()) {

    operator fun invoke(url: String?) {
        if (url.isNullOrBlank()) {
            return
        }

        desktop.browse(URI(url))
    }
}