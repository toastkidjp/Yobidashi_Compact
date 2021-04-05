package jp.toastkid.yobidashi.compact.calendar.view

import jp.toastkid.yobidashi.compact.calendar.service.DayLabelRefresherService
import java.awt.Color
import java.time.LocalDate
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.LineBorder

class CalendarPanel : JPanel() {

    private val dayLabels:  Array<Array<JLabel?>>

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        val panelAndDayLabels = DayPanelFactory().invoke()
        dayLabels = panelAndDayLabels.second

        val dayLabelRefresherService = DayLabelRefresherService(dayLabels)

        add(ChooserPanelFactory({ year, month -> dayLabelRefresherService(year, month) }).invoke())
        add(panelAndDayLabels.first)

        val date = LocalDate.now()
        dayLabelRefresherService(date, true)
    }

    private fun getDayLabel(day: Int, firstDayOfWeek: Int): JLabel? {
        return dayLabels[(day + firstDayOfWeek - 1) / 7][(day + firstDayOfWeek - 1) % 7]
    }

    companion object {
        private val DAY_BG: Color = Color(250, 250, 255)
        private val BORDER = LineBorder(Color(220, 220, 220, 220), 2, false)

        private val TODAY_BG: Color = Color(220, 220, 255)
        private val TODAY_BORDER = LineBorder(Color(50, 50, 175), 2, false)
    }
}

fun main() {
    val frame = JFrame("Calender")
    frame.add(CalendarPanel())
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.pack()
    frame.isVisible = true
}