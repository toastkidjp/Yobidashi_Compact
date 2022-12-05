package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.calendar.view.CalendarPanel
import jp.toastkid.yobidashi.compact.service.DateArticleUrlFactoryService
import jp.toastkid.yobidashi.compact.service.UnixTimeConverterService
import jp.toastkid.yobidashi.compact.service.UrlEncoderService
import jp.toastkid.yobidashi.compact.service.UrlOpenerService
import jp.toastkid.yobidashi.compact.web.private.search.PrivateImageSearchService
import jp.toastkid.yobidashi.compact.web.search.WebSearchService
import java.awt.Dimension
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetAdapter
import java.awt.dnd.DropTargetDropEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.Files
import java.time.LocalDateTime
import javax.swing.BoxLayout
import javax.swing.DefaultListModel
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.KeyStroke
import kotlin.io.path.extension


class ToolMenuView(
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
                urlEncoderService.invoke()
            }
        })

        menu.add(JMenuItem("Files rename").also {
            it.addActionListener {
                val panel = JPanel()
                panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
                panel.add(JLabel("Please would you drop any file?"))
                val droppedFileList = JList<File>()
                droppedFileList.preferredSize = Dimension(300, 400)
                panel.add(droppedFileList)
                val defaultListModel = DefaultListModel<File>()
                droppedFileList.model = defaultListModel
                val dropTarget = DropTarget()
                dropTarget.addDropTargetListener(
                    object : DropTargetAdapter() {
                        override fun drop(dtde: DropTargetDropEvent?) {
                            println(dtde)
                            try {
                                if (dtde!!.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                                    dtde!!.acceptDrop(DnDConstants.ACTION_COPY)
                                    val transferable = dtde!!.transferable
                                    val list = transferable.getTransferData(
                                        DataFlavor.javaFileListFlavor
                                    ) as List<*>
                                    println("list ${list.size}")
                                    for (o in list) {
                                        if (o is File) {
                                            defaultListModel.addElement(o)
                                        }
                                    }
                                    dtde!!.dropComplete(true)
                                    return
                                }
                            } catch (ex: UnsupportedFlavorException) {
                                ex.printStackTrace()
                            } catch (ex: IOException) {
                                ex.printStackTrace()
                            }
                            dtde!!.rejectDrop()
                        }
                    }
                )
                panel.dropTarget = dropTarget
                panel.add(JLabel("Base file name"))
                val input = JOptionPane.showInputDialog(null, panel)
                if (input.isNullOrBlank() || defaultListModel.isEmpty) {
                    return@addActionListener
                }
                defaultListModel.elements().toList().map { it.toPath() }.forEachIndexed { i, p ->
                    Files.copy(p, p.resolveSibling("${input}_${i + 1}.${p.extension}"))
                }
            }
        })

        return menu
    }

}
