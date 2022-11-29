package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.calendar.view.CalendarPanel
import jp.toastkid.yobidashi.compact.service.DateArticleUrlFactoryService
import jp.toastkid.yobidashi.compact.service.UnixTimeConverterService
import jp.toastkid.yobidashi.compact.service.UrlOpenerService
import jp.toastkid.yobidashi.compact.web.private.search.PrivateImageSearchService
import jp.toastkid.yobidashi.compact.web.search.WebSearchService
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.net.URI
import java.time.LocalDateTime
import java.time.OffsetDateTime
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class ToolMenuView(private val urlOpenerService: UrlOpenerService = UrlOpenerService()) {

    private val zoneOffset = OffsetDateTime.now().offset

    operator fun invoke(): JMenu {
        val menu = JMenu("Tool")

        menu.add(JMenuItem("Calendar").also {
            it.addActionListener {
                SubjectPool.addNewTab(CalendarPanel(), "Calendar")
            }
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK)
        })

        menu.add(JMenuItem("Web search").also {
            it.addActionListener {
                WebSearchService().invoke()
            }
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK)
        })

        menu.add(JMenuItem("Private image search").also {
            it.addActionListener {
                PrivateImageSearchService().invoke()
            }
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK)
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

        menu.add(JMenuItem("UNIX TIME").also {
            it.addActionListener {
                UnixTimeConverterService().invoke()
            }
        })

        return menu
    }

}
