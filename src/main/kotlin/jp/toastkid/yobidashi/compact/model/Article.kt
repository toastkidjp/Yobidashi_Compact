package jp.toastkid.yobidashi.compact.model

import jp.toastkid.yobidashi.compact.editor.service.OpenEditorService
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Article(private val file: Path) {

    private val title = file.toFile().nameWithoutExtension

    fun getTitle() = title

    fun open() {
        try {
            OpenEditorService().invoke(this)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun makeFile(contentSupplier: () -> String) {
        Files.createFile(file)
        Files.write(file, contentSupplier().toByteArray(StandardCharsets.UTF_8))
    }

    fun count(): Int {
        return Files.readAllLines(file).map { it.codePointCount(0, it.length) }.sum()
    }

    fun lastModified(): Long {
        return try {
            Files.getLastModifiedTime(file).toMillis()
        } catch (e: NoSuchFieldException) {
            0L
        }
    }

    fun path() = file

    companion object {

        fun withTitle(title: String): Article {
            val file = Paths.get(Setting.articleFolder(), "$title.md")
            return Article(file)
        }
    }
}