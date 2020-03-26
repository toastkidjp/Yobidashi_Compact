package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.TodayFileTitleGenerator
import jp.toastkid.yobidashi.compact.service.ZipArchiver
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import javax.swing.*

class FileMenuView(private val addToList: (Article) -> Unit) {

    operator fun invoke(): JMenu {
        val fileMenu = JMenu("File(F)")
        fileMenu.setMnemonic('F')

        fileMenu.add(makeTodayMenuItem())
        fileMenu.add(makeNewMenuItem())
        fileMenu.add(makeZipMenuItem())
        return fileMenu
    }

    private fun makeTodayMenuItem(): JMenuItem {
        val item = JMenuItem("Make today")
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK)
        item.addActionListener {
            val article = Article.withTitle(TodayFileTitleGenerator().invoke(System.currentTimeMillis()))
            article.makeFieIfNeed()
            addToList(article)
        }
        return item
    }

    private fun makeNewMenuItem(): JMenuItem {
        val item = JMenuItem("Make new")
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK)
        item.addActionListener {
            val dialog = JOptionPane.showInputDialog(this, "Please input new article name.")
            if (dialog.isNullOrBlank()) {
                return@addActionListener
            }
            val article = Article.withTitle(dialog)
            article.makeFieIfNeed()
            addToList(article)
        }
        return item
    }

    private fun makeZipMenuItem(): JMenuItem {
        val item = JMenuItem("Zip")
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK)
        item.addActionListener {
            val paths = Files.list(Paths.get(Setting.articleFolder()))
                    .sorted { p1, p2 -> Files.getLastModifiedTime(p1).compareTo(Files.getLastModifiedTime(p2)) * -1 }
                    .filter {
                        val name = it.fileName.toString()
                        name.startsWith("20") || name.startsWith("『")
                    }
                    .collect(Collectors.toList())
                    .subList(0, 1500)
            ZipArchiver().invoke(paths)
        }
        return item
    }

}