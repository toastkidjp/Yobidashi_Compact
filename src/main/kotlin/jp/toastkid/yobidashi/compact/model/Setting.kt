package jp.toastkid.yobidashi.compact.model

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Properties

/**
 * You should put "settings.properties" to same folder.
 * And you should write following style
 * <pre>
 * article.folder=C://Users/user_name/Documents/files/articles
 * </pre>
 */
object Setting {
    private const val PATH = "setting.properties"

    private val properties = Properties()

    init {
        properties.load(Files.newBufferedReader(Paths.get(PATH)))
    }

    fun articleFolder() = properties.getProperty("article.folder")

    fun articleFolderFile() = File(articleFolder())

    fun lookAndFeel() = properties.getProperty("look_and_feel")

    fun sorting() = Sorting.findByName(properties.getProperty("sorting"))

    fun setSorting(newValue: Sorting) {
        properties.setProperty("sorting", newValue.name)
    }

    fun setLookAndFeel(newValue: String) {
        properties.setProperty("look_and_feel", newValue)
    }

    fun setUseInternalEditor(newValue: Boolean) {
        properties.setProperty("use_internal_editor", newValue.toString())
    }

    fun useInternalEditor(): Boolean {
        return properties.getProperty("use_internal_editor")?.toBoolean() ?: false
    }

    fun userOffDay(): List<Pair<Int, Int>> {
        val offDayString = properties.getProperty("user_off_day") ?: return emptyList()
        return offDayString.split(",")
                .filter { it.contains("/") }
                .map { it.split("/").let { it[0].toInt() to it[1].toInt() } }
    }

    fun save() {
        properties.store(Files.newBufferedWriter(Paths.get(PATH)), null)
    }
}