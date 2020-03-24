package jp.toastkid.yobidashi.compact.model

import java.awt.Desktop
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Article(private val file: Path) {

    private val title = file.fileName.toString().substring(0, file.fileName.toString().lastIndexOf("."))

    fun getTitle() = title

    fun open() {
        try {
            Desktop.getDesktop().open(file.toFile())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {

        fun withTitle(title: String): Article {
            val file = Paths.get(Setting.articleFolder(), "$title.md")
            if (!Files.exists(file)) {
                Files.createFile(file)
                Files.write(file, ArticleTemplate()(title).toByteArray(StandardCharsets.UTF_8))
            }
            return Article(file)
        }
    }
}