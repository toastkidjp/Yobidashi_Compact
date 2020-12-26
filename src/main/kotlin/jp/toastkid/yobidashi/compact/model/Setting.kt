package jp.toastkid.yobidashi.compact.model

import jp.toastkid.yobidashi.compact.service.ColorDecoderService
import java.awt.Color
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

    fun setUseCaseSensitiveInFinder(use: Boolean) {
        properties.setProperty("use_case_sensitive", use.toString())
    }

    fun useCaseSensitiveInFinder(): Boolean {
        return properties.getProperty("use_case_sensitive")?.toBoolean() ?: false
    }

    fun setEditorBackgroundColor(color: Color) {
        properties.setProperty("editor_background_color", Integer.toHexString(color.rgb))
    }

    fun editorBackgroundColor(): Color {
        return ColorDecoderService().invoke(properties.getProperty("editor_background_color"))
                ?: Color(225, 225, 225, 255)
    }

    fun setEditorForegroundColor(color: Color) {
        properties.setProperty("editor_foreground_color", Integer.toHexString(color.rgb))
    }

    fun editorForegroundColor(): Color {
        return ColorDecoderService().invoke(properties.getProperty("editor_foreground_color"))
                ?: Color.BLACK
    }

    fun resetEditorColorSetting() {
        properties.remove("editor_foreground_color")
        properties.remove("editor_background_color")
    }

    fun setEditorFontFamily(fontFamily: String?) {
        fontFamily?.let {
            properties.setProperty("editor_font_family", it)
        }
    }

    fun editorFontFamily(): String? {
        return properties.getProperty("editor_font_family")
    }

    fun save() {
        properties.store(Files.newBufferedWriter(Paths.get(PATH)), null)
    }

}
