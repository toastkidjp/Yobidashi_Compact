package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.ArticleTemplate
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.TodayFileTitleGenerator
import jp.toastkid.yobidashi.compact.service.ZipArchiver
import jp.toastkid.yobidashi.compact.viewmodel.ZipViewModel
import java.awt.Desktop
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import javax.swing.JCheckBoxMenuItem
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.KeyStroke

class FileMenuView {

    operator fun invoke(): JMenu {
        val fileMenu = JMenu("File(F)")
        fileMenu.setMnemonic('F')

        fileMenu.add(makeTodayMenuItem())
        fileMenu.add(makeNewMenuItem())
        fileMenu.add(makeZipAllMenuItem())
        fileMenu.add(makeZipDiaryMenuItem())

        val item = JMenuItem("Open folder")
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK)
        item.addActionListener {
            Desktop.getDesktop().open(Setting.articleFolderFile())
        }
        fileMenu.add(item)

        fileMenu.add(makeSwitchInternalBrowserMenu())

        fileMenu.add(makeExit())
        return fileMenu
    }

    private fun makeSwitchInternalBrowserMenu(): JCheckBoxMenuItem {
        val useInternalEditorMenu = JCheckBoxMenuItem("Use internal editor", Setting.useInternalEditor())
        useInternalEditorMenu.addChangeListener {
            Setting.setUseInternalEditor(useInternalEditorMenu.isSelected)
        }
        return useInternalEditorMenu
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
            val title = TodayFileTitleGenerator().invoke(System.currentTimeMillis()) ?: return@addActionListener
            val article = Article.withTitle(title)
            if (Setting.articleFolderFile().listFiles()?.any { it.nameWithoutExtension == article.getTitle() } == true) {
                return@addActionListener
            }
            article.makeFieIfNeed { ArticleTemplate()(article.getTitle()) }
            SubjectPool.addToList(article)
        }
        return item
    }

    private fun makeNewMenuItem(): JMenuItem {
        val item = JMenuItem("Make new")
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK)
        item.addActionListener {
            val dialog = JOptionPane.showInputDialog("Please input new article name.")
            if (dialog.isNullOrBlank()) {
                return@addActionListener
            }
            val article = Article.withTitle(dialog)
            article.makeFieIfNeed { "# ${article.getTitle()}" }
            SubjectPool.addToList(article)
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
        Desktop.getDesktop().open(File("."))
    }

}