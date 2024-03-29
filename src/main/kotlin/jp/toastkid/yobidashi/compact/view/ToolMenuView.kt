package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.calendar.view.CalendarPanel
import jp.toastkid.yobidashi.compact.service.DateArticleUrlFactoryService
import jp.toastkid.yobidashi.compact.service.FileRenameService
import jp.toastkid.yobidashi.compact.service.UnixTimeConverterService
import jp.toastkid.yobidashi.compact.service.UrlEncoderService
import jp.toastkid.yobidashi.compact.service.UrlOpenerService
import jp.toastkid.yobidashi.compact.web.private.search.PrivateImageSearchService
import jp.toastkid.yobidashi.compact.web.search.WebSearchService
import java.awt.event.KeyEvent
import java.net.URI
import java.time.LocalDateTime
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class ToolMenuView(
    private val fileRenameService: FileRenameService = FileRenameService(),
    private val urlOpenerService: UrlOpenerService = UrlOpenerService(),
    private val unixTimeConverterService: UnixTimeConverterService = UnixTimeConverterService(),
    private val urlEncoderService: UrlEncoderService = UrlEncoderService()
) {

    operator fun invoke(): JMenu {
        val menu = JMenu("Tool")

        menu.add(JMenuItem("Calendar").also {
            it.addActionListener {
                SubjectPool.addNewTab(CalendarPanel(), "Calendar")
            }
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK)
        })

        menu.add(JMenuItem("Web search").also {
            it.addActionListener {
                WebSearchService().invoke()
            }
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK)
        })

        menu.add(JMenuItem("Private image search").also {
            it.addActionListener {
                PrivateImageSearchService().invoke()
            }
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK)
        })

        menu.add(JMenuItem("What happened today").also {
            it.addActionListener {
                val dateTime = LocalDateTime.now()
                val url = DateArticleUrlFactoryService().invoke(dateTime.month.value, dateTime.dayOfMonth)
                urlOpenerService(URI(url))
            }
        })

        menu.add(JMenuItem("Today(Yahoo! Kids)").also {
            it.addActionListener {
                urlOpenerService(URI("https://kids.yahoo.co.jp/today/"))
            }
        })

        menu.add(JMenuItem("Google Trends").also {
            it.addActionListener {
                urlOpenerService(URI("https://trends.google.co.jp/trends/trendingsearches/realtime"))
            }
        })

        menu.add(JMenuItem("UNIX TIME Converter").also {
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK)
            it.addActionListener {
                unixTimeConverterService.invoke()
            }
        })

        menu.add(JMenuItem("URL Encoder").also {
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK)
            it.addActionListener {
                urlEncoderService.invoke()
            }
        })

        menu.add(JMenuItem("Files rename").also {
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK)
            it.addActionListener {
                fileRenameService.invoke()
            }
        })

        return menu
    }

}
