package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.calendar.view.CalendarPanel
import jp.toastkid.yobidashi.compact.service.DateArticleUrlFactoryService
import java.awt.Desktop
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.KeyStroke

class ToolMenuView {

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
                val input = JOptionPane.showInputDialog("Please would you input search query?")
                if (input.isNullOrBlank()) {
                    return@addActionListener
                }
                Desktop.getDesktop().browse(URI("https://search.yahoo.co.jp/search?p=${URLEncoder.encode(input, StandardCharsets.UTF_8.name())}"))
            }
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK)
        })
        menu.add(JMenuItem("What happened today").also {
            it.addActionListener {
                val dateTime = LocalDateTime.now()
                val url = DateArticleUrlFactoryService().invoke(dateTime.month.value, dateTime.dayOfMonth)
                Desktop.getDesktop().browse(URI(url))
            }
        })
        menu.add(JMenuItem("Today(Yahoo! Kids)").also {
            it.addActionListener {
                Desktop.getDesktop().browse(URI("https://kids.yahoo.co.jp/today/"))
            }
        })
        menu.add(JMenuItem("Google Trends").also {
            it.addActionListener {
                Desktop.getDesktop().browse(URI("https://trends.google.co.jp/trends/trendingsearches/realtime"))
            }
        })
        return menu
    }

}
