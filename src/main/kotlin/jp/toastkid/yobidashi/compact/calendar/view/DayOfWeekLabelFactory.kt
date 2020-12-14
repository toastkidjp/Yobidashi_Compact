package jp.toastkid.yobidashi.compact.calendar.view

import java.awt.Color
import java.awt.Dimension
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.SwingConstants
import javax.swing.border.LineBorder

class DayOfWeekLabelFactory(private val preferredSize: Dimension) {

    operator fun invoke(dayOfWeek: DayOfWeek): JComponent {
        val dayOfWeekLabel = JLabel(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH))
        dayOfWeekLabel.font = dayOfWeekLabel.font.deriveFont(16f)
        dayOfWeekLabel.horizontalAlignment = SwingConstants.CENTER
        dayOfWeekLabel.verticalAlignment = SwingConstants.CENTER
        dayOfWeekLabel.preferredSize = preferredSize
        dayOfWeekLabel.isOpaque = true
        dayOfWeekLabel.background = WEEK_BG
        dayOfWeekLabel.border = BORDER
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            dayOfWeekLabel.foreground = Color.RED
        } else if (dayOfWeek == DayOfWeek.SATURDAY) {
            dayOfWeekLabel.foreground = Color.BLUE
        }
        return dayOfWeekLabel
    }

    companion object {

        private val WEEK_BG: Color = Color(240, 240, 255)

        private val BORDER = LineBorder(Color(220, 220, 220, 220), 2, false)

    }

}