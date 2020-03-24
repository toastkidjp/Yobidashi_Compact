package jp.toastkid.yobidashi.compact.model

import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files

class Article(private val file: File) {

    private val title = file.name.substring(0, file.name.lastIndexOf("."))

    fun getTitle() = title

    fun open() {
        try {
            Desktop.getDesktop().open(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {

        fun withTitle(title: String): Article {
            val file = File("C://Users/toastkidjp/Documents/Article2", "$title.md")
            if (!file.exists()) {
                file.createNewFile()
                Files.write(file.toPath(), ArticleTemplate()(title).toByteArray(StandardCharsets.UTF_8))
            }
            return Article(file)
        }
    }
}