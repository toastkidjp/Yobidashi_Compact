package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.TodayFileTitleGenerator
import jp.toastkid.yobidashi.compact.service.ZipArchiver
import jp.toastkid.yobidashi.compact.viewmodel.ZipViewModel
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import javax.swing.*

class FileMenuView(private val addToList: (Article) -> Unit) {

    operator fun invoke(): JMenu {
        val fileMenu = JMenu("File(F)")
        fileMenu.setMnemonic('F')

        fileMenu.add(makeTodayMenuItem())
        fileMenu.add(makeNewMenuItem())
        fileMenu.add(makeZipAllMenuItem())
        fileMenu.add(makeZipDiaryMenuItem())
        fileMenu.add(makeExit())
        return fileMenu
    }

    private fun makeExit(): JMenuItem {
        val item = JMenuItem("Exit")
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK)
        item.addActionListener {
            SubjectPool.closeWindow()
        }
        return item
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

    private fun makeZipAllMenuItem(): JMenuItem {
        val item = JMenuItem("Zip all")
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK)
        item.addActionListener {
            ZipViewModel.zip()
        }
        return item
    }

    private fun makeZipDiaryMenuItem(): JMenuItem {
        val item = JMenuItem("Zip diaries")
        item.addActionListener {
            zipPaths(
                    Files.list(Paths.get(Setting.articleFolder()))
                            .sorted { p1, p2 -> Files.getLastModifiedTime(p1).compareTo(Files.getLastModifiedTime(p2)) * -1 }
                            .filter {
                                val name = it.fileName.toString()
                                name.startsWith("20") || name.startsWith("『")
                            }
                            .collect(Collectors.toList())
                            .subList(0, 1500)
            )
        }
        return item
    }

    private fun zipPaths(paths: Collection<Path>) {
        ZipArchiver().invoke(paths)
    }

}