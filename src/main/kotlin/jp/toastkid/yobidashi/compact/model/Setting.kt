package jp.toastkid.yobidashi.compact.model

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

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

    fun lookAndFeel() = properties.getProperty("look_and_feel")

    fun setLookAndFeel(newValue: String) {
        properties.setProperty("look_and_feel", newValue)
    }

    fun save() {
        properties.store(Files.newBufferedWriter(Paths.get(PATH)), null)
    }
}