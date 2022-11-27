package jp.toastkid.yobidashi.compact.web.private.search

import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.UrlOpenerService
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel

class PrivateImageSearchService(private val urlOpenerService: UrlOpenerService = UrlOpenerService()) {

    operator fun invoke() {
        val content = JPanel().also { panel ->
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            panel.add(JLabel(MESSAGE))
        }
        val input = JOptionPane.showInputDialog(content)
        try {
            URL(input)
        } catch (e: MalformedURLException) {
            null
        } ?: return

        println(Setting.privateSearchPath())

        Runtime.getRuntime().exec(
                arrayOf(
                        Setting.privateSearchPath(),
                        Setting.privateSearchOption(),
                        "https://www.bing.com/images/search?view=detailv2&iss=sbi&q=imgurl:${URLEncoder.encode(input, StandardCharsets.UTF_8.name())}"
                )
        )
    }

    companion object {
        private const val MESSAGE = "Please would you input search image URL?"
    }

}