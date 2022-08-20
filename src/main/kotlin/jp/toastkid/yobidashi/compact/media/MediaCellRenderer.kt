package jp.toastkid.yobidashi.compact.media

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer
import javax.swing.border.LineBorder
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.nameWithoutExtension

class MediaCellRenderer : ListCellRenderer<Path> {

    private val dateFormat = ThreadLocal.withInitial {
        return@withInitial SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss", Locale.ENGLISH)
    }

    override fun getListCellRendererComponent(
            list: JList<out Path>?,
            value: Path?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
    ): Component {
        val view = JPanel()
        view.layout = BorderLayout()

        val title = JLabel()
        title.text = value?.nameWithoutExtension

        val lastUpdated = JLabel()
        lastUpdated.text = dateFormat.get().format(Date().also { it.time = value?.getLastModifiedTime()?.toMillis() ?: 0L })

        view.add(title, BorderLayout.NORTH)
        view.add(lastUpdated, BorderLayout.SOUTH)

        view.background = if (isSelected) list?.selectionBackground else list?.background
        view.foreground = if (isSelected) list?.selectionForeground else list?.foreground
        title.foreground = if (isSelected) Color.WHITE else Color.BLACK
        lastUpdated.foreground = if (isSelected) Color.WHITE else Color.BLACK

        view.border = LineBorder(Color.GRAY, 1, false)
        return view
    }

}