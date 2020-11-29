package jp.toastkid.yobidashi.compact.calendar.view

import jp.toastkid.yobidashi.compact.calendar.model.DayOfWeek
import java.awt.Color
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.SwingConstants
import javax.swing.border.LineBorder

class DayOfWeekLabelFactory(private val preferredSize: Dimension) {

    operator fun invoke(i: Int): JComponent {
        val dayOfWeekLabel = JLabel(DayOfWeek.getName(i))
        dayOfWeekLabel.horizontalAlignment = SwingConstants.CENTER
        dayOfWeekLabel.verticalAlignment = SwingConstants.CENTER
        dayOfWeekLabel.preferredSize = preferredSize
        dayOfWeekLabel.isOpaque = true
        dayOfWeekLabel.background = WEEK_BG
        dayOfWeekLabel.border = BORDER
        if (i == 0) {
            dayOfWeekLabel.foreground = Color.RED
        } else if (i == 6) {
            dayOfWeekLabel.foreground = Color.BLUE
        }
        return dayOfWeekLabel
    }

    companion object {

        private val WEEK_BG: Color = Color(240, 240, 255)

        private val BORDER = LineBorder(Color(220, 220, 220, 220), 2, false)

    }

}