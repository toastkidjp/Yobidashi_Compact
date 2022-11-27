package jp.toastkid.yobidashi.compact.calendar.view

import jp.toastkid.yobidashi.compact.calendar.service.DayLabelRefresherService
import java.time.LocalDate
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

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

}

fun main() {
    val frame = JFrame("Calender")
    frame.add(CalendarPanel())
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.pack()
    frame.isVisible = true
}