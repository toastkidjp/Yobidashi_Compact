package jp.toastkid.yobidashi.compact.calendar.service

import java.awt.Color
import java.time.LocalDate
import java.time.Month
import javax.swing.JLabel
import javax.swing.border.LineBorder

class DayLabelRefresherService(private val dayLabels:  Array<Array<JLabel?>>) {

    operator fun invoke(year: Int, month: Month) {
        val cal = LocalDate.now()
        if (month == cal.month && year == cal.year) {
            invoke(cal, true)
        } else {
            invoke(LocalDate.of(year, month.ordinal + 1, 1), false)
        }
    }

    operator fun invoke(calendar: LocalDate, currentMonth: Boolean) {
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
    }
}