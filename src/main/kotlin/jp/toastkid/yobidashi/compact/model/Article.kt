package jp.toastkid.yobidashi.compact.model

import jp.toastkid.yobidashi.compact.editor.OpenEditorUseCase
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Article(private val file: Path) {

    // TODO use [ExtensionRemover]
    private val title = file.fileName.toString().substring(0, file.fileName.toString().lastIndexOf("."))

    fun getTitle() = title

    fun open() {
        try {
            OpenEditorUseCase().invoke(this)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun makeFieIfNeed(contentSupplier: () -> String) {
        if (Files.exists(file)) {
            return
        }
        Files.createFile(file)
        Files.write(file, contentSupplier().toByteArray(StandardCharsets.UTF_8))
    }

    fun count(): Int {
        return Files.readAllLines(file).map { it.codePointCount(0, it.length) }.sum()
    }

    fun lastModified(): Long {
        return Files.getLastModifiedTime(file).toMillis()
    }

    fun path() = file

    companion object {

        fun withTitle(title: String): Article {
            val file = Paths.get(Setting.articleFolder(), "$title.md")
            return Article(file)
        }
    }
}