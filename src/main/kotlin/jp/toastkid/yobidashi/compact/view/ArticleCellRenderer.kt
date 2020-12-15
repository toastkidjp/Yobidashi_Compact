package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.Article
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer
import javax.swing.border.LineBorder

class ArticleCellRenderer :  ListCellRenderer<Article> {

    private val dateFormat = ThreadLocal.withInitial {
        return@withInitial SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss", Locale.ENGLISH)
    }

    override fun getListCellRendererComponent(
            list: JList<out Article>?,
            value: Article?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
    ): Component {
        val view = JPanel()
        view.layout = BorderLayout()

        val title = JLabel()
        title.text = value?.getTitle()

        val lastUpdated = JLabel()
        lastUpdated.text = dateFormat.get().format(Date().also { it.time = value?.lastModified() ?: 0L })

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