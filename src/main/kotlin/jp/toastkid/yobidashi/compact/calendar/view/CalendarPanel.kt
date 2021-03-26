package jp.toastkid.yobidashi.compact.calendar.view

import jp.toastkid.yobidashi.compact.calendar.service.OffDayFinderService
import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.LineBorder

class CalendarPanel : JPanel() {

    private val dayLabels = Array(6) { arrayOfNulls<JLabel>(7) }

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(ChooserPanelFactory({ year, month -> refreshDayLabels(year, month) }).invoke())
        add(makeDayPanel())

        val date = LocalDate.now()
        refreshDayLabels(date, true)
    }

    private fun makeDayPanel(): JPanel {
        val dayPanel = JPanel()
        val layout = GridLayout(7, 7)
        layout.hgap = 3
        layout.vgap = 3
        dayPanel.layout = layout

        val dayOfWeekLabelFactory = DayOfWeekLabelFactory(DAY_LABEL_SIZE)
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.SUNDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.MONDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.TUESDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.WEDNESDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.THURSDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.FRIDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.SATURDAY))

        val dayLabelFactory = DayLabelFactory(DAY_LABEL_SIZE)

        for (i in 0..5) {
            for (j in 0..6) {
                dayLabels[i][j] = dayLabelFactory.invoke(j)
                dayPanel.add(dayLabels[i][j])
            }
        }
        return dayPanel
    }

    private fun refreshDayLabels(year: Int, month: Month) {
        val cal = LocalDate.now()
        if (month == cal.month && year == cal.year) {
            refreshDayLabels(cal, true)
        } else {
            refreshDayLabels(LocalDate.of(year, month.ordinal + 1, 1), false)
        }
    }

    private fun refreshDayLabels(calendar: LocalDate, currentMonth: Boolean) {
        val maxDate = calendar.month.length(calendar.isLeapYear)
        val today = calendar.dayOfMonth
        val firstDay = calendar.withDayOfMonth(1)
        val firstDayOfWeek = firstDay.dayOfWeek.value % 7
        for (ll in dayLabels) {
            for (l in ll) {
                l?.text = ""
            }
        }

        val year = calendar.year
        val month = calendar.monthValue
        val offDayFinder = OffDayFinderService()
        for (day in 1..maxDate) {
            getDayLabel(day, firstDayOfWeek)?.also {
                it.text = day.toString()
                val color = if (offDayFinder(year, month, day, (day + firstDayOfWeek - 1) % 7)) {
                    Color.RED
                } else {
                    Color.BLACK
                }
                when (it.foreground) {
                    Color.BLACK -> it.foreground = color
                    Color.RED -> it.foreground = color
                }
            }
        }

        for (i in dayLabels.indices) {
            for (element in dayLabels[i]) {
                element?.also {
                    it.background = DAY_BG
                    it.border = BORDER
                    it.isVisible = it.text.isNotEmpty()
                }
            }
        }
        if (currentMonth) {
            getDayLabel(today, firstDayOfWeek)?.also {
                it.background = TODAY_BG
                it.border = TODAY_BORDER
            }
        }
    }

    private fun getDayLabel(day: Int, firstDayOfWeek: Int): JLabel? {
        return dayLabels[(day + firstDayOfWeek - 1) / 7][(day + firstDayOfWeek - 1) % 7]
    }

    companion object {
        private val DAY_BG: Color = Color(250, 250, 255)
        private val BORDER = LineBorder(Color(220, 220, 220, 220), 2, false)

        private val TODAY_BG: Color = Color(220, 220, 255)
        private val TODAY_BORDER = LineBorder(Color(50, 50, 175), 2, false)

        private val DAY_LABEL_SIZE = Dimension(50, 50)
    }
}

fun main() {
    val frame = JFrame("Calender")
    frame.add(CalendarPanel())
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.pack()
    frame.isVisible = true
}