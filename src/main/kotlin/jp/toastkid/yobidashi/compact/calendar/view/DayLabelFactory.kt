package jp.toastkid.yobidashi.compact.calendar.view

import java.awt.Color
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.SwingConstants
import javax.swing.border.LineBorder

class DayLabelFactory(private val preferredSize: Dimension) {

    operator fun invoke(dayOfWeek: Int): JLabel {
        val dayLabel = JLabel()
        dayLabel.font = dayLabel.font.deriveFont(16f)
        dayLabel.horizontalAlignment = SwingConstants.CENTER
        dayLabel.verticalAlignment = SwingConstants.CENTER
        dayLabel.isOpaque = true
        dayLabel.background = DAY_BG
        dayLabel.foreground = if (dayOfWeek == 0 || dayOfWeek == 6) OFF_DAY_FG else DAY_FG
        dayLabel.preferredSize = preferredSize
        dayLabel.border = LineBorder(Color.DARK_GRAY, 2, false)
        return dayLabel
    }

    companion object {
        private val DAY_FG: Color = Color.BLACK
        private val OFF_DAY_FG:  Color = Color(190, 50, 55)
        private val DAY_BG: Color = Color(250, 250, 255)
    }

}