package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.calendar.view.CalendarPanel
import jp.toastkid.yobidashi.compact.service.DateArticleUrlFactoryService
import jp.toastkid.yobidashi.compact.service.UnixTimeConverterService
import jp.toastkid.yobidashi.compact.service.UrlOpenerService
import jp.toastkid.yobidashi.compact.web.private.search.PrivateImageSearchService
import jp.toastkid.yobidashi.compact.web.search.WebSearchService
import java.awt.Dimension
import java.awt.event.InputEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.OffsetDateTime
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.KeyStroke

class ToolMenuView(
    private val urlOpenerService: UrlOpenerService = UrlOpenerService(),
    private val unixTimeConverterService: UnixTimeConverterService = UnixTimeConverterService()
) {

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

        menu.add(JMenuItem("UNIX TIME Converter").also {
            it.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK)
            it.addActionListener {
                unixTimeConverterService.invoke()
            }
        })

        menu.add(JMenuItem("URL Encoder").also {
            it.addActionListener {
                val panel = JPanel()
                panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
                val unixTimeInput = JTextField()
                val offset = OffsetDateTime.now().offset
                unixTimeInput.text = "東京特許 許可局"
                val dateTime = JTextField()
                dateTime.text = URLEncoder.encode(unixTimeInput.text, StandardCharsets.UTF_8.name())

                unixTimeInput.preferredSize = Dimension(100, 24)
                unixTimeInput.addKeyListener(object : KeyAdapter() {
                    override fun keyReleased(e: KeyEvent?) {
                        dateTime.text = URLEncoder.encode(unixTimeInput.text, StandardCharsets.UTF_8.name())
                    }
                })
                panel.add(JLabel("Please would you input some words."))
                panel.add(JLabel("Raw string"))
                panel.add(unixTimeInput)
                panel.add(JLabel("Date time"))
                dateTime.preferredSize = Dimension(100, 24)
                dateTime.addKeyListener(object : KeyAdapter() {
                    override fun keyReleased(e: KeyEvent?) {
                        unixTimeInput.text = URLDecoder.decode(dateTime.text, StandardCharsets.UTF_8.name())
                    }
                })
                panel.add(dateTime)
                JOptionPane.showMessageDialog(null, panel)
            }
        })

        return menu
    }

}
